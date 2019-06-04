package com.topartes.verificaduvida.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.topartes.verificaduvida.R;
import com.topartes.verificaduvida.controller.ComentarioCtrl;
import com.topartes.verificaduvida.model.Comentario;
import com.topartes.verificaduvida.util.Util;

import java.util.ArrayList;

public class ComentarioListAdapter extends ArrayAdapter<Comentario> implements View.OnClickListener {

    Context context;

    Comentario comentario;
    ComentarioCtrl comentarioCtrl;
    ArrayList<Comentario> dados;
    ViewHolder linha;
    Bitmap bitmap;
    Util util = new Util();

    public ComentarioListAdapter(ArrayList<Comentario> dataSet, Context context) {
        super(context, R.layout.list_comentario, dataSet);

        this.dados = dataSet;
        this.context = context;
    }

    public void atualizarComentarios(ArrayList<Comentario> novosDados) {
        this.dados.clear();
        this.dados.addAll(novosDados);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(final View view) {
        int posicao = (Integer) view.getTag();
        Object object = getItem(posicao);
        comentario = (Comentario) object;
        comentarioCtrl = new ComentarioCtrl(getContext());

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @NonNull
    public View getView(int position, View dataSet, @NonNull ViewGroup parent) {

        comentario = getItem(position);
        comentario.getImagem();
        try {
            bitmap = util.base64ToBitmap(comentario.getImagem());
        } catch (Exception e) {
            bitmap = null;
        }

        if (dataSet == null) {

            linha = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            dataSet = layoutInflater.inflate(R.layout.list_comentario, parent, false);

            if (comentario.getImagem() != "") {
                linha.imageComentario = dataSet.findViewById(R.id.imageComentario);
                linha.txtComentario = dataSet.findViewById(R.id.txtComentario);
            } else {
                linha.txtComentario = dataSet.findViewById(R.id.txtComentario);
            }

            dataSet.setTag(linha);

        } else {
            linha = (ViewHolder) dataSet.getTag();
        }
        linha.imageComentario.setImageBitmap(bitmap);
        linha.txtComentario.setText(comentario.getComentario());

        return dataSet;
    }

    private static class ViewHolder {
        TextView txtComentario;
        ImageView imageComentario;
    }
}
