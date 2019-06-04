package com.topartes.verificaduvida.controller;

import android.content.ContentValues;
import android.content.Context;

import com.topartes.verificaduvida.datamodel.AvaliacaoDataModel;
import com.topartes.verificaduvida.datamodel.RespostaDataModel;
import com.topartes.verificaduvida.datasource.DataSource;
import com.topartes.verificaduvida.datasource.RespostaSource;
import com.topartes.verificaduvida.model.Avaliacao;
import com.topartes.verificaduvida.model.Resposta;

import java.util.ArrayList;
import java.util.List;

public class RespostaCtrl extends RespostaSource {
    ContentValues dados;
    DataSource dataSource;

    public RespostaCtrl(Context context) {
        super(context);
    }

    public boolean salvarAvaliacao(Avaliacao objAvaliacao) {

        boolean sucesso;

        dados = new ContentValues();

        dados.put(AvaliacaoDataModel.getFk_usuario(), objAvaliacao.getFk_Usuario());
        dados.put(AvaliacaoDataModel.getFk_resposta(), objAvaliacao.getFk_Resposta());
        dados.put(AvaliacaoDataModel.getAvaliacao(), objAvaliacao.getAvaliacao());
        dados.put(AvaliacaoDataModel.getData(), objAvaliacao.getData());

        sucesso = insert(AvaliacaoDataModel.getTABELA(), dados);

        return sucesso;
    }

    public boolean salvar(Resposta objResposta) {

        boolean sucesso;

        dados = new ContentValues();

        dados.put(RespostaDataModel.getIdpk(), objResposta.getIdpk());
        dados.put(RespostaDataModel.getImagem(), objResposta.getImagem());
        dados.put(RespostaDataModel.getResposta(), objResposta.getResposta());
        dados.put(RespostaDataModel.getFk_pergunta(), objResposta.getFk_pergunta());
        dados.put(RespostaDataModel.getFk_usuario(), objResposta.getFk_usuario());
        dados.put(RespostaDataModel.getData(), objResposta.getData());

        sucesso = insert(RespostaDataModel.getTABELA(), dados);

        return sucesso;
    }

    public List<Resposta> listar() {
        return super.getAllListRespostas();
    }

    @Override
    public ArrayList<Resposta> getAllRespostas() {
        return super.getAllRespostas();
    }

    public boolean deletar(Resposta obj) {

        boolean sucesso;
        sucesso = dataSource.deletar(RespostaDataModel.getTABELA(), obj.getId());
        return sucesso;
    }

    @Override
    public ArrayList<Resposta> getRespostasPergunta() {

        return super.getRespostasPergunta();
    }

    public Resposta getResposta() {

        return super.getResposta();
    }

    public long getQtdResposta() {
        return super.getQtdResposta();
    }


}
