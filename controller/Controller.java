package com.topartes.verificaduvida.controller;

import android.content.Context;

import com.topartes.verificaduvida.datasource.DataSource;

public class Controller extends DataSource {

    private final Context context;

    public Controller(Context context) {
        super(context);
        this.context = context;
    }

}
