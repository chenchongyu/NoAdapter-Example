package com.baidu.adt.example.recyclerview.vh;

import com.baidu.adt.example.recyclerview.R;
import com.baidu.adt.example.recyclerview.model.News;
import com.baidu.hmi.adpater.BaseVH;
import com.bumptech.glide.Glide;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import net.runningcoder.annotation.ViewHolder;

@ViewHolder(cls = News.class, type = 2, filed = "type")
public class News2ViewHolder extends BaseVH<News> {
    private TextView textView;
    private ImageView imageView;

    public News2ViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_news_2);
        textView = itemView.findViewById(R.id.text);
        imageView = itemView.findViewById(R.id.image);

    }

    @Override
    public void bindData(News data) {
        textView.setText(data.title);
        Glide.with(itemView.getContext()).load(data.url).into(imageView);
    }

}
