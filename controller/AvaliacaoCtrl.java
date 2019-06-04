package com.topartes.verificaduvida.controller;

import android.content.ContentValues;
import android.content.Context;

import com.topartes.verificaduvida.datamodel.AvaliacaoDataModel;
import com.topartes.verificaduvida.datamodel.MateriaDataModel;
import com.topartes.verificaduvida.datasource.AvaliacaoSource;
import com.topartes.verificaduvida.datasource.DataSource;
import com.topartes.verificaduvida.model.Avaliacao;
import com.topartes.verificaduvida.model.Materia;

import java.util.ArrayList;
import java.util.List;

public class AvaliacaoCtrl extends AvaliacaoSource {
    ContentValues dados;
    DataSource dataSource;

    public AvaliacaoCtrl(Context context) {
        super(context);
    }

    public boolean salvar(Avaliacao objAvaliacao) {

        boolean sucesso;

        dados = new ContentValues();

        dados.put(AvaliacaoDataModel.getFk_usuario(), objAvaliacao.getFk_Usuario());
        dados.put(AvaliacaoDataModel.getFk_resposta(), objAvaliacao.getFk_Resposta());
        dados.put(AvaliacaoDataModel.getAvaliacao(), objAvaliacao.getAvaliacao());
        dados.put(AvaliacaoDataModel.getData(), objAvaliacao.getData());

        sucesso = insert(AvaliacaoDataModel.getTABELA(), dados);

        return sucesso;
    }

    public List<Avaliacao> listar() {
        return super.getAllListAvaliacao();
    }

    @Override
    public ArrayList<Avaliacao> getAllAvaliacao() {
        return super.getAllAvaliacao();
    }

    public boolean deletar(Materia obj) {

        boolean sucesso;
        sucesso = dataSource.deletar(MateriaDataModel.getTABELA(), obj.getId());
        return sucesso;
    }

    public ArrayList<Avaliacao> getAvaliacaoResposta() {
        return super.getAvaliacaoResposta();
    }

    public long somaAvaliacao() {
        return super.somaAvaliacao();
    }
}
