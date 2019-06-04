package com.topartes.verificaduvida.controller;

import android.content.ContentValues;
import android.content.Context;

import com.topartes.verificaduvida.datamodel.PerguntaDataModel;
import com.topartes.verificaduvida.datasource.DataSource;
import com.topartes.verificaduvida.datasource.PerguntaSource;
import com.topartes.verificaduvida.model.Pergunta;

import java.util.ArrayList;
import java.util.List;

public class PerguntaCtrl extends PerguntaSource {
    ContentValues dados;
    DataSource dataSource;

    public PerguntaCtrl(Context context) {
        super(context);
    }

    public boolean salvar(Pergunta objPergunta) {
        boolean sucesso = true;

        dados = new ContentValues();

        dados.put(PerguntaDataModel.getIdpk(), objPergunta.getIdpk());
        dados.put(PerguntaDataModel.getImagem(), objPergunta.getImagem());
        dados.put(PerguntaDataModel.getPergunta(), objPergunta.getPergunta());
        dados.put(PerguntaDataModel.getNivel(), objPergunta.getNivel());
        dados.put(PerguntaDataModel.getFk_materia(), objPergunta.getFk_materia());
        dados.put(PerguntaDataModel.getFk_usuario(), objPergunta.getFk_usuario());
        dados.put(PerguntaDataModel.getData(), objPergunta.getData());

        sucesso = insert(PerguntaDataModel.getTABELA(), dados);

        return sucesso;
    }

    protected List<Pergunta> getAllListaPerguntas() {
        return super.getAllListPergunta();
    }

    public Pergunta getPergunta() {
        return super.getPergunta();
    }

    public ArrayList<Pergunta> getAllPerguntas() {
        return super.getAllPerguntas();
    }

    public ArrayList<Pergunta> getMateriaPerguntas() {
        return super.getMateriaPerguntas();
    }

    public long getQtdPergunta() {
        return super.getQtdPergunta();
    }

    public boolean deletar(Pergunta obj) {
        boolean sucesso = true;
        sucesso = dataSource.deletar(PerguntaDataModel.getTABELA(), obj.getId());

        return sucesso;
    }

}
