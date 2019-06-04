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
import com.topartes.verificaduvida.adapter.RespostaListAdapter;
import com.topartes.verificaduvida.controller.AvaliacaoCtrl;
import com.topartes.verificaduvida.controller.RespostaCtrl;
import com.topartes.verificaduvida.datamodel.AvaliacaoDataModel;
import com.topartes.verificaduvida.datamodel.RespostaDataModel;
import com.topartes.verificaduvida.model.Avaliacao;
import com.topartes.verificaduvida.model.Resposta;
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

public class RespostaFragment extends Fragment {

    public ImageView imagePergunta;
    public String imgPergunta;
    public String txtPergunta;
    public int idPergunta, idResposta;
    Util util = new Util();
    ArrayList<Resposta> dataSet;
    ListView listView;
    TextView txtViewPergunta;
    Button btnAtualResposta, btnResponder;
    Context context;
    RespostaCtrl respostaCtrl;
    AvaliacaoCtrl avaliacaoCtrl;
    Bundle bundle;
    String txtResposta;
    String imgResposta;
    Bitmap bitmap;
    FormRespostaFragment formRespostaFragment = new FormRespostaFragment();
    ComentarioFragment comentarioFragment = new ComentarioFragment();

    View view;

    public RespostaFragment() {
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
        view = inflater.inflate(R.layout.fragment_resposta, container, false);

        getActivity().setTitle("Pregunta/Respostas");

        respostaCtrl = new RespostaCtrl(getContext());
        txtViewPergunta = view.findViewById(R.id.txtViewPergunta);
        imagePergunta = view.findViewById(R.id.imagePergunta);
        btnResponder = view.findViewById(R.id.btnResponder);
        listView = view.findViewById(R.id.listViewResposta);
        btnAtualResposta = view.findViewById(R.id.btnAtualResposta);

        bundle = getArguments();

        respostaCtrl.idPergunta = idPergunta;
        try {
            bitmap = util.base64ToBitmap(imgPergunta);
        } catch (Exception e) {
            bitmap = null;
        }
        imagePergunta.setImageBitmap(bitmap);
        txtViewPergunta.setText(txtPergunta);

        dataSet = respostaCtrl.getRespostasPergunta();

        final RespostaListAdapter adapter =
                new RespostaListAdapter(dataSet, getContext());
        listView.setAdapter(adapter);

        btnResponder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar.make(view, txtPergunta + "\nSelecionada", Snackbar.LENGTH_LONG)
                        .setAction("Listar Materias: ", null).show();

                formRespostaFragment.idPergunta = idPergunta;
                formRespostaFragment.imgPergunta = imgPergunta;
                formRespostaFragment.txtPergunta = txtPergunta;

                Bundle bundle = new Bundle();
                bundle.putInt("idPergunta", idPergunta);
                bundle.putString("imgPergunta", imgPergunta);
                bundle.putString("txtPergunta", txtPergunta);
                formRespostaFragment.setArguments(bundle);

                abreFormResposta();
            }
        });

        btnAtualResposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                RespostaFragment.AsyncTaskResposta taskResposta = new RespostaFragment.AsyncTaskResposta();
                taskResposta.execute();

                RespostaFragment.AsyncTaskAvaliacao taskAvaliacao = new RespostaFragment.AsyncTaskAvaliacao();
                taskAvaliacao.execute();


                dataSet = respostaCtrl.getRespostasPergunta();
                adapter.atualizarRespostas(dataSet);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                getActivity().setTitle("Resposta/Comentários");

                Resposta objResposta = dataSet.get(position);

                Snackbar.make(view, objResposta.getResposta() + "\nSelecionada", Snackbar.LENGTH_LONG)
                        .setAction("Listar Respostas: ", null).show();

                idResposta = objResposta.getIdpk();
                imgResposta = objResposta.getImagem();
                txtResposta = objResposta.getResposta();

                comentarioFragment.idResposta = objResposta.getIdpk();
                comentarioFragment.imgResposta = objResposta.getImagem();
                comentarioFragment.txtResposta = objResposta.getResposta();

                Bundle bundle = new Bundle();
                bundle.putInt("idResposta", idResposta);
                bundle.putString("imgResposta", imgResposta);
                bundle.putString("txtResposta", txtResposta);
                comentarioFragment.setArguments(bundle);

                abreComentarios();
            }
        });
        return view;
    }

    public void abreComentarios() {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.content_fragment, comentarioFragment);
        transaction.addToBackStack(null);

        transaction.commit();

    }

    public void abreFormResposta() {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.content_fragment, formRespostaFragment);
        transaction.addToBackStack(null);

        transaction.commit();

    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskResposta extends AsyncTask<String, String, String> {

        ProgressDialog progressDiaglog = new ProgressDialog(getContext());

        HttpURLConnection conn;
        URL url = null;

        Uri.Builder builder;

        public AsyncTaskResposta() {

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

                url = new URL(Util.URL_WEB_SERVICE + "APISincronizarResposta.php");

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

                    InputStream inputResposta = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputResposta));
                    StringBuilder resultResposta = new StringBuilder();

                    String line;

                    Log.i("RESPOSTA MySQL", "[" + resultResposta + "]");

                    while ((line = reader.readLine()) != null)
                        resultResposta.append(line);
                    Log.e("response", "" + resultResposta);
                    return (resultResposta.toString());
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
        protected void onPostExecute(String resultResposta) {

            try {
                JSONArray jArrayResposta = new JSONArray(resultResposta);

                if (jArrayResposta.length() != 0) {

                    respostaCtrl.deletarTabela(RespostaDataModel.getTABELA());
                    respostaCtrl.criarTabela(RespostaDataModel.criarTabela());

                    // Salvar dados recebidos no BD SQLite

                    for (int u = 0; u < jArrayResposta.length(); u++) {

                        JSONObject jsonResposta = jArrayResposta.getJSONObject(u);
                        Resposta objResposta = new Resposta();

                        objResposta.setId((jsonResposta.getInt(RespostaDataModel.getId())));
                        objResposta.setIdpk((jsonResposta.getInt(RespostaDataModel.getId())));
                        objResposta.setImagem((jsonResposta.getString(RespostaDataModel.getImagem())));
                        objResposta.setResposta((jsonResposta.getString(RespostaDataModel.getResposta())));
                        objResposta.setFk_pergunta((jsonResposta.getInt(RespostaDataModel.getFk_pergunta())));
                        objResposta.setFk_usuario((jsonResposta.getInt(RespostaDataModel.getFk_usuario())));
                        objResposta.setData((jsonResposta.getString(RespostaDataModel.getData())));

                        respostaCtrl.salvar(objResposta);

                    }
                } else {
                    Util.showMensagem(context, "Nenhum registro encontrado...");
                }

            } catch (Exception e) {

                Log.e("WEBService", "Erro JSONException - " + e.getMessage());

            } finally {

                if (progressDiaglog != null && progressDiaglog.isShowing()) {
                    progressDiaglog.dismiss();

                    dataSet = respostaCtrl.getRespostasPergunta();

                    final RespostaListAdapter adapter =
                            new RespostaListAdapter(dataSet, getContext());
                    listView.setAdapter(adapter);

                }

            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskAvaliacao extends AsyncTask<String, String, String> {

        ProgressDialog progressDiaglog = new ProgressDialog(getContext());

        HttpURLConnection conn;
        URL url = null;

        Uri.Builder builder;

        public AsyncTaskAvaliacao() {

            this.builder = new Uri.Builder();

            // "app" nome do dispositivo autrizado
            // "MediaEscolar" key da aplicação permitida
            // A key deve ser criptografada para manter a segurança

            builder.appendQueryParameter("app", "VerificaMateria");

        }

        @Override
        protected void onPreExecute() {
            progressDiaglog.setMessage("As Avaliações estão sendo atualizadas, por favor aguarde...");
            progressDiaglog.setCancelable(false);
            progressDiaglog.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            // Monta URL com script PHP
            try {

                url = new URL(Util.URL_WEB_SERVICE + "APISincronizarAvaliacao.php");

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

                    InputStream inputAvaliacao = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputAvaliacao));
                    StringBuilder resultAvaliacao = new StringBuilder();

                    String line;

                    Log.i("RESPOSTA MySQL", "[" + resultAvaliacao + "]");

                    while ((line = reader.readLine()) != null)
                        resultAvaliacao.append(line);
                    Log.e("response", "" + resultAvaliacao);
                    return (resultAvaliacao.toString());
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
        protected void onPostExecute(String resultAvaliacao) {

            try {
                JSONArray jArrayAvaliacao = new JSONArray(resultAvaliacao);

                if (jArrayAvaliacao.length() != 0) {
                    // Salvar dados recebidos no BD SQLite

                    for (int i = 0; i < jArrayAvaliacao.length(); i++) {

                        JSONObject jsonAvaliacao = jArrayAvaliacao.getJSONObject(i);

                        Avaliacao objAvaliacao = new Avaliacao();
                        objAvaliacao.setFk_Usuario(jsonAvaliacao.getInt(AvaliacaoDataModel.getFk_usuario()));
                        objAvaliacao.setFk_Resposta(jsonAvaliacao.getInt(AvaliacaoDataModel.getFk_resposta()));
                        objAvaliacao.setAvaliacao(jsonAvaliacao.getInt(AvaliacaoDataModel.getAvaliacao()));
                        objAvaliacao.setData(jsonAvaliacao.getString(AvaliacaoDataModel.getData()));

                        respostaCtrl.salvarAvaliacao(objAvaliacao);

                    }
                } else {
                    Util.showMensagem(context, "Nenhum registro encontrado...");
                }

            } catch (Exception e) {

                Log.e("WEBService", "Erro JSONException - " + e.getMessage());

            } finally {

                if (progressDiaglog != null && progressDiaglog.isShowing()) {
                    progressDiaglog.dismiss();

                    dataSet = respostaCtrl.getRespostasPergunta();

                    final RespostaListAdapter adapter =
                            new RespostaListAdapter(dataSet, getContext());
                    listView.setAdapter(adapter);

                }

            }
        }

    }

}
