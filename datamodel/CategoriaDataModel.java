package com.topartes.verificaduvida.datamodel;

public class CategoriaDataModel {

    private static final String TABELA = "categoria";
    private static final String id = "id";
    private static final String idpk = "idpk";
    private static final String categoria = "categoria";
    private static final String fk_usuario = "fk_usuario";
    private static final String data = "data";

    private static String queryCriarTabela = "";

    public static String criarTabela() {

        queryCriarTabela = "CREATE TABLE IF NOT EXISTS " + TABELA;
        queryCriarTabela += "(";
        queryCriarTabela += id + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela += idpk + " INTEGER, ";
        queryCriarTabela += categoria + " TEXT, ";
        queryCriarTabela += fk_usuario + " INTEGER, ";
        queryCriarTabela += data + " TEXT ";
        queryCriarTabela += ")";

        return queryCriarTabela;
    }

    public static String getTABELA() {
        return TABELA;
    }

    public static String getId() {
        return id;
    }

    public static String getIdpk() {
        return idpk;
    }

    public static String getCategoria() {
        return categoria;
    }

    public static String getFk_usuario() {
        return fk_usuario;
    }

    public static String getData() {
        return data;
    }

    public static String getQueryCriarTabela() {
        return queryCriarTabela;
    }

    public static void setQueryCriarTabela(String queryCriarTabela) {
        CategoriaDataModel.queryCriarTabela = queryCriarTabela;
    }

}