package com.topartes.verificaduvida.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.topartes.verificaduvida.datamodel.MateriaDataModel;
import com.topartes.verificaduvida.model.Materia;

import java.util.ArrayList;
import java.util.List;

public class MateriaSource extends SQLiteOpenHelper {

    private static final String DB_NAME = "u793605722_tig5.sqlite";
    private static final int DB_VERSION = 1;
    public int idCategoria;
    public int idMateria;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Context context;


    public MateriaSource(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            db.execSQL(MateriaDataModel.criarTabela());

        } catch (Exception e) {

            Log.e("MATERIA", "DB---> ERRO: Criar Tabela" + e.getMessage());

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
            Log.e("Insert", "ERRO ------> BD" + e.getMessage());
        }
        return sucesso;
    }

    protected List<Materia> getAllListMaterias() {
        Materia objMateria;

        List<Materia> listaMaterias = new ArrayList<>();

        String sql = "SELECT * FROM " + MateriaDataModel.getTABELA();
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                objMateria = new Materia();

                objMateria.setId(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getId())));
                objMateria.setIdpk(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getIdpk())));
                objMateria.setMateria(cursor.getString(cursor.getColumnIndex(MateriaDataModel.getMateria())));
                objMateria.setFk_categoria(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getFk_categoria())));
                objMateria.setFk_usuario(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getFk_usuario())));
                objMateria.setData(cursor.getString(cursor.getColumnIndex(MateriaDataModel.getData())));

                listaMaterias.add(objMateria);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaMaterias;
    }

    public ArrayList<Materia> getAllMaterias() {
        Materia objMateria;

        ArrayList<Materia> listaMaterias = new ArrayList<>();

        String sql = "SELECT * FROM " + MateriaDataModel.getTABELA();
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                objMateria = new Materia();

                objMateria.setId(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getId())));
                objMateria.setIdpk(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getIdpk())));
                objMateria.setMateria(cursor.getString(cursor.getColumnIndex(MateriaDataModel.getMateria())));
                objMateria.setFk_categoria(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getFk_categoria())));
                objMateria.setFk_usuario(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getFk_usuario())));
                objMateria.setData(cursor.getString(cursor.getColumnIndex(MateriaDataModel.getData())));

                listaMaterias.add(objMateria);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaMaterias;
    }

    public ArrayList<Materia> getMateriasCategoria() {

        Materia objMateria;

        ArrayList<Materia> listaMaterias = new ArrayList<>();
        String sql = "SELECT * FROM " + MateriaDataModel.getTABELA() + " WHERE " + MateriaDataModel.getFk_categoria() + " = " + idCategoria;
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                objMateria = new Materia();

                objMateria.setId(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getId())));
                objMateria.setIdpk(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getIdpk())));
                objMateria.setMateria(cursor.getString(cursor.getColumnIndex(MateriaDataModel.getMateria())));
                objMateria.setFk_categoria(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getFk_categoria())));
                objMateria.setFk_usuario(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getFk_usuario())));
                objMateria.setData(cursor.getString(cursor.getColumnIndex(MateriaDataModel.getData())));

                listaMaterias.add(objMateria);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaMaterias;
    }

    public long getQtdMateria() {

        String sql = "SELECT * FROM " + MateriaDataModel.getTABELA() + " WHERE " + MateriaDataModel.getFk_categoria() + " = " + idCategoria;

        Cursor cursor = db.rawQuery(sql, null);
        long somaMaterias = cursor.getCount();
        cursor.close();
        return somaMaterias;
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

    public Materia getMateriaPergunta() {

        Materia objMateria = null;

        String sql = "SELECT * FROM " + MateriaDataModel.getTABELA() + " WHERE " + MateriaDataModel.getIdpk() + " = " + idMateria;
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                objMateria = new Materia();

                objMateria.setId(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getId())));
                objMateria.setIdpk(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getIdpk())));
                objMateria.setMateria(cursor.getString(cursor.getColumnIndex(MateriaDataModel.getMateria())));
                objMateria.setFk_categoria(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getFk_categoria())));
                objMateria.setFk_usuario(cursor.getInt(cursor.getColumnIndex(MateriaDataModel.getFk_usuario())));
                objMateria.setData(cursor.getString(cursor.getColumnIndex(MateriaDataModel.getData())));

            } while (cursor.moveToNext());
        }
        cursor.close();

        return objMateria;
    }
}
