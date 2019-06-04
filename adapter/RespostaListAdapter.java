package com.topartes.verificaduvida.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.topartes.verificaduvida.R;
import com.topartes.verificaduvida.controller.AvaliacaoCtrl;
import com.topartes.verificaduvida.controller.ComentarioCtrl;
import com.topartes.verificaduvida.controller.RespostaCtrl;
import com.topartes.verificaduvida.model.Comentario;
import com.topartes.verificaduvida.model.Resposta;
import com.topartes.verificaduvida.util.InComentarAsyncTask;
import com.topartes.verificaduvida.util.Util;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class RespostaListAdapter extends ArrayAdapter<Resposta> implements View.OnClickListener {

    public String imgResposta, txtResposta, imagem64;
    Context context;
    Resposta resposta;
    RespostaCtrl respostaCtrl;
    ArrayList<Resposta> dados;
    ViewHolder linha;
    Bitmap bitmap, bitmapSelected, image;
    Util util = new Util();
    Boolean dadosValidados = true;
    Comentario novaComentario;
    ComentarioCtrl comentarioCtrl;
    ImageView imageResposta;
    int idResposta;

    AlertDialog.Builder alertBox;

    public RespostaListAdapter(ArrayList<Resposta> dataSet, Context context) {
        super(context, R.layout.list_resposta, dataSet);

        this.dados = dataSet;
        this.context = context;
    }

    public void atualizarRespostas(ArrayList<Resposta> novosDados) {
        this.dados.clear();
        this.dados.addAll(novosDados);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(final View view) {
        int posicao = (Integer) view.getTag();
        Object object = getItem(posicao);
        resposta = (Resposta) object;
        respostaCtrl = new RespostaCtrl(getContext());
        switch (view.getId()) {

            case R.id.imgComentar:

                EnableRuntimePermission();

                final View alertView = view.inflate(getContext(), R.layout.fragment_form_comentario, null);

                final TextView txtViewResposta = alertView.findViewById(R.id.txtViewResposta);
                final ImageView imageResposta = alertView.findViewById(R.id.imageResposta);
                final ImageView imageComentario = alertView.findViewById(R.id.imageComentario);
                final ImageButton imageGaleria = alertView.findViewById(R.id.imageGaleria);
                final ImageButton imageCam = alertView.findViewById(R.id.imageCam);
                final EditText edComentario = alertView.findViewById(R.id.edComentario);
                final Button btnComentar = alertView.findViewById(R.id.btnComentar);

                txtResposta = resposta.getResposta();
                imgResposta = resposta.getImagem();
                idResposta = resposta.getIdpk();
                image = bitmapSelected;

                try {
                    bitmap = util.base64ToBitmap(imgResposta);
                    imageResposta.setImageBitmap(bitmap);
                } catch (Exception e) {
                    bitmap = null;
                }
                txtViewResposta.setText(txtResposta);
                imageResposta.setImageBitmap(bitmap);

                imageCam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        solicitaAcessoCam();
                        imageResposta.setImageBitmap(image);
                    }
                });

                imageGaleria.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        solicitaAcessoArquivos();
                        imageResposta.setImageBitmap(image);
                    }
                });
                btnComentar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        novaComentario = new Comentario();
                        comentarioCtrl = new ComentarioCtrl(getContext());

                        String strComentario = edComentario.getText().toString();
                        try {
                            if (strComentario == "") {
                                dadosValidados = false;
                                Util.showMensagem(context, "Descreva sua dúvida!");
                                edComentario.requestFocus();
                            }
                            if (imageResposta.getDrawable() != null) {
                                imagem64 = util.bitmapToString(bitmap);
                                novaComentario.setImagem(imagem64);
                            }
                            if (dadosValidados) {
                                novaComentario.setComentario(strComentario);
                                novaComentario.setFk_resposta(idResposta);
                                novaComentario.setFk_usuario(-1);

                                try {
                                    comentarioCtrl.salvar(novaComentario);

                                    InComentarAsyncTask task = new InComentarAsyncTask(novaComentario, context);
                                    task.execute();
                                    Util.showMensagem(context, "Comentário gravado com sucesso!");

                                } catch (Exception e) {
                                    Util.showMensagem(context, "Erro ao salvar os dados!");
                                }
                            }
                        } catch (Exception e) {
                            Util.showMensagem(context, "Confirme as informações e tente novamente!");
                        }
                    }
                });
                alertBox = new AlertDialog.Builder(alertView.getRootView().getContext());
                alertBox.setCancelable(true);
                alertBox.setTitle("Comentar");
                alertBox.setView(alertView);
                alertBox.show();
                break;
        }

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @NonNull
    public View getView(int position, View dataSet, @NonNull ViewGroup parent) {

        resposta = getItem(position);
        resposta.getImagem();
        try {
            bitmap = util.base64ToBitmap(resposta.getImagem());
        } catch (Exception e) {
            bitmap = null;
        }
        if (dataSet == null) {
            linha = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            dataSet = layoutInflater.inflate(R.layout.list_resposta, parent, false);

            if (resposta.getImagem() != "") {
                linha.imageResposta = dataSet.findViewById(R.id.imageResposta);
            }
            linha.txtResposta = dataSet.findViewById(R.id.txtResposta);
            linha.imgComentar = dataSet.findViewById(R.id.imgComentar);
            linha.qtdComentarios = dataSet.findViewById(R.id.qtdComentarios);
            linha.imgAvaliacao = dataSet.findViewById(R.id.imgAvaliacao);

            dataSet.setTag(linha);

        } else {
            linha = (ViewHolder) dataSet.getTag();
        }
        linha.imageResposta.setImageBitmap(bitmap);
        linha.txtResposta.setText(resposta.getResposta());

        ComentarioCtrl comentarioCtrl = new ComentarioCtrl(getContext());
        comentarioCtrl.idResposta = resposta.getIdpk();

        long somaComentarios = comentarioCtrl.getQtdComentario();
        String txtQtdComentarios = Long.toString(somaComentarios) + " Comentário(s)";
        linha.qtdComentarios.setText(txtQtdComentarios);

        AvaliacaoCtrl avaliacaoCtrl = new AvaliacaoCtrl(getContext());
        avaliacaoCtrl.idResposta = resposta.getIdpk();
        long valorAvalia = avaliacaoCtrl.somaAvaliacao();

        if (valorAvalia < 0) {
            linha.imgAvaliacao.setImageResource(R.drawable.ic_deslike);
        } else if (valorAvalia > 0) {
            linha.imgAvaliacao.setImageResource(R.drawable.ic_like);
        } else {
            linha.imgAvaliacao.setImageResource(R.drawable.ic_alerta);
        }
//        linha.imgComentar.setOnClickListener(this);
//        linha.imgComentar.setTag(position);

        return dataSet;
    }

    public void solicitaAcessoCam() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult((Activity) context, intent, Util.REQUEST_IMAGE_CAPTURE, null);
    }

    public void solicitaAcessoArquivos() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult((Activity) context, intent, Util.INTERNAL_IMAGE, null);
    }

    public void EnableRuntimePermission() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, Util.PERMISSAO_REQUEST);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                Toast.makeText(context, "CÂMERA permission allows us to Access CÂMERA app", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{
                        Manifest.permission.CAMERA}, Util.PERMISSAO_REQUEST);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageIntent) {

        if (requestCode == Util.INTERNAL_IMAGE && resultCode == RESULT_OK) {

            Uri imgSelecionada = imageIntent.getData();
            String[] colunas = {MediaStore.Images.Media.DATA};

            Cursor cursor = context.getContentResolver().query(imgSelecionada, colunas, null, null, null);
            cursor.moveToFirst();

            int indexColuna = cursor.getColumnIndex(colunas[0]);
            String pathImage = cursor.getString(indexColuna);

            cursor.close();

            bitmap = BitmapFactory.decodeFile(pathImage);
            //imageResposta.setImageBitmap(bitmap);
        }

        if (requestCode == Util.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            bitmap = (Bitmap) imageIntent.getExtras().get("data");
            //imageResposta.setImageBitmap(bitmap);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == Util.PERMISSAO_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permissão para acesso aos arquivos garantida", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Permissão para acesso aos arquivos negada", Toast.LENGTH_LONG).show();
            }
        }
    }

    private static class ViewHolder {
        ImageView imageResposta, imgComentar, imgAvaliacao;
        TextView txtResposta, qtdComentarios;
    }
}
