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
import android.widget.ListView;

import com.topartes.verificaduvida.R;
import com.topartes.verificaduvida.adapter.MateriaListAdapter;
import com.topartes.verificaduvida.controller.MateriaCtrl;
import com.topartes.verificaduvida.datamodel.MateriaDataModel;
import com.topartes.verificaduvida.model.Materia;
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

public class MateriaFragment extends Fragment {

    public int idCategoria;
    public int idMateria;
    ArrayList<Materia> dataSet;
    ListView listView;
    Button btnAtualMateria;
    Context context;
    MateriaCtrl materiaCtrl;
    Bundle bundle;
    View view;

    public MateriaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_materia, container, false);

        materiaCtrl = new MateriaCtrl(getContext());

        listView = view.findViewById(R.id.listViewMateria);
        btnAtualMateria = view.findViewById(R.id.btnAtualMateria);

        bundle = getArguments();
        materiaCtrl.idCategoria = idCategoria;

        dataSet = materiaCtrl.getMateriasCategoria();

        final MateriaListAdapter adapter =
                new MateriaListAdapter(dataSet, getContext());
        listView.setAdapter(adapter);

        btnAtualMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                MateriaFragment.AsyncTaskMateria taskMateria = new MateriaFragment.AsyncTaskMateria();
                taskMateria.execute();
                dataSet = materiaCtrl.getMateriasCategoria();
                adapter.atualizarMaterias(dataSet);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Materia objMateria = dataSet.get(position);

                getActivity().setTitle("Perguntas/" + objMateria.getMateria());

                objMateria.setId(objMateria.getId());
                objMateria.setIdpk(objMateria.getIdpk());
                objMateria.setMateria(objMateria.getMateria());
                objMateria.setFk_categoria(objMateria.getFk_categoria());
                objMateria.setFk_usuario(objMateria.getFk_usuario());
                objMateria.setData(objMateria.getData());

                Snackbar.make(view, objMateria.getMateria() + "\nSelecionada", Snackbar.LENGTH_LONG)
                        .setAction("Listar Materias: ", null).show();

                idMateria = objMateria.getIdpk();
                PerguntaFragment perguntaFragment = new PerguntaFragment();
                perguntaFragment.idMateria = objMateria.getIdpk();

                Bundle bundle = new Bundle();
                bundle.putInt("idMateria", idMateria);
                perguntaFragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.content_fragment, perguntaFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskMateria extends AsyncTask<String, String, String> {

        ProgressDialog progressDiaglog = new ProgressDialog(getContext());

        HttpURLConnection conn;
        URL url = null;

        Uri.Builder builder;

        public AsyncTaskMateria() {

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

                url = new URL(Util.URL_WEB_SERVICE + "APISincronizarMateria.php");

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

                    InputStream inputMateria = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputMateria));
                    StringBuilder resultMateria = new StringBuilder();

                    String line;

                    Log.i("MATERIA MySQL", "[" + resultMateria + "]");

                    while ((line = reader.readLine()) != null)
                        resultMateria.append(line);
                    Log.e("response", "" + resultMateria);
                    return (resultMateria.toString());
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
        protected void onPostExecute(String resultMateria) {

            try {
                JSONArray jArrayMateria = new JSONArray(resultMateria);

                if (jArrayMateria.length() != 0) {

                    materiaCtrl.deletarTabela(MateriaDataModel.getTABELA());
                    materiaCtrl.criarTabela(MateriaDataModel.criarTabela());

                    // Salvar dados recebidos no BD SQLite

                    for (int u = 0; u < jArrayMateria.length(); u++) {

                        JSONObject jsonMateria = jArrayMateria.getJSONObject(u);
                        Materia objMateria = new Materia();

                        objMateria.setId((jsonMateria.getInt(MateriaDataModel.getId())));
                        objMateria.setIdpk((jsonMateria.getInt(MateriaDataModel.getId())));
                        objMateria.setMateria((jsonMateria.getString(MateriaDataModel.getMateria())));
                        objMateria.setFk_categoria((jsonMateria.getInt(MateriaDataModel.getFk_categoria())));
                        objMateria.setFk_usuario((jsonMateria.getInt(MateriaDataModel.getFk_usuario())));
                        objMateria.setData((jsonMateria.getString(MateriaDataModel.getData())));

                        materiaCtrl.salvar(objMateria);

                    }
                } else {
                    Util.showMensagem(context, "Nenhum registro encontrado...");
                }

            } catch (Exception e) {

                Log.e("WEBService", "Erro JSONException - " + e.getMessage());

            } finally {

                if (progressDiaglog != null && progressDiaglog.isShowing()) {
                    progressDiaglog.dismiss();

                    dataSet = materiaCtrl.getMateriasCategoria();

                    final MateriaListAdapter adapter =
                            new MateriaListAdapter(dataSet, getContext());
                    listView.setAdapter(adapter);

                }

            }
        }

    }

}
