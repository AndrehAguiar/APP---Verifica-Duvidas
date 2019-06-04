package com.topartes.verificaduvida.datamodel;

public class PerguntaDataModel {

    private static final String TABELA = "pergunta";
    private static final String id = "id";
    private static final String idpk = "idpk";
    private static final String imagem = "imagem";
    private static final String pergunta = "pergunta";
    private static final String nivel = "nivel";
    private static final String fk_materia = "fk_materia";
    private static final String fk_usuario = "fk_usuario";
    private static final String data = "data";

    private static String queryCriarTabela = "";

    public static String criarTabela() {
        queryCriarTabela = "CREATE TABLE IF NOT EXISTS " + TABELA;
        queryCriarTabela += "(";
        queryCriarTabela += id + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela += idpk + " INTEGER, ";
        queryCriarTabela += imagem + " LONGTEXT, ";
        queryCriarTabela += pergunta + " TEXT, ";
        queryCriarTabela += nivel + " TEXT, ";
        queryCriarTabela += fk_materia + " INTEGER, ";
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

    public static String getImagem() {
        return imagem;
    }

    public static String getPergunta() {
        return pergunta;
    }

    public static String getNivel() {
        return nivel;
    }

    public static String getFk_materia() {
        return fk_materia;
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
        PerguntaDataModel.queryCriarTabela = queryCriarTabela;
    }

}
