/*
 * Copyright 2021 ccy.All Rights Reserved
 */
package com.runningcode.noadapter.example.recyclerview;


import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 引入gradle plugin之后可以省略这个注册过程
//        ViewHolderRegistry.add(new ViewHolderRegistry$app());
//        ViewHolderRegistry.add(new ViewHolderRegistry$module());
    }
}
