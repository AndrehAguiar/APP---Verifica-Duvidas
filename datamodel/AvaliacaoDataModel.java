package com.topartes.verificaduvida.datamodel;

public class AvaliacaoDataModel {

    private static final String TABELA = "avaliacao";
    private static final String fk_usuario = "fk_usuario";
    private static final String fk_resposta = "fk_resposta";
    private static final String avaliacao = "avaliacao";
    private static final String data = "data";

    private static String queryCriarTabela = "";

    public static String criarTabela() {

        queryCriarTabela = "CREATE TABLE IF NOT EXISTS " + TABELA;
        queryCriarTabela += "(";
        queryCriarTabela += fk_usuario + " INTEGER KEY, ";
        queryCriarTabela += fk_resposta + " INTEGER KEY, ";
        queryCriarTabela += avaliacao + " INTEGER, ";
        queryCriarTabela += data + " TEXT ";
        queryCriarTabela += ")";

        return queryCriarTabela;
    }

    public static String getTABELA() {
        return TABELA;
    }

    public static String getFk_usuario() {
        return fk_usuario;
    }

    public static String getFk_resposta() {
        return fk_resposta;
    }

    public static String getAvaliacao() {
        return avaliacao;
    }

    public static String getData() {
        return data;
    }

    public static void setQueryCriarTabela(String queryCriarTabela) {
        AvaliacaoDataModel.queryCriarTabela = queryCriarTabela;
    }
}