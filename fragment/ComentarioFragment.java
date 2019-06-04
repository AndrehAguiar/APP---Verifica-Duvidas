package com.topartes.verificaduvida.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.topartes.verificaduvida.R;
import com.topartes.verificaduvida.adapter.ComentarioListAdapter;
import com.topartes.verificaduvida.controller.ComentarioCtrl;
import com.topartes.verificaduvida.datamodel.ComentarioDataModel;
import com.topartes.verificaduvida.model.Comentario;
import com.topartes.verificaduvida.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ComentarioFragment extends Fragment {

    public int idResposta, idComentario;
    Util util = new Util();
    ArrayList<Comentario> dataSet;
    ListView listView;
    TextView txtViewResposta;
    ImageView imageResposta;
    Button btnAtualComentario, btnComentar;
    Context context;
    ComentarioCtrl comentarioCtrl;
    Bundle bundle;
    String imgResposta, txtResposta, comentario, imagemComentario;
    Bitmap bitmap;
    View view;

    public ComentarioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comentario, container, false);

        comentarioCtrl = new ComentarioCtrl(getContext());

        txtViewResposta = view.findViewById(R.id.txtViewResposta);
        imageResposta = view.findViewById(R.id.imageResposta);
        btnComentar = view.findViewById(R.id.btnComentar);
        listView = view.findViewById(R.id.listViewComentario);
        btnAtualComentario = view.findViewById(R.id.btnAtualComentario);

        bundle = getArguments();

        comentarioCtrl.idResposta = idResposta;
        try {
            bitmap = util.base64ToBitmap(imgResposta);
        } catch (Exception e) {
            bitmap = null;
        }
        imageResposta.setImageBitmap(bitmap);
        txtViewResposta.setText(txtResposta);

        dataSet = comentarioCtrl.getComentariosResposta();

        final ComentarioListAdapter adapter =
                new ComentarioListAdapter(dataSet, getContext());
        listView.setAdapter(adapter);

        btnComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar.make(view, txtResposta + "\nSelecionada", Snackbar.LENGTH_LONG)
                        .setAction("Listar Materias: ", null).show();

                FormComentarioFragment formComentarioFragment = new FormComentarioFragment();

                formComentarioFragment.idResposta = idResposta;
                formComentarioFragment.imgResposta = imgResposta;
                formComentarioFragment.txtResposta = txtResposta;

                Bundle bundle = new Bundle();
                bundle.putInt("idResposta", idResposta);
                bundle.putString("imgResposta", imgResposta);
                bundle.putString("txtResposta", txtResposta);
                formComentarioFragment.setArguments(bundle);

                getActivity().setTitle("Descreva o Comentário");

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.content_fragment, formComentarioFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        btnAtualComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ComentarioFragment.AsyncTaskComentario taskComentario = new ComentarioFragment.AsyncTaskComentario();
                taskComentario.execute();
                dataSet = comentarioCtrl.getComentariosResposta();
                adapter.atualizarComentarios(dataSet);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Comentario objComentario = dataSet.get(position);

                Snackbar.make(view, objComentario.getComentario() + "\nSelecionada", Snackbar.LENGTH_LONG)
                        .setAction("Listar Respostas: ", null).show();

                idComentario = objComentario.getIdpk();
            }
        });
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskComentario extends AsyncTask<String, String, String> {

        ProgressDialog progressDiaglog = new ProgressDialog(getContext());

        HttpURLConnection conn;
        URL url = null;

        Uri.Builder builder;

        public AsyncTaskComentario() {

            this.builder = new Uri.Builder();

            // "app" nome do dispositivo autrizado
            // "MediaEscolar" key da aplicação permitida
            // A key deve ser criptografada para manter a segurança

            builder.appendQueryParameter("app", "VerificaMateria");

        }

        @Override
        protected void onPreExecute() {
            progressDiaglog.setMessage("Os dados estão sendo atualizados, por favor aguarde...");
            progressDiaglog.setCancelable(false);
            progressDiaglog.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            // Monta URL com script PHP
            try {

                url = new URL(Util.URL_WEB_SERVICE + "APISincronizarComentario.php");

            } catch (MalformedURLException e) {

                Log.e("WEBService", "MalformedURLException - " + e.getMessage());

            } catch (Exception e) {

                Log.e("WEBService", "Exception - " + e.getMessage());
            }

            try {

                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(Util.CONECTION_TIMEOUT);
                conn.setReadTimeout(Util.READ_TIME_OUT);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("charset", "utf-8");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();

            } catch (IOException e) {

                Log.e("WEBService", "IOException - " + e.getMessage());

            }

            try {
                int response_code = conn.getResponseCode();

                /* Lista de ResponseCodes comuns
                 * 200 OK
                 * 403 forbideen
                 * 404 pg não encontrada
                 * 500 erro interno no servidor */

                if (response_code == HttpURLConnection.HTTP_OK) {

                    InputStream inputComentario = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputComentario));
                    StringBuilder resultComentario = new StringBuilder();

                    String line;

                    Log.i("COMENTARIO MySQL", "[" + resultComentario + "]");

                    while ((line = reader.readLine()) != null)
                        resultComentario.append(line);
                    Log.e("response", "" + resultComentario);
                    return (resultComentario.toString());
                } else {
                    return "Erro de conexão";
                }

            } catch (IOException e) {

                Log.e("WEBService", "IOException - " + e.getMessage());
                return e.toString();

            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String resultComentario) {

            try {
                JSONArray jArrayComentario = new JSONArray(resultComentario);

                if (jArrayComentario.length() != 0) {

                    comentarioCtrl.deletarTabela(ComentarioDataModel.getTABELA());
                    comentarioCtrl.criarTabela(ComentarioDataModel.criarTabela());

                    // Salvar dados recebidos no BD SQLite

                    for (int u = 0; u < jArrayComentario.length(); u++) {

                        JSONObject jsonComentario = jArrayComentario.getJSONObject(u);
                        Comentario objComentario = new Comentario();

                        objComentario.setIdpk((jsonComentario.getInt(ComentarioDataModel.getId())));
                        objComentario.setImagem((jsonComentario.getString(ComentarioDataModel.getImagem())));
                        objComentario.setComentario((jsonComentario.getString(ComentarioDataModel.getComentario())));
                        objComentario.setFk_resposta((jsonComentario.getInt(ComentarioDataModel.getFk_resposta())));
                        objComentario.setFk_usuario((jsonComentario.getInt(ComentarioDataModel.getFk_usuario())));
                        objComentario.setData((jsonComentario.getString(ComentarioDataModel.getData())));

                        comentarioCtrl.salvar(objComentario);

                    }
                } else {
                    Util.showMensagem(context, "Nenhum registro encontrado...");
                }

            } catch (Exception e) {

                Log.e("WEBService", "Erro JSONException - " + e.getMessage());

            } finally {

                if (progressDiaglog != null && progressDiaglog.isShowing()) {
                    progressDiaglog.dismiss();

                    dataSet = comentarioCtrl.getComentariosResposta();

                    final ComentarioListAdapter adapter =
                            new ComentarioListAdapter(dataSet, getContext());
                    listView.setAdapter(adapter);

                }

            }
        }

    }

}
