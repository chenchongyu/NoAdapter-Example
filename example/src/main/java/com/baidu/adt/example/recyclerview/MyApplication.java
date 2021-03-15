/*
 * Copyright (C) 2021 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.adt.example.recyclerview;

import com.baidu.adu.noadapter.compiler.ViewHolderRegistry$app;
import com.baidu.adu.noadapter.compiler.ViewHolderRegistry$module;
import com.baidu.hmi.adpater.ViewHolderRegistry;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ViewHolderRegistry.add(new ViewHolderRegistry$app());
        ViewHolderRegistry.add(new ViewHolderRegistry$module());
    }
}
