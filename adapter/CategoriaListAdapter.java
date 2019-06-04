package com.topartes.verificaduvida.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.topartes.verificaduvida.R;
import com.topartes.verificaduvida.controller.CategoriaCtrl;
import com.topartes.verificaduvida.controller.MateriaCtrl;
import com.topartes.verificaduvida.model.Categoria;
import com.topartes.verificaduvida.util.Util;

import java.util.ArrayList;

public class CategoriaListAdapter extends ArrayAdapter<Categoria> implements View.OnClickListener {

    Context context;

    Categoria categoria;
    CategoriaCtrl categoriaCtrl;
    ArrayList<Categoria> dados;
    ViewHolder linha;

    public CategoriaListAdapter(ArrayList<Categoria> dataSet, Context context) {
        super(context, R.layout.list_catogoria, dataSet);

        this.dados = dataSet;
        this.context = context;
    }

    public void atualizarCategorias(ArrayList<Categoria> novosDados) {
        this.dados.clear();
        this.dados.addAll(novosDados);
        notifyDataSetChanged();
    }

    public void onClick(final View view) {
        int posicao = (Integer) view.getTag();
        Object object = getItem(posicao);
        categoria = (Categoria) object;
        categoriaCtrl = new CategoriaCtrl(getContext());

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @NonNull
    public View getView(int position, View dataSet, @NonNull ViewGroup parent) {

        categoria = getItem(position);

        if (dataSet == null) {

            linha = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            dataSet = layoutInflater.inflate(R.layout.list_catogoria, parent, false);

            linha.txtCategoria = dataSet.findViewById(R.id.txtCategoria);
            linha.qtdMaterias = dataSet.findViewById(R.id.qtdMaterias);

            dataSet.setTag(linha);

        } else {
            linha = (ViewHolder) dataSet.getTag();
        }
        linha.txtCategoria.setText(categoria.getCategoria());
        try {
            MateriaCtrl materiaCtrl = new MateriaCtrl(getContext());
            materiaCtrl.idCategoria = categoria.getIdpk();

            long somaMaterias = materiaCtrl.getQtdMateria();
            String txtQtdMaterias = Long.toString(somaMaterias) + " Matéria(s)";
            linha.qtdMaterias.setText(txtQtdMaterias);

        } catch (Exception e) {
            Util.showMensagem(context, "Atualize as matérias para visualizar a quantidade.");
        }


//        linha.txtCategoria.setOnClickListener(this);
//        linha.txtCategoria.setTag(position);

        return dataSet;
    }

    private static class ViewHolder {
        TextView txtCategoria, qtdMaterias;
    }
}
