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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.topartes.verificaduvida.R;
import com.topartes.verificaduvida.controller.PerguntaCtrl;
import com.topartes.verificaduvida.model.Pergunta;
import com.topartes.verificaduvida.util.InPerguntaAsyncTask;
import com.topartes.verificaduvida.util.Util;

import static android.app.Activity.RESULT_OK;

public class FormPerguntaFragment extends Fragment {

    public Bitmap bitmap;
    public int idMateria;
    Util util = new Util();
    View view;
    Context context;
    Pergunta novaPergunta;
    PerguntaCtrl perguntaCtrl;
    Button btnPerguntar;
    EditText edPergunta;
    Spinner nivelPergunta;
    ImageButton imageCam, imageGaleria;
    ImageView imagePergunta;
    String imagem64;
    Boolean dadosValidados = true;

    public FormPerguntaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_form_pergunta, container, false);
        context = getContext();
        perguntaCtrl = new PerguntaCtrl(getContext());

        nivelPergunta = view.findViewById(R.id.nivelPergunta);
        ArrayAdapter nivel = ArrayAdapter.createFromResource(context, R.array.nivelPergunta, android.R.layout.simple_spinner_item);
        nivelPergunta.setAdapter(nivel);

        imagePergunta = view.findViewById(R.id.imagePergunta);
        imageGaleria = view.findViewById(R.id.imageGaleria);
        imageCam = view.findViewById(R.id.imageCam);
        edPergunta = view.findViewById(R.id.edPergunta);
        btnPerguntar = view.findViewById(R.id.btnPerguntar);

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

        btnPerguntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (edPergunta.getText().toString().length() == 0) {
                        dadosValidados = false;
                        Util.showMensagem(context, "Descreva sua dúvida!");
                        edPergunta.requestFocus();
                    }
                    if (imagePergunta.getDrawable() != null) {
                        imagem64 = util.bitmapToString(bitmap);
                    }
                    if (dadosValidados) {
                        novaPergunta = new Pergunta();

                        novaPergunta.setImagem(imagem64);
                        novaPergunta.setPergunta(edPergunta.getText().toString());
                        novaPergunta.setNivel(nivelPergunta.getSelectedItem().toString());
                        novaPergunta.setFk_materia(idMateria);
                        novaPergunta.setFk_usuario(-1);

                        if (perguntaCtrl.salvar(novaPergunta)) {
                            Util.showMensagem(context, "Pergunta gravada com sucesso!");

                            InPerguntaAsyncTask task = new InPerguntaAsyncTask(novaPergunta, context);
                            task.execute();

                            PrincipalFragment principalFragment = new PrincipalFragment();

                            FragmentTransaction transaction = getFragmentManager().beginTransaction();

                            transaction.replace(R.id.content_fragment, principalFragment);
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
            imagePergunta.setImageBitmap(bitmap);
        }

        if (requestCode == Util.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            bitmap = (Bitmap) imageIntent.getExtras().get("data");
            imagePergunta.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == Util.PERMISSAO_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Util.showMensagem(context, "Permissão para acesso aos arquivos garantida");
            } else {
                Util.showMensagem(context, "Permissão para acesso aos arquivos negada");
            }
        }
    }
}