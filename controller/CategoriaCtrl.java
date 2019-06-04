package com.topartes.verificaduvida.controller;

import android.content.ContentValues;
import android.content.Context;

import com.topartes.verificaduvida.datamodel.CategoriaDataModel;
import com.topartes.verificaduvida.datasource.CategoriaSource;
import com.topartes.verificaduvida.datasource.DataSource;
import com.topartes.verificaduvida.model.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaCtrl extends CategoriaSource {
    ContentValues dados;
    DataSource dataSource;

    public CategoriaCtrl(Context context) {
        super(context);
    }

    public boolean salvar(Categoria objCategoria) {

        boolean sucesso;

        dados = new ContentValues();

        dados.put(CategoriaDataModel.getIdpk(), objCategoria.getIdpk());
        dados.put(CategoriaDataModel.getCategoria(), objCategoria.getCategoria());
        dados.put(CategoriaDataModel.getFk_usuario(), objCategoria.getFk_usuario());
        dados.put(CategoriaDataModel.getData(), objCategoria.getData());

        sucesso = insert(CategoriaDataModel.getTABELA(), dados);

        return sucesso;
    }

    protected List<Categoria> getAllListCategorias() {
        return super.getAllListCategorias();
    }

    public ArrayList<Categoria> getAllCategorias() {
        return super.getAllCategorias();
    }

    public boolean deletar(Categoria obj) {

        boolean sucesso = true;
        sucesso = dataSource.deletar(CategoriaDataModel.getTABELA(), obj.getId());

        return sucesso;
    }

}
