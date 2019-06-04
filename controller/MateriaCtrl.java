package com.topartes.verificaduvida.controller;

import android.content.ContentValues;
import android.content.Context;

import com.topartes.verificaduvida.datamodel.MateriaDataModel;
import com.topartes.verificaduvida.datasource.DataSource;
import com.topartes.verificaduvida.datasource.MateriaSource;
import com.topartes.verificaduvida.model.Materia;

import java.util.ArrayList;
import java.util.List;

public class MateriaCtrl extends MateriaSource {
    ContentValues dados;
    DataSource dataSource;
    Materia materia;

    public MateriaCtrl(Context context) {
        super(context);
    }

    public boolean salvar(Materia objMateria) {

        boolean sucesso;

        dados = new ContentValues();

        dados.put(MateriaDataModel.getIdpk(), objMateria.getIdpk());
        dados.put(MateriaDataModel.getMateria(), objMateria.getMateria());
        dados.put(MateriaDataModel.getFk_categoria(), objMateria.getFk_categoria());
        dados.put(MateriaDataModel.getFk_usuario(), objMateria.getFk_usuario());
        dados.put(MateriaDataModel.getData(), objMateria.getData());

        sucesso = insert(MateriaDataModel.getTABELA(), dados);

        return sucesso;
    }

    public List<Materia> listar() {
        return super.getAllListMaterias();
    }

    @Override
    public ArrayList<Materia> getAllMaterias() {
        return super.getAllMaterias();
    }

    public boolean deletar(Materia obj) {

        boolean sucesso;
        sucesso = dataSource.deletar(MateriaDataModel.getTABELA(), obj.getId());
        return sucesso;
    }

    @Override
    public ArrayList<Materia> getMateriasCategoria() {
        return super.getMateriasCategoria();
    }

    public Materia getMateriaPergunta() {
        return super.getMateriaPergunta();
    }

    public long getQtdMateria() {
        return super.getQtdMateria();
    }
}
