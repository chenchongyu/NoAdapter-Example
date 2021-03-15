/*
 * Copyright (C) 2021 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.hmi.module_example;

import com.baidu.hmi.adpater.BaseVH;

import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.baidu.hmi.annotation.ViewHolder;

@ViewHolder
public class UserViewHolder extends BaseVH<Student> {
    TextView textView;
    public UserViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_user);
        textView = itemView.findViewById(R.id.v_name);
    }

    @Override
    public void bindData(Student data) {
        textView.setText(data.getName());
    }
}
