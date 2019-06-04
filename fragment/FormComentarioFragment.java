package com.topartes.verificaduvida.fragment;

import android.Manifest;
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
import com.topartes.verificaduvida.controller.ComentarioCtrl;
import com.topartes.verificaduvida.model.Comentario;
import com.topartes.verificaduvida.util.InComentarAsyncTask;
import com.topartes.verificaduvida.util.Util;

import static android.app.Activity.RESULT_OK;

public class FormComentarioFragment extends Fragment {

    public Bitmap bitmap;
    public int idResposta;
    View view;
    Context context;
    Comentario novaComentario;
    ComentarioCtrl comentarioCtrl;
    Util util = new Util();
    Button btnComentar;
    TextView txtViewResposta;
    ImageView imageResposta, imageComentario;
    EditText edComentario;
    ImageButton imageCam, imageGaleria;
    Boolean dadosValidados = true;
    Bundle bundle;
    String imgResposta, txtResposta, imagem64;

    public FormComentarioFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_form_comentario, container, false);

        context = getContext();
        comentarioCtrl = new ComentarioCtrl(getContext());

        txtViewResposta = view.findViewById(R.id.txtViewResposta);
        imageResposta = view.findViewById(R.id.imageResposta);
        imageComentario = view.findViewById(R.id.imageComentario);
        imageGaleria = view.findViewById(R.id.imageGaleria);
        imageCam = view.findViewById(R.id.imageCam);
        edComentario = view.findViewById(R.id.edComentario);
        btnComentar = view.findViewById(R.id.btnComentar);

        bundle = getArguments();

        comentarioCtrl.idResposta = idResposta;
        try {
            bitmap = util.base64ToBitmap(imgResposta);
        } catch (Exception e) {
            bitmap = null;
        }
        imageResposta.setImageBitmap(bitmap);
        txtViewResposta.setText(txtResposta);

        EnableRuntimePermission();
        imageCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(intent, Util.REQUEST_IMAGE_CAPTURE);
                } catch (Exception e) {
                    Util.showMensagem(context, "Permissão para acesso à CÂMERA negada.\nAtive a permissão para utilizar a CÂMERA.");
                }
            }
        });
        imageGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent(Intent.ACTION_PICK);
                imageIntent.setType("image/*");
                try {
                    startActivityForResult(imageIntent, Util.INTERNAL_IMAGE);
                } catch (Exception e) {
                    Util.showMensagem(context, "Permissão para acesso aos arquivos negada\nAtive a permissão para acessar os arquivos.");
                }

            }
        });

        btnComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (edComentario.getText().toString().length() == 0) {
                        dadosValidados = false;
                        Util.showMensagem(context, "Descreva sua dúvida!");
                        edComentario.requestFocus();
                    }
                    if (imageComentario.getDrawable() != null) {
                        imagem64 = util.bitmapToString(bitmap);
                    }
                    if (dadosValidados) {
                        novaComentario = new Comentario();

                        novaComentario.setImagem(imagem64);
                        novaComentario.setComentario(edComentario.getText().toString());
                        novaComentario.setFk_resposta(idResposta);
                        novaComentario.setFk_usuario(-1);

                        if (comentarioCtrl.salvar(novaComentario)) {
                            Util.showMensagem(context, "Comentario gravado com sucesso!");

                            InComentarAsyncTask task = new InComentarAsyncTask(novaComentario, context);
                            task.execute();

                            ComentarioFragment comentarioFragment = new ComentarioFragment();

                            getActivity().setTitle("Resposta/Comentários");

                            comentarioFragment.idResposta = idResposta;
                            comentarioFragment.imgResposta = imgResposta;
                            comentarioFragment.txtResposta = txtResposta;

                            Bundle bundle = new Bundle();
                            bundle.putInt("idResposta", idResposta);
                            bundle.putString("imgResposta", imgResposta);
                            bundle.putString("txtResposta", txtResposta);
                            comentarioFragment.setArguments(bundle);

                            FragmentTransaction transaction = getFragmentManager().beginTransaction();

                            transaction.replace(R.id.content_fragment, comentarioFragment);
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

    public void EnableRuntimePermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, Util.PERMISSAO_REQUEST);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                Util.showMensagem(context, "Permissão para acesso à câmera concedida");
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
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
            imageComentario.setImageBitmap(bitmap);
        }

        if (requestCode == Util.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            bitmap = (Bitmap) imageIntent.getExtras().get("data");
            imageComentario.setImageBitmap(bitmap);
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