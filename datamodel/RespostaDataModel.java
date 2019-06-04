package com.topartes.verificaduvida.datamodel;

public class RespostaDataModel {

    private static final String TABELA = "resposta";
    private static final String id = "id";
    private static final String idpk = "idpk";
    private static final String imagem = "imagem";
    private static final String resposta = "resposta";
    private static final String fk_pergunta = "fk_pergunta";
    private static final String fk_usuario = "fk_usuario";
    private static final String data = "data";

    private static String queryCriarTabela = "";

    public static String criarTabela() {

        queryCriarTabela = "CREATE TABLE IF NOT EXISTS " + TABELA;
        queryCriarTabela += "(";
        queryCriarTabela += id + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela += idpk + " INTEGER, ";
        queryCriarTabela += imagem + " LONGTEXT, ";
        queryCriarTabela += resposta + " TEXT, ";
        queryCriarTabela += fk_pergunta + " INTEGER KEY, ";
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

    public static String getImagem() {
        return imagem;
    }

    public static String getResposta() {
        return resposta;
    }

    public static String getFk_pergunta() {
        return fk_pergunta;
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
        RespostaDataModel.queryCriarTabela = queryCriarTabela;
    }

}