package com.topartes.verificaduvida.model;

public class Resposta {

    int id;
    int idpk;
    String imagem;
    String resposta;
    int fk_pergunta;
    int fk_usuario;
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

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public int getFk_pergunta() {
        return fk_pergunta;
    }

    public void setFk_pergunta(int fk_pergunta) {
        this.fk_pergunta = fk_pergunta;
    }

    public int getFk_usuario() {
        return fk_usuario;
    }

    public void setFk_usuario(int fk_usuario) {
        this.fk_usuario = fk_usuario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
