package com.runningcode.noadapter.example.recyclerview.vh;

import com.runningcode.noadapter.example.recyclerview.R;
import com.runningcode.noadapter.example.recyclerview.model.User;
import com.runningcode.noadapter.adpater.BaseVH;

import android.view.ViewGroup;
import android.widget.ImageView;
import com.runningcode.noadapter.annotation.ViewHolder;


@ViewHolder
public class Station2Holder extends BaseVH<User> {
    private ImageView imageView;

    public Station2Holder(ViewGroup parent) {
        super(parent, R.layout.item_image);
        imageView = itemView.findViewById(R.id.image);
    }

    @Override
    public void bindData(User data) {
        imageView.setImageResource(data.image);
    }
}
