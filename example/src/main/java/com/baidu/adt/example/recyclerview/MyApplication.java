/*
 * Copyright (C) 2021 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.adt.example.recyclerview;


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
