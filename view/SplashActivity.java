package com.topartes.verificaduvida.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.topartes.verificaduvida.R;
import com.topartes.verificaduvida.controller.AvaliacaoCtrl;
import com.topartes.verificaduvida.controller.CategoriaCtrl;
import com.topartes.verificaduvida.controller.ComentarioCtrl;
import com.topartes.verificaduvida.controller.MateriaCtrl;
import com.topartes.verificaduvida.controller.PerguntaCtrl;
import com.topartes.verificaduvida.controller.RespostaCtrl;
import com.topartes.verificaduvida.datamodel.AvaliacaoDataModel;
import com.topartes.verificaduvida.datamodel.CategoriaDataModel;
import com.topartes.verificaduvida.datamodel.ComentarioDataModel;
import com.topartes.verificaduvida.datamodel.MateriaDataModel;
import com.topartes.verificaduvida.datamodel.PerguntaDataModel;
import com.topartes.verificaduvida.datamodel.RespostaDataModel;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        showTelaSplash();
    }

    private void showTelaSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                PerguntaCtrl perguntaCtrl = new PerguntaCtrl(getBaseContext());
                perguntaCtrl.criarTabela(PerguntaDataModel.criarTabela());

                RespostaCtrl respostaCtrl = new RespostaCtrl(getBaseContext());
                respostaCtrl.criarTabela(RespostaDataModel.criarTabela());

                ComentarioCtrl comentarioCtrl = new ComentarioCtrl(getBaseContext());
                comentarioCtrl.criarTabela(ComentarioDataModel.criarTabela());

                CategoriaCtrl categoriaCtrl = new CategoriaCtrl(getBaseContext());
                categoriaCtrl.criarTabela(CategoriaDataModel.criarTabela());

                MateriaCtrl materiaCtrl = new MateriaCtrl(getBaseContext());
                materiaCtrl.criarTabela(MateriaDataModel.criarTabela());

                AvaliacaoCtrl avaliacaoCtrl = new AvaliacaoCtrl(getBaseContext());
                avaliacaoCtrl.criarTabela(AvaliacaoDataModel.criarTabela());

                Intent telaPrincipal = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(telaPrincipal);
                finish();

            }
        }, SPLASH_TIME_OUT);
    }

}
