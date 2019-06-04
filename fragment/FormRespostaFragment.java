package com.topartes.verificaduvida.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.topartes.verificaduvida.R;
import com.topartes.verificaduvida.controller.RespostaCtrl;
import com.topartes.verificaduvida.model.Resposta;
import com.topartes.verificaduvida.util.InRespostaAsyncTask;
import com.topartes.verificaduvida.util.Util;

import static android.app.Activity.RESULT_OK;

public class FormRespostaFragment extends Fragment {

    public Bitmap bitmap;
    public String imgPergunta;
    public String txtPergunta;
    public int idPergunta;
    View view;
    Context context;
    Resposta novaResposta;
    RespostaCtrl respostaCtrl;
    Util util = new Util();
    Button btnResponder;
    TextView txtViewPergunta;
    ImageView imagePergunta, imageResposta;
    EditText edResposta;
    ImageButton imageCam, imageGaleria;
    Boolean dadosValidados = true;
    Bundle bundle;
    String imagem64;

    public FormRespostaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_form_resposta, container, false);
        context = getContext();
        respostaCtrl = new RespostaCtrl(getContext());

        txtViewPergunta = view.findViewById(R.id.txtViewPergunta);
        imagePergunta = view.findViewById(R.id.imagePergunta);
        imageResposta = view.findViewById(R.id.imageResposta);
        imageGaleria = view.findViewById(R.id.imageGaleria);
        imageCam = view.findViewById(R.id.imageCam);
        edResposta = view.findViewById(R.id.edResposta);
        btnResponder = view.findViewById(R.id.btnResponder);

        bundle = getArguments();

        respostaCtrl.idPergunta = idPergunta;
        try {
            bitmap = util.base64ToBitmap(imgPergunta);
        } catch (Exception e) {
            bitmap = null;
        }
        imagePergunta.setImageBitmap(bitmap);
        txtViewPergunta.setText(txtPergunta);

        EnableRuntimePermission();
        imageCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    solicitaAcessoCam();
                    //startActivityForResult(intent, Util.REQUEST_IMAGE_CAPTURE);
                } catch (Exception e) {
                    Util.showMensagem(context, "Permissão para acesso à CÂMERA negada.\nAtive a permissão para utilizar a CÂMERA.");
                }
            }
        });
        imageGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    solicitaAcessoArquivos();
                } catch (Exception e) {
                    Util.showMensagem(context, "Permissão para acesso aos arquivos negada\nAtive a permissão para acessar os arquivos.");
                }

            }
        });

        btnResponder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (edResposta.getText().toString().length() == 0) {
                        dadosValidados = false;
                        Util.showMensagem(context, "Descreva sua dúvida!");
                        edResposta.requestFocus();
                    }
                    if (imageResposta.getDrawable() != null) {
                        imagem64 = util.bitmapToString(bitmap);
                    }
                    if (dadosValidados) {
                        novaResposta = new Resposta();

                        novaResposta.setImagem(imagem64);
                        novaResposta.setResposta(edResposta.getText().toString());
                        novaResposta.setFk_pergunta(idPergunta);
                        novaResposta.setFk_usuario(-1);

                        if (respostaCtrl.salvar(novaResposta)) {
                            Util.showMensagem(context, "Resposta gravada com sucesso!");

                            InRespostaAsyncTask task = new InRespostaAsyncTask(novaResposta, context);
                            task.execute();

                            RespostaFragment respostaFragment = new RespostaFragment();

                            respostaFragment.idPergunta = idPergunta;
                            respostaFragment.imgPergunta = imgPergunta;
                            respostaFragment.txtPergunta = txtPergunta;

                            Bundle bundle = new Bundle();
                            bundle.putInt("idPergunta", idPergunta);
                            bundle.putString("imgPergunta", imgPergunta);
                            bundle.putString("txtPergunta", txtPergunta);
                            respostaFragment.setArguments(bundle);

                            getActivity().setTitle("Pergunta/Respostas");

                            FragmentTransaction transaction = getFragmentManager().beginTransaction();

                            transaction.replace(R.id.content_fragment, respostaFragment);
                            transaction.addToBackStack(null);

                            transaction.commit();

                        } else {
                            Util.showMensagem(context, "Erro ao salvar os dados!");
                        }

                    }
                } catch (Exception e) {
                    Util.showMensagem(context, "Confirme as informações e tente novamente!");
                }
            }
        });
        return view;
    }

    public void solicitaAcessoCam() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Util.REQUEST_IMAGE_CAPTURE);
    }

    public void solicitaAcessoArquivos() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Util.INTERNAL_IMAGE);
    }

    public void EnableRuntimePermission() {

        if (ContextCompat.checkSelfPermission((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageIntent) {
        super.onActivityResult(requestCode, resultCode, imageIntent);
        if (requestCode == Util.INTERNAL_IMAGE && resultCode == RESULT_OK) {

            Uri imgSelecionada = imageIntent.getData();
            String[] colunas = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(imgSelecionada, colunas, null, null, null);
            cursor.moveToFirst();

            int indexColuna = cursor.getColumnIndex(colunas[0]);
            String pathImage = cursor.getString(indexColuna);

            cursor.close();

            bitmap = BitmapFactory.decodeFile(pathImage);
            imageResposta.setImageBitmap(bitmap);
        }

        if (requestCode == Util.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            bitmap = (Bitmap) imageIntent.getExtras().get("data");
            imageResposta.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == Util.PERMISSAO_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permissão para acesso aos arquivos garantida", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Permissão para acesso aos arquivos negada", Toast.LENGTH_LONG).show();
            }
        }
    }
}