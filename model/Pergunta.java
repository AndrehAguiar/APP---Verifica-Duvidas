package com.topartes.verificaduvida.model;

public class Pergunta {
    int id;
    int idpk;
    String imagem;
    String pergunta;
    String nivel;
    int Fk_materia;
    int Fk_usuario;
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

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public int getFk_materia() {
        return Fk_materia;
    }

    public void setFk_materia(int fk_materia) {
        Fk_materia = fk_materia;
    }

    public int getFk_usuario() {
        return Fk_usuario;
    }

    public void setFk_usuario(int fk_usuario) {
        Fk_usuario = fk_usuario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
