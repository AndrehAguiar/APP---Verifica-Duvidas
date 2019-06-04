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
import com.topartes.verificaduvida.controller.MateriaCtrl;
import com.topartes.verificaduvida.controller.PerguntaCtrl;
import com.topartes.verificaduvida.controller.RespostaCtrl;
import com.topartes.verificaduvida.model.Materia;
import com.topartes.verificaduvida.model.Pergunta;
import com.topartes.verificaduvida.model.Resposta;
import com.topartes.verificaduvida.util.InRespostaAsyncTask;
import com.topartes.verificaduvida.util.Util;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class PerguntaListAdapter extends ArrayAdapter<Pergunta> implements View.OnClickListener {

    public String imgPergunta, txtPergunta, imagem64;
    Context context;
    Pergunta pergunta;
    PerguntaCtrl perguntaCtrl;
    ArrayList<Pergunta> dados;
    ViewHolder linha;
    Bitmap bitmap, bitmapSelected, image;
    Util util = new Util();
    Boolean dadosValidados = true;
    Resposta novaResposta;
    RespostaCtrl respostaCtrl;
    ImageView imageResposta;

    int idPergunta;

    AlertDialog.Builder alertBox;

    public PerguntaListAdapter(ArrayList<Pergunta> dataSet, Context context) {
        super(context, R.layout.list_pergunta, dataSet);

        this.dados = dataSet;
        this.context = context;
    }

    public void atualizarPerguntas(ArrayList<Pergunta> novosDados) {
        this.dados.clear();
        this.dados.addAll(novosDados);
        notifyDataSetChanged();
    }

    public void onClick(final View view) {
        int posicao = (Integer) view.getTag();
        Object object = getItem(posicao);
        pergunta = (Pergunta) object;
        perguntaCtrl = new PerguntaCtrl(getContext());

        context = getContext();

        switch (view.getId()) {

            case R.id.imgResponder:

                EnableRuntimePermission();

                final View alertView = view.inflate(getContext(), R.layout.fragment_form_resposta, null);

                final TextView txtViewPergunta = alertView.findViewById(R.id.txtViewPergunta);
                final ImageView imagePergunta = alertView.findViewById(R.id.imagePergunta);
                final ImageView imageResposta = alertView.findViewById(R.id.imageResposta);
                final ImageButton imageGaleria = alertView.findViewById(R.id.imageGaleria);
                final ImageButton imageCam = alertView.findViewById(R.id.imageCam);
                final EditText edResposta = alertView.findViewById(R.id.edResposta);
                final Button btnResponder = alertView.findViewById(R.id.btnResponder);

                txtPergunta = pergunta.getPergunta();
                imgPergunta = pergunta.getImagem();
                idPergunta = pergunta.getIdpk();
                image = bitmapSelected;

                try {
                    bitmap = util.base64ToBitmap(imgPergunta);
                    imagePergunta.setImageBitmap(bitmap);
                } catch (Exception e) {
                    bitmap = null;
                }
                txtViewPergunta.setText(txtPergunta);
                imagePergunta.setImageBitmap(bitmap);

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
                btnResponder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        novaResposta = new Resposta();
                        respostaCtrl = new RespostaCtrl(getContext());

                        String strResposta = edResposta.getText().toString();
                        try {
                            if (strResposta == "") {
                                dadosValidados = false;
                                Util.showMensagem(context, "Descreva sua dúvida!");
                                edResposta.requestFocus();
                            }
                            if (imageResposta.getDrawable() != null) {
                                imagem64 = util.bitmapToString(bitmap);
                                novaResposta.setImagem(imagem64);
                            }
                            if (dadosValidados) {
                                novaResposta.setResposta(strResposta);
                                novaResposta.setFk_pergunta(idPergunta);
                                novaResposta.setFk_usuario(-1);

                                try {
                                    respostaCtrl.salvar(novaResposta);

                                    InRespostaAsyncTask task = new InRespostaAsyncTask(novaResposta, context);
                                    task.execute();
                                    Util.showMensagem(context, "Resposta gravada com sucesso!");

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
                alertBox.setTitle("Responder");
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

        pergunta = getItem(position);

        try {
            bitmap = util.base64ToBitmap(pergunta.getImagem());
        } catch (Exception e) {
            bitmap = null;
        }
        if (dataSet == null) {
            linha = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            dataSet = layoutInflater.inflate(R.layout.list_pergunta, parent, false);

            if (pergunta.getImagem() != "") {
                linha.imagePergunta = dataSet.findViewById(R.id.imagePergunta);
            }

            linha.txtPergunta = dataSet.findViewById(R.id.txtPergunta);
            linha.imgResponder = dataSet.findViewById(R.id.imgResponder);
            linha.qtdRespostas = dataSet.findViewById(R.id.qtdRespostas);
            linha.txtMateria = dataSet.findViewById(R.id.txtMateria);

            dataSet.setTag(linha);

        } else {
            linha = (ViewHolder) dataSet.getTag();
        }
        linha.imagePergunta.setImageBitmap(bitmap);
        linha.txtPergunta.setText(pergunta.getPergunta());

        RespostaCtrl respostaCtrl = new RespostaCtrl(getContext());
        respostaCtrl.idPergunta = pergunta.getIdpk();

        long somaRespostas = respostaCtrl.getQtdResposta();
        String txtQtdRespostas = Long.toString(somaRespostas) + " Resposta(s)";
        linha.qtdRespostas.setText(txtQtdRespostas);

        try {
            MateriaCtrl materiaCtrl = new MateriaCtrl(getContext());
            materiaCtrl.idMateria = pergunta.getFk_materia();
            Materia materia = materiaCtrl.getMateriaPergunta();
            String strMateria = materia.getMateria();
            linha.txtMateria.setText(strMateria);
        } catch (Exception e) {
            Util.showMensagem(context, "Atualize as matérias para visualizar");
        }

//        linha.imgResponder.setOnClickListener(this);
//        linha.imgResponder.setTag(position);

        return dataSet;
    }

    private void solicitaAcessoCam() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult((Activity) context, intent, Util.REQUEST_IMAGE_CAPTURE, null);
    }

    private void solicitaAcessoArquivos() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult((Activity) context, intent, Util.INTERNAL_IMAGE, null);
    }

    private void EnableRuntimePermission() {

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

    private void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == Util.INTERNAL_IMAGE && resultCode == RESULT_OK) {

            Uri imgSelecionada = intent.getData();
            String[] colunas = {MediaStore.Images.Media.DATA};

            assert imgSelecionada != null;
            Cursor cursor = context.getContentResolver().query(imgSelecionada, colunas, null, null, null);
            cursor.moveToFirst();

            int indexColuna = cursor.getColumnIndex(colunas[0]);
            String pathImage = cursor.getString(indexColuna);

            cursor.close();

            bitmapSelected = BitmapFactory.decodeFile(pathImage);
            imageResposta.setImageBitmap(image);
        }

        if (requestCode == Util.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            bitmapSelected = (Bitmap) intent.getExtras().get("data");
            imageResposta.setImageBitmap(image);
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

    public static class ViewHolder {
        ImageView imagePergunta, imgResponder;
        TextView txtPergunta, qtdRespostas, txtMateria;
    }
}
