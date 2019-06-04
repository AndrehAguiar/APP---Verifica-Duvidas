package com.topartes.verificaduvida.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.topartes.verificaduvida.datamodel.RespostaDataModel;
import com.topartes.verificaduvida.model.Resposta;

import java.util.ArrayList;
import java.util.List;

public class RespostaSource extends SQLiteOpenHelper {

    private static final String DB_NAME = "u793605722_tig5.sqlite";
    private static final int DB_VERSION = 1;
    public int idPergunta, idResposta;
    private SQLiteDatabase db;
    private Cursor cursor;


    public RespostaSource(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(RespostaDataModel.criarTabela());
        } catch (Exception e) {
            Log.e("RESPOSTA", "DB---> ERRO: Criar Tabela" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    protected boolean insert(String tabela, ContentValues dados) {
        boolean sucesso = true;
        try {
            sucesso = db.insert(tabela, null, dados) > 0;
        } catch (Exception e) {
            sucesso = false;
            Log.e("Insert", "ERRO ------> BD Resposta" + e.getMessage());
        }
        return sucesso;
    }

    protected List<Resposta> getAllListRespostas() {
        Resposta objResposta;

        List<Resposta> listaRespostas = new ArrayList<>();

        String sql = "SELECT * FROM " + RespostaDataModel.getTABELA();
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                objResposta = new Resposta();

                objResposta.setId(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getId())));
                objResposta.setIdpk(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getIdpk())));
                objResposta.setResposta(cursor.getString(cursor.getColumnIndex(RespostaDataModel.getResposta())));
                objResposta.setFk_pergunta(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getFk_pergunta())));
                objResposta.setFk_usuario(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getFk_usuario())));
                objResposta.setData(cursor.getString(cursor.getColumnIndex(RespostaDataModel.getData())));

                listaRespostas.add(objResposta);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaRespostas;
    }

    public ArrayList<Resposta> getAllRespostas() {
        Resposta objResposta;

        ArrayList<Resposta> listaRespostas = new ArrayList<>();

        String sql = "SELECT * FROM " + RespostaDataModel.getTABELA();
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                objResposta = new Resposta();

                objResposta.setId(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getId())));
                objResposta.setIdpk(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getIdpk())));
                objResposta.setImagem(cursor.getString(cursor.getColumnIndex(RespostaDataModel.getImagem())));
                objResposta.setResposta(cursor.getString(cursor.getColumnIndex(RespostaDataModel.getResposta())));
                objResposta.setFk_pergunta(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getFk_pergunta())));
                objResposta.setFk_usuario(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getFk_usuario())));
                objResposta.setData(cursor.getString(cursor.getColumnIndex(RespostaDataModel.getData())));

                listaRespostas.add(objResposta);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaRespostas;
    }

    public ArrayList<Resposta> getRespostasPergunta() {
        ArrayList<Resposta> listaRespostas = new ArrayList<>();

        String sql = "SELECT * FROM " + RespostaDataModel.getTABELA() + " WHERE " + RespostaDataModel.getFk_pergunta() + " = " + idPergunta;
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            Resposta objResposta;
            do {
                objResposta = new Resposta();

                objResposta.setId(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getId())));
                objResposta.setIdpk(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getIdpk())));
                objResposta.setImagem(cursor.getString(cursor.getColumnIndex(RespostaDataModel.getImagem())));
                objResposta.setResposta(cursor.getString(cursor.getColumnIndex(RespostaDataModel.getResposta())));
                objResposta.setFk_pergunta(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getFk_pergunta())));
                objResposta.setFk_usuario(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getFk_usuario())));
                objResposta.setData(cursor.getString(cursor.getColumnIndex(RespostaDataModel.getData())));

                listaRespostas.add(objResposta);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaRespostas;
    }

    public long getQtdResposta() {

        String sql = "SELECT * FROM " + RespostaDataModel.getTABELA() + " WHERE " + RespostaDataModel.getFk_pergunta() + " = " + idPergunta;

        Cursor cursor = db.rawQuery(sql, null);
        long somaRespostas = cursor.getCount();
        cursor.close();
        return somaRespostas;
    }

    public Resposta getResposta() {
        Resposta objResposta = new Resposta();

        String sql = "SELECT * FROM " + RespostaDataModel.getTABELA() + " WHERE " + RespostaDataModel.getId() + " = " + idResposta;
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                objResposta.setId(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getId())));
                objResposta.setIdpk(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getIdpk())));
                objResposta.setImagem(cursor.getString(cursor.getColumnIndex(RespostaDataModel.getImagem())));
                objResposta.setResposta(cursor.getString(cursor.getColumnIndex(RespostaDataModel.getResposta())));
                objResposta.setFk_pergunta(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getFk_pergunta())));
                objResposta.setFk_usuario(cursor.getInt(cursor.getColumnIndex(RespostaDataModel.getFk_usuario())));
                objResposta.setData(cursor.getString(cursor.getColumnIndex(RespostaDataModel.getData())));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return objResposta;
    }

    public void criarTabela(String queryCriarTabela) {

        try {
            db.execSQL(queryCriarTabela);

        } catch (SQLiteCantOpenDatabaseException e) {
            Log.e("ERRO Criar Tabela: ", "SQL " + e.getMessage());
        }
    }

    public void deletarTabela(String tabela) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + tabela);
        } catch (Exception e) {
            Log.e("ERRO Deletar Tabela: ", "SQL " + e.getMessage());
        }
    }
}
