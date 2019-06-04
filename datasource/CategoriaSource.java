package com.topartes.verificaduvida.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.topartes.verificaduvida.datamodel.CategoriaDataModel;
import com.topartes.verificaduvida.model.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaSource extends SQLiteOpenHelper {

    private static final String DB_NAME = "u793605722_tig5.sqlite";
    private static final int DB_VERSION = 1;

    private Cursor cursor;
    private SQLiteDatabase db;

    public CategoriaSource(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {

            db.execSQL(CategoriaDataModel.criarTabela());

        } catch (Exception e) {

            Log.e("CATEGORIA", "DB---> ERRO: Criar Tabela" + e.getMessage());

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        newVersion = oldVersion++;
    }

    protected boolean insert(String tabela, ContentValues dados) {
        boolean sucesso = true;
        try {
            sucesso = db.insert(tabela, null, dados) > 0;
        } catch (Exception e) {
            sucesso = false;
            Log.e("Insert", "ERRO ------> BD" + e.getMessage());
        }
        return sucesso;
    }

    protected List<Categoria> getAllListCategorias() {
        Categoria objCategoria;
        List<Categoria> listaCategorias = new ArrayList<>();

        String sql = "SELECT * FROM " + CategoriaDataModel.getTABELA();
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                objCategoria = new Categoria();

                objCategoria.setId(cursor.getInt(cursor.getColumnIndex(CategoriaDataModel.getId())));
                objCategoria.setIdpk(cursor.getInt(cursor.getColumnIndex(CategoriaDataModel.getIdpk())));
                objCategoria.setCategoria(cursor.getString(cursor.getColumnIndex(CategoriaDataModel.getCategoria())));
                objCategoria.setFk_usuario(cursor.getInt(cursor.getColumnIndex(CategoriaDataModel.getFk_usuario())));
                objCategoria.setData(cursor.getString(cursor.getColumnIndex(CategoriaDataModel.getData())));

                listaCategorias.add(objCategoria);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaCategorias;
    }

    public ArrayList<Categoria> getAllCategorias() {

        ArrayList<Categoria> listaCategorias = new ArrayList<>();

        String sql = "SELECT * FROM " + CategoriaDataModel.getTABELA();
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            Categoria objCategoria;

            do {
                objCategoria = new Categoria();

                objCategoria.setId(cursor.getInt(cursor.getColumnIndex(CategoriaDataModel.getId())));
                objCategoria.setIdpk(cursor.getInt(cursor.getColumnIndex(CategoriaDataModel.getIdpk())));
                objCategoria.setCategoria(cursor.getString(cursor.getColumnIndex(CategoriaDataModel.getCategoria())));
                objCategoria.setFk_usuario(cursor.getInt(cursor.getColumnIndex(CategoriaDataModel.getFk_usuario())));
                objCategoria.setData(cursor.getString(cursor.getColumnIndex(CategoriaDataModel.getData())));

                listaCategorias.add(objCategoria);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaCategorias;
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

