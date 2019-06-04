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
import com.topartes.verificaduvida.adapter.CategoriaListAdapter;
import com.topartes.verificaduvida.controller.CategoriaCtrl;
import com.topartes.verificaduvida.datamodel.CategoriaDataModel;
import com.topartes.verificaduvida.model.Categoria;
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

public class FormCategoriaFragment extends Fragment {

    public int idCategoria;
    ArrayList<Categoria> dataSet;
    ListView listView;
    Button btnAtualCategoria;
    CategoriaCtrl categoriaCtrl;
    Context context;
    View view;

    public FormCategoriaFragment() {
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
        view = inflater.inflate(R.layout.fragment_categoria, container, false);

        categoriaCtrl = new CategoriaCtrl(getContext());
        listView = view.findViewById(R.id.listViewCategoria);
        btnAtualCategoria = view.findViewById(R.id.btnAtualCategoria);

        dataSet = categoriaCtrl.getAllCategorias();
        final CategoriaListAdapter adapter =
                new CategoriaListAdapter(dataSet, getContext());

        listView.setAdapter(adapter);
        btnAtualCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AsyncTaskCategoria taskCategoria = new AsyncTaskCategoria();
                taskCategoria.execute();
                dataSet = categoriaCtrl.getAllCategorias();
                adapter.atualizarCategorias(dataSet);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //noinspection SimplifiableIfStatement


                getActivity().setTitle("Selecione a Matéria");

                Categoria objCategoria = dataSet.get(position);

                objCategoria.setId(objCategoria.getId());
                objCategoria.setIdpk(objCategoria.getIdpk());
                objCategoria.setCategoria(objCategoria.getCategoria());
                objCategoria.setFk_usuario(objCategoria.getFk_usuario());
                objCategoria.setData(objCategoria.getData());

                Snackbar.make(view, objCategoria.getCategoria() + "\nSelecionada", Snackbar.LENGTH_LONG)
                        .setAction("Listar Materias: ", null).show();

                idCategoria = objCategoria.getIdpk();
                FormMateriaFragment formMateriaFragment = new FormMateriaFragment();
                formMateriaFragment.idCategoria = objCategoria.getIdpk();

                Bundle bundle = new Bundle();
                bundle.putInt("idCategoria", idCategoria);
                formMateriaFragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.content_fragment, formMateriaFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskCategoria extends AsyncTask<String, String, String> {

        ProgressDialog progressDiaglog = new ProgressDialog(getContext());

        HttpURLConnection conn;
        URL url = null;
        Uri.Builder builder;

        public AsyncTaskCategoria() {

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

                url = new URL(Util.URL_WEB_SERVICE + "APISincronizarCategoria.php");

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

                    InputStream inputCategoria = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputCategoria));
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

                JSONArray jArrayCategoria = new JSONArray(result);

                if (jArrayCategoria.length() != 0) {
                    // Salvar dados recebidos no BD SQLite

                    categoriaCtrl.deletarTabela(CategoriaDataModel.getTABELA());
                    categoriaCtrl.criarTabela(CategoriaDataModel.criarTabela());

                    for (int i = 0; i < jArrayCategoria.length(); i++) {

                        JSONObject jsonCategoria = jArrayCategoria.getJSONObject(i);
                        Categoria objCategoria = new Categoria();

                        objCategoria.setId((jsonCategoria.getInt(CategoriaDataModel.getId())));
                        objCategoria.setIdpk((jsonCategoria.getInt(CategoriaDataModel.getId())));
                        objCategoria.setCategoria((jsonCategoria.getString(CategoriaDataModel.getCategoria())));
                        objCategoria.setFk_usuario((jsonCategoria.getInt(CategoriaDataModel.getFk_usuario())));
                        objCategoria.setData((jsonCategoria.getString(CategoriaDataModel.getData())));

                        categoriaCtrl.salvar(objCategoria);

                    }
                } else {
                    Util.showMensagem(context, "Nenhum registro encontrado...");
                }

            } catch (Exception e) {

                Log.e("WEBService", "Erro JSONException - " + e.getMessage());

            } finally {

                if (progressDiaglog != null && progressDiaglog.isShowing()) {
                    progressDiaglog.dismiss();

                    dataSet = categoriaCtrl.getAllCategorias();
                    final CategoriaListAdapter adapter =
                            new CategoriaListAdapter(dataSet, getContext());

                    listView.setAdapter(adapter);
                }
            }
        }

    }

}
