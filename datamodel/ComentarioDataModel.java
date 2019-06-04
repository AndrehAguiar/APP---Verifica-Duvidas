package com.topartes.verificaduvida.datamodel;

public class ComentarioDataModel {

    private static final String TABELA = "comentario";
    private static final String id = "id";
    private static final String idpk = "idpk";
    private static final String imagem = "imagem";
    private static final String comentario = "comentario";
    private static final String fk_resposta = "fk_resposta";
    private static final String fk_usuario = "fk_usuario";
    private static final String data = "data";

    private static String queryCriarTabela = "";

    public static String criarTabela() {

        queryCriarTabela = "CREATE TABLE IF NOT EXISTS " + TABELA;
        queryCriarTabela += "(";
        queryCriarTabela += id + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela += idpk + " INTEGER, ";
        queryCriarTabela += imagem + " LONGTEXT, ";
        queryCriarTabela += comentario + " TEXT, ";
        queryCriarTabela += fk_resposta + " INTEGER KEY, ";
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

    public static String getComentario() {
        return comentario;
    }

    public static String getFk_resposta() {
        return fk_resposta;
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
        ComentarioDataModel.queryCriarTabela = queryCriarTabela;
    }
}