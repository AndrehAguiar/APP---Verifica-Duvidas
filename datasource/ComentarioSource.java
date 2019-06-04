package com.topartes.verificaduvida.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.topartes.verificaduvida.datamodel.ComentarioDataModel;
import com.topartes.verificaduvida.model.Comentario;

import java.util.ArrayList;
import java.util.List;

public class ComentarioSource extends SQLiteOpenHelper {

    private static final String DB_NAME = "u793605722_tig5.sqlite";
    private static final int DB_VERSION = 1;
    public int idResposta, idComentario;
    private SQLiteDatabase db;
    private Cursor cursor;


    public ComentarioSource(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            db.execSQL(ComentarioDataModel.criarTabela());

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

    protected List<Comentario> getAllListComentarios() {
        Comentario objComentario;

        List<Comentario> listaComentarios = new ArrayList<>();

        String sql = "SELECT * FROM " + ComentarioDataModel.getTABELA();
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                objComentario = new Comentario();

                objComentario.setId(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getId())));
                objComentario.setIdpk(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getIdpk())));
                objComentario.setImagem(cursor.getString(cursor.getColumnIndex(ComentarioDataModel.getImagem())));
                objComentario.setComentario(cursor.getString(cursor.getColumnIndex(ComentarioDataModel.getComentario())));
                objComentario.setFk_resposta(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getFk_resposta())));
                objComentario.setFk_usuario(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getFk_usuario())));
                objComentario.setData(cursor.getString(cursor.getColumnIndex(ComentarioDataModel.getData())));

                listaComentarios.add(objComentario);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaComentarios;
    }

    public ArrayList<Comentario> getAllComentarios() {
        Comentario objComentario;

        ArrayList<Comentario> listaComentarios = new ArrayList<>();

        String sql = "SELECT * FROM " + ComentarioDataModel.getTABELA();
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                objComentario = new Comentario();

                objComentario.setId(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getId())));
                objComentario.setIdpk(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getIdpk())));
                objComentario.setImagem(cursor.getString(cursor.getColumnIndex(ComentarioDataModel.getImagem())));
                objComentario.setComentario(cursor.getString(cursor.getColumnIndex(ComentarioDataModel.getComentario())));
                objComentario.setFk_resposta(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getFk_resposta())));
                objComentario.setFk_usuario(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getFk_usuario())));
                objComentario.setData(cursor.getString(cursor.getColumnIndex(ComentarioDataModel.getData())));

                listaComentarios.add(objComentario);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaComentarios;
    }

    public ArrayList<Comentario> getComentariosResposta() {

        this.idResposta = idResposta;

        Comentario objComentario;

        ArrayList<Comentario> listaComentarios = new ArrayList<>();
        String sql = "SELECT * FROM " + ComentarioDataModel.getTABELA() + " WHERE " + ComentarioDataModel.getFk_resposta() + " = " + idResposta;
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                objComentario = new Comentario();

                objComentario.setId(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getId())));
                objComentario.setIdpk(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getIdpk())));
                objComentario.setImagem(cursor.getString(cursor.getColumnIndex(ComentarioDataModel.getImagem())));
                objComentario.setComentario(cursor.getString(cursor.getColumnIndex(ComentarioDataModel.getComentario())));
                objComentario.setFk_resposta(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getFk_resposta())));
                objComentario.setFk_usuario(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getFk_usuario())));
                objComentario.setData(cursor.getString(cursor.getColumnIndex(ComentarioDataModel.getData())));

                listaComentarios.add(objComentario);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaComentarios;
    }

    public Comentario getComentario() {

        Comentario objComentario = new Comentario();

        String sql = "SELECT * FROM " + ComentarioDataModel.getTABELA() + " WHERE " + ComentarioDataModel.getId() + " = " + idComentario;
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                objComentario.setId(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getId())));
                objComentario.setIdpk(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getIdpk())));
                objComentario.setImagem(cursor.getString(cursor.getColumnIndex(ComentarioDataModel.getImagem())));
                objComentario.setComentario(cursor.getString(cursor.getColumnIndex(ComentarioDataModel.getComentario())));
                objComentario.setFk_resposta(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getFk_resposta())));
                objComentario.setFk_usuario(cursor.getInt(cursor.getColumnIndex(ComentarioDataModel.getFk_usuario())));
                objComentario.setData(cursor.getString(cursor.getColumnIndex(ComentarioDataModel.getData())));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return objComentario;
    }

    public long getQtdComentario() {

        String sql = "SELECT * FROM " + ComentarioDataModel.getTABELA() + " WHERE " + ComentarioDataModel.getFk_resposta() + " = " + idResposta;

        Cursor cursor = db.rawQuery(sql, null);
        long somaComentarios = cursor.getCount();
        cursor.close();
        return somaComentarios;
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
