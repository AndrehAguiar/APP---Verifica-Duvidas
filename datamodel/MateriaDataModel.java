package com.topartes.verificaduvida.datamodel;

public class MateriaDataModel {

    private static final String TABELA = "materia";
    private static final String id = "id";
    private static final String idpk = "idpk";
    private static final String materia = "materia";
    private static final String fk_categoria = "fk_categoria";
    private static final String fk_usuario = "fk_usuario";
    private static final String data = "data";

    private static String queryCriarTabela = "";

    public static String criarTabela() {

        queryCriarTabela = "CREATE TABLE IF NOT EXISTS " + TABELA;
        queryCriarTabela += "(";
        queryCriarTabela += id + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela += idpk + " INTEGER, ";
        queryCriarTabela += materia + " TEXT, ";
        queryCriarTabela += fk_categoria + " INTEGER KEY, ";
        queryCriarTabela += fk_usuario + " INTEGER KEY, ";
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

    public static String getMateria() {
        return materia;
    }

    public static String getFk_categoria() {
        return fk_categoria;
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
        MateriaDataModel.queryCriarTabela = queryCriarTabela;
    }

}