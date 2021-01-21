package com.baidu.adt.example.recyclerview.vh;

import com.baidu.adt.example.recyclerview.R;
import com.baidu.adt.example.recyclerview.model.User;
import com.baidu.hmi.adpater.BaseVH;

import android.view.ViewGroup;
import android.widget.ImageView;
import net.runningcoder.annotation.ViewHolder;

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
