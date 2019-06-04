package com.topartes.verificaduvida.datasource;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataSource extends SQLiteOpenHelper {

    private static final String DB_NAME = "u793605722_tig5.sqlite";
    private static final int DB_VERSION = 1;
    Context context;
    String tabela;
    private Cursor cursor;
    private SQLiteDatabase db;

    public DataSource(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean deletar(String tabela, int id) {
        boolean sucesso = true;
        sucesso = db.delete(tabela, "id=?", new String[]{Integer.toString(id)}) > 0;

        return sucesso;
    }
}
