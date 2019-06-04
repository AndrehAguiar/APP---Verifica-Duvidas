package com.topartes.verificaduvida.model;

public class Avaliacao {
    int id, idpk, fk_Usuario, fk_Resposta, avaliacao;
    String data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdpk() {
        return idpk;
    }

    public void setIdpk(int idpk) {
        this.idpk = idpk;
    }

    public int getFk_Usuario() {
        return fk_Usuario;
    }

    public void setFk_Usuario(int fk_Usuario) {
        this.fk_Usuario = fk_Usuario;
    }

    public int getFk_Resposta() {
        return fk_Resposta;
    }

    public void setFk_Resposta(int fk_Resposta) {
        this.fk_Resposta = fk_Resposta;
    }

    public int getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
