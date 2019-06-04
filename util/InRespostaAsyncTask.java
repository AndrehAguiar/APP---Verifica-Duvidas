package com.topartes.verificaduvida.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.topartes.verificaduvida.datamodel.RespostaDataModel;
import com.topartes.verificaduvida.model.Resposta;

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

public class InRespostaAsyncTask extends AsyncTask<String, String, String> {

    ProgressDialog progressDialog;

    HttpURLConnection conn;
    URL url = null;
    Uri.Builder builder;

    Context context;


    public InRespostaAsyncTask(Resposta obj, Context context) {
        this.builder = new Uri.Builder();

        this.context = context;

        builder.appendQueryParameter("app", "VerificaDuvida");
        builder.appendQueryParameter(RespostaDataModel.getImagem(), obj.getImagem());
        builder.appendQueryParameter(RespostaDataModel.getResposta(), String.valueOf(obj.getResposta()));
        builder.appendQueryParameter(RespostaDataModel.getFk_pergunta(), String.valueOf(obj.getFk_pergunta()));
        builder.appendQueryParameter(RespostaDataModel.getFk_usuario(), String.valueOf(obj.getFk_usuario()));

    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);

        progressDialog.setMessage("Incluindo, por favor espere...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        try {

            url = new URL(Util.URL_WEB_SERVICE + "APIIncluirResposta.php");

        } catch (MalformedURLException e) {

            Log.e("WebService", "MalformedURLException - " + e.getMessage());

        } catch (Exception e) {

            Log.e("WebService", "Exception - " + e.getMessage());

        }

        try {

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(Util.READ_TIME_OUT);
            conn.setConnectTimeout(Util.CONECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("charset", "utf-8");

            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.connect();

            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (IOException erro) {

            Log.e("WebService", "IOException - " + erro.getMessage());

        }

        try {

            int response_code = conn.getResponseCode();

            if (response_code == HttpURLConnection.HTTP_OK) {

                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                return (result.toString());

            } else {
                return ("Erro de conexão");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        } finally {
            conn.disconnect();
        }

    }

    @Override
    protected void onPostExecute(String result) {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();

        }
    }

}
