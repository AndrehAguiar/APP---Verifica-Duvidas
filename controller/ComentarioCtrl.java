package com.topartes.verificaduvida.controller;

import android.content.ContentValues;
import android.content.Context;

import com.topartes.verificaduvida.datamodel.ComentarioDataModel;
import com.topartes.verificaduvida.datasource.ComentarioSource;
import com.topartes.verificaduvida.datasource.DataSource;
import com.topartes.verificaduvida.model.Comentario;

import java.util.ArrayList;
import java.util.List;

public class ComentarioCtrl extends ComentarioSource {
    ContentValues dados;
    DataSource dataSource;
    ComentarioSource comentarioSource;

    public ComentarioCtrl(Context context) {
        super(context);
    }

    public int idResposta() {
        this.idResposta = idResposta;
        return idResposta();
    }

    public boolean salvar(Comentario objComentario) {

        boolean sucesso;

        dados = new ContentValues();

        dados.put(ComentarioDataModel.getIdpk(), objComentario.getIdpk());
        dados.put(ComentarioDataModel.getImagem(), objComentario.getImagem());
        dados.put(ComentarioDataModel.getComentario(), objComentario.getComentario());
        dados.put(ComentarioDataModel.getFk_resposta(), objComentario.getFk_resposta());
        dados.put(ComentarioDataModel.getFk_usuario(), objComentario.getFk_usuario());
        dados.put(ComentarioDataModel.getData(), objComentario.getData());

        sucesso = insert(ComentarioDataModel.getTABELA(), dados);

        return sucesso;
    }

    public List<Comentario> listar() {
        return super.getAllListComentarios();
    }

    @Override
    public ArrayList<Comentario> getAllComentarios() {
        return super.getAllComentarios();
    }

    public boolean deletar(Comentario obj) {

        boolean sucesso = true;
        sucesso = dataSource.deletar(ComentarioDataModel.getTABELA(), obj.getId());
        return sucesso;
    }

    @Override
    public ArrayList<Comentario> getComentariosResposta() {

        return super.getComentariosResposta();
    }

    public Comentario getComentario() {

        return super.getComentario();
    }

    public long getQtdComentario() {
        return super.getQtdComentario();
    }
}
