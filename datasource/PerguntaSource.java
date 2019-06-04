package com.topartes.verificaduvida.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.topartes.verificaduvida.datamodel.PerguntaDataModel;
import com.topartes.verificaduvida.model.Pergunta;

import java.util.ArrayList;
import java.util.List;

public class PerguntaSource extends SQLiteOpenHelper {

    private static final String DB_NAME = "u793605722_tig5.sqlite";
    private static int DB_Version = 1;
    public int idMateria, idPergunta;
    private Cursor cursor;
    private SQLiteDatabase db;

    public PerguntaSource(Context context) {
        super(context, DB_NAME, null, DB_Version);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(PerguntaDataModel.getQueryCriarTabela());
        } catch (Exception e) {
            Log.e("PERGUNTA", "DB -----------> ERRO: Criar Tabela" + e.getMessage());
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
            Log.e("INSERT ", "ERRO _----------> BD" + e.getMessage());

        }
        return sucesso;
    }

    protected List<Pergunta> getAllListPergunta() {
        Pergunta objPergunta;
        List<Pergunta> listaPerguntas = new ArrayList<>();

        String sql = "SELECT * FROM " + PerguntaDataModel.getTABELA();
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                objPergunta = new Pergunta();
                objPergunta.setId(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getId())));
                objPergunta.setIdpk(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getIdpk())));
                objPergunta.setImagem(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getImagem())));
                objPergunta.setPergunta(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getPergunta())));
                objPergunta.setNivel(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getNivel())));
                objPergunta.setFk_materia(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getFk_materia())));
                objPergunta.setFk_usuario(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getFk_usuario())));
                objPergunta.setData(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getData())));

                listaPerguntas.add(objPergunta);
            } while (cursor.moveToFirst());
        }
        cursor.close();
        return listaPerguntas;
    }

    public ArrayList<Pergunta> getAllPerguntas() {
        ArrayList<Pergunta> listaPerguntas = new ArrayList<>();

        String sql = "SELECT * FROM " + PerguntaDataModel.getTABELA() + " ORDER BY id DESC";
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            Pergunta objPergunta;
            do {
                objPergunta = new Pergunta();
                objPergunta.setId(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getId())));
                objPergunta.setIdpk(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getIdpk())));
                objPergunta.setImagem(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getImagem())));
                objPergunta.setPergunta(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getPergunta())));
                objPergunta.setNivel(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getNivel())));
                objPergunta.setFk_materia(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getFk_materia())));
                objPergunta.setFk_usuario(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getFk_usuario())));
                objPergunta.setData(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getData())));

                listaPerguntas.add(objPergunta);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaPerguntas;
    }

    public ArrayList<Pergunta> getMateriaPerguntas() {
        ArrayList<Pergunta> listaPerguntas = new ArrayList<>();

        String sql = "SELECT * FROM " + PerguntaDataModel.getTABELA() + " WHERE " + PerguntaDataModel.getFk_materia() + " = " + idMateria + " ORDER BY id DESC";
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            Pergunta objPergunta;
            do {
                objPergunta = new Pergunta();
                objPergunta.setId(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getId())));
                objPergunta.setIdpk(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getIdpk())));
                objPergunta.setImagem(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getImagem())));
                objPergunta.setPergunta(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getPergunta())));
                objPergunta.setNivel(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getNivel())));
                objPergunta.setFk_materia(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getFk_materia())));
                objPergunta.setFk_usuario(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getFk_usuario())));
                objPergunta.setData(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getData())));

                listaPerguntas.add(objPergunta);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaPerguntas;
    }

    public Pergunta getPergunta() {
        Pergunta objPergunta = new Pergunta();

        String sql = "SELECT * FROM " + PerguntaDataModel.getTABELA() + " WHERE " + PerguntaDataModel.getId() + " = " + idPergunta;
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            do {

                objPergunta.setId(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getId())));
                objPergunta.setIdpk(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getIdpk())));
                objPergunta.setImagem(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getImagem())));
                objPergunta.setPergunta(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getPergunta())));
                objPergunta.setNivel(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getNivel())));
                objPergunta.setFk_materia(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getFk_materia())));
                objPergunta.setFk_usuario(cursor.getInt(cursor.getColumnIndex(PerguntaDataModel.getFk_usuario())));
                objPergunta.setData(cursor.getString(cursor.getColumnIndex(PerguntaDataModel.getData())));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return objPergunta;
    }

    public long getQtdPergunta() {

        String sql = "SELECT * FROM " + PerguntaDataModel.getTABELA() + " WHERE " + PerguntaDataModel.getFk_materia() + " = " + idMateria;

        Cursor cursor = db.rawQuery(sql, null);
        long somaPerguntas = cursor.getCount();
        cursor.close();
        return somaPerguntas;
    }

    public void criarTabela(String queryCriarTabela) {
        try {
            db.execSQL(queryCriarTabela);
        } catch (Exception e) {
            Log.e("ERRO: Criar tabela: ", "SQL " + e.getMessage());
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
