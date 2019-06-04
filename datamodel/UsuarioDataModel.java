package com.topartes.verificaduvida.datamodel;

public class UsuarioDataModel {

    private static final String TABELA = "users";
    private static final String id_usuario = "id_usuario";
    private static final String email = "email";
    private static final String formacao = "formacao";
    private static final String name = "name";
    private static final String sobrenome = "sobrenome";
    private static final String pass = "password";
    private static final String data = "data";

    private static String queryCriarTabela = " ";

    public static String criarTabela() {
        queryCriarTabela = " DROP TABLE IF EXISTS" + TABELA;
        queryCriarTabela += " CREATE TABLE IF NOT EXISTS " + TABELA;
        queryCriarTabela += "(";
        queryCriarTabela += id_usuario + " BIGINT(20) NOT NULL AUTOINCREMENT,";
        queryCriarTabela += email + " varchar(35) UNIQUE KEY NOT NULL,";
        queryCriarTabela += formacao + " VARCHAR(35) NOT NULL,";
        queryCriarTabela += name + " VARCHAR(35) NOT NULL,";
        queryCriarTabela += sobrenome + " VARCHAR(35) NOT NULL, ";
        queryCriarTabela += pass + " VARCHAR(255) NOT NULL, ";
        queryCriarTabela += data + " datetime NOT NULL DEFAULT CURRENT_TIMESTAMP, ";
        queryCriarTabela += " PRIMARY KEY (" + id_usuario + "), ";
        queryCriarTabela += " UNIQUE KEY " + email + " (" + email + "), ";
        queryCriarTabela += ")";

        return queryCriarTabela;
    }

    public static String getTABELA() {
        return TABELA;
    }

    public static String getId_usuario() {
        return id_usuario;
    }

    public static String getEmail() {
        return email;
    }

    public static String getFormacao() {
        return formacao;
    }

    public static String getName() {
        return name;
    }

    public static String getSobrenome() {
        return sobrenome;
    }

    public static String getPass() {
        return pass;
    }

    public static String getData() {
        return data;
    }

    public static String getQueryCriarTabela() {
        return queryCriarTabela;
    }
}