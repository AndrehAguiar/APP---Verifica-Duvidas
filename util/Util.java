package com.topartes.verificaduvida.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class Util {
    //public static final String URL_WEB_SERVICE = "http://192.168.0.10:81/tig/appwebservice/";
    public static final String URL_WEB_SERVICE = "http://topartes.esy.es/appwebservice/";

    //TEMPO máximo para conectar ao apache
    public static final int CONECTION_TIMEOUT = 10000; //10seg
    // Tempo máximo para resposta do apache
    public static final int READ_TIME_OUT = 15000; //15 seg

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int INTERNAL_IMAGE = 2;
    public static final int PERMISSAO_REQUEST = 3;

    public static void showMensagem(Context context, String mensagem) {
        Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show();
    }

    public Bitmap base64ToBitmap(String encodedByte) {
        String imageBytes = encodedByte.split(",")[1];
        byte[] imageAsBytes = Base64.decode(imageBytes.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public String bitmapToString(Bitmap decodeByte) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        decodeByte.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
