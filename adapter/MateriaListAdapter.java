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
import com.topartes.verificaduvida.controller.MateriaCtrl;
import com.topartes.verificaduvida.controller.PerguntaCtrl;
import com.topartes.verificaduvida.model.Materia;
import com.topartes.verificaduvida.util.Util;

import java.util.ArrayList;

public class MateriaListAdapter extends ArrayAdapter<Materia> implements View.OnClickListener {

    Context context;

    Materia materia;
    MateriaCtrl materiaCtrl;
    ArrayList<Materia> dados;
    ViewHolder linha;

    public MateriaListAdapter(ArrayList<Materia> dataSet, Context context) {
        super(context, R.layout.list_materia, dataSet);

        this.dados = dataSet;
        this.context = context;
    }

    public void atualizarMaterias(ArrayList<Materia> novosDados) {
        this.dados.clear();
        this.dados.addAll(novosDados);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(final View view) {
        int posicao = (Integer) view.getTag();
        Object object = getItem(posicao);
        materia = (Materia) object;
        materiaCtrl = new MateriaCtrl(getContext());

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @NonNull
    public View getView(int position, View dataSet, @NonNull ViewGroup parent) {

        materia = getItem(position);

        if (dataSet == null) {

            linha = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            dataSet = layoutInflater.inflate(R.layout.list_materia, parent, false);

            linha.txtMateria = dataSet.findViewById(R.id.txtMateria);
            linha.qtdPerguntas = dataSet.findViewById(R.id.qtdPerguntas);

            dataSet.setTag(linha);

        } else {
            linha = (ViewHolder) dataSet.getTag();
        }
        linha.txtMateria.setText(materia.getMateria());
        try {
            PerguntaCtrl perguntaCtrl = new PerguntaCtrl(getContext());
            perguntaCtrl.idMateria = materia.getIdpk();

            long somaPerguntas = perguntaCtrl.getQtdPergunta();
            String txtQtdPerguntas = Long.toString(somaPerguntas) + " Perguntas(s)";
            linha.qtdPerguntas.setText(txtQtdPerguntas);

        } catch (Exception e) {
            Util.showMensagem(context, "Atualize as perguntas para visualizar a quantidade.");
        }

        return dataSet;
    }

    private static class ViewHolder {
        TextView txtMateria, qtdPerguntas;
    }
}
