package com.topartes.verificaduvida.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.topartes.verificaduvida.R;
import com.topartes.verificaduvida.adapter.PerguntaListAdapter;
import com.topartes.verificaduvida.controller.PerguntaCtrl;
import com.topartes.verificaduvida.datamodel.PerguntaDataModel;
import com.topartes.verificaduvida.model.Pergunta;
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

public class PrincipalFragment extends Fragment {

    public int idPergunta;
    ArrayList<Pergunta> dataSet;
    ListView listView;
    Button btnAtualPergunta;
    Context context;
    PerguntaCtrl perguntaCtrl;
    View view;
    ImageView imgResponder;
    String txtPergunta, imgPergunta;
    RespostaFragment respostaFragment = new RespostaFragment();
    FormRespostaFragment formRespostaFragment = new FormRespostaFragment();

    public PrincipalFragment() {
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
        view = inflater.inflate(R.layout.fragment_principal, container, false);

        perguntaCtrl = new PerguntaCtrl(getContext());
        listView = view.findViewById(R.id.listViewPergunta);
        btnAtualPergunta = view.findViewById(R.id.btnAtualPergunta);


        dataSet = perguntaCtrl.getAllPerguntas();
        final PerguntaListAdapter adapter =
                new PerguntaListAdapter(dataSet, getContext());

        listView.setAdapter(adapter);

        btnAtualPergunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PrincipalFragment.AsyncTaskPergunta taskPergunta = new PrincipalFragment.AsyncTaskPergunta();
                taskPergunta.execute();
                dataSet = perguntaCtrl.getAllPerguntas();
                adapter.atualizarPerguntas(dataSet);
            }
        });

        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getActivity().setTitle("Resposts");
                //noinspection SimplifiableIfStatement

                Pergunta objPergunta = dataSet.get(getId());

                Snackbar.make(view, objPergunta.getPergunta() + "\nSelecionada", Snackbar.LENGTH_LONG)
                        .setAction("Listar Materias: ", null).show();

                idPergunta = objPergunta.getIdpk();
                imgPergunta = objPergunta.getImagem();
                txtPergunta = objPergunta.getPergunta();

                FormRespostaFragment formRespostaFragment = new FormRespostaFragment();

                formRespostaFragment.idPergunta = objPergunta.getIdpk();
                formRespostaFragment.imgPergunta = objPergunta.getImagem();
                formRespostaFragment.txtPergunta = objPergunta.getPergunta();

                Bundle bundle = new Bundle();
                bundle.putInt("idPergunta", idPergunta);
                bundle.putString("imgPergunta", imgPergunta);
                bundle.putString("txtPergunta", txtPergunta);
                formRespostaFragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.content_fragment, formRespostaFragment);
                transaction.addToBackStack(null);

                transaction.commit();


                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //noinspection SimplifiableIfStatement

                getActivity().setTitle("Pergunta/Respostas");

                Pergunta objPergunta = dataSet.get(position);

                Snackbar.make(view, objPergunta.getPergunta() + "\nSelecionada", Snackbar.LENGTH_LONG)
                        .setAction("Listar Materias: ", null).show();

                idPergunta = objPergunta.getIdpk();
                imgPergunta = objPergunta.getImagem();
                txtPergunta = objPergunta.getPergunta();

                respostaFragment.idPergunta = objPergunta.getIdpk();
                respostaFragment.imgPergunta = objPergunta.getImagem();
                respostaFragment.txtPergunta = objPergunta.getPergunta();

                Bundle bundle = new Bundle();
                bundle.putInt("idPergunta", idPergunta);
                bundle.putString("imgPergunta", imgPergunta);
                bundle.putString("txtPergunta", txtPergunta);
                respostaFragment.setArguments(bundle);

                abreRespostas();
            }

        });
        return view;
    }

    public void abreRespostas() {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.content_fragment, respostaFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskPergunta extends AsyncTask<String, String, String> {

        ProgressDialog progressDiaglog = new ProgressDialog(getContext());

        HttpURLConnection conn;
        URL url = null;
        Uri.Builder builder;

        public AsyncTaskPergunta() {

            this.builder = new Uri.Builder();

            // "app" nome do dispositivo autrizado
            // "MediaEscolar" key da aplicação permitida
            // A key deve ser criptografada para manter a segurança

            builder.appendQueryParameter("app", "VerificaDuvida");

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

                url = new URL(Util.URL_WEB_SERVICE + "APISincronizarPergunta.php");

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

                    InputStream inputPergunta = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputPergunta));
                    StringBuilder result = new StringBuilder();

                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (result.toString());
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
        protected void onPostExecute(String result) {
            try {

                JSONArray jArrayPergunta = new JSONArray(result);

                if (jArrayPergunta.length() != 0) {
                    // Salvar dados recebidos no BD SQLite

                    perguntaCtrl.deletarTabela(PerguntaDataModel.getTABELA());
                    perguntaCtrl.criarTabela(PerguntaDataModel.criarTabela());

                    for (int i = 0; i < jArrayPergunta.length(); i++) {

                        JSONObject jsonPergunta = jArrayPergunta.getJSONObject(i);
                        Pergunta objPergunta = new Pergunta();

                        objPergunta.setIdpk((jsonPergunta.getInt(PerguntaDataModel.getId())));
                        objPergunta.setImagem((jsonPergunta.getString(PerguntaDataModel.getImagem())));
                        objPergunta.setPergunta((jsonPergunta.getString(PerguntaDataModel.getPergunta())));
                        objPergunta.setNivel((jsonPergunta.getString(PerguntaDataModel.getNivel())));
                        objPergunta.setFk_materia((jsonPergunta.getInt(PerguntaDataModel.getFk_materia())));
                        objPergunta.setFk_usuario((jsonPergunta.getInt(PerguntaDataModel.getFk_usuario())));
                        objPergunta.setData((jsonPergunta.getString(PerguntaDataModel.getData())));

                        perguntaCtrl.salvar(objPergunta);

                    }
                } else {
                    Util.showMensagem(context, "Nenhum registro encontrado...");
                }

            } catch (Exception e) {

                Log.e("WEBService", "Erro JSONException - " + e.getMessage());

            } finally {

                if (progressDiaglog != null && progressDiaglog.isShowing()) {
                    progressDiaglog.dismiss();

                    dataSet = perguntaCtrl.getAllPerguntas();

                    final PerguntaListAdapter adapter =
                            new PerguntaListAdapter(dataSet, getContext());
                    listView.setAdapter(adapter);
                }
            }
        }
    }

}
