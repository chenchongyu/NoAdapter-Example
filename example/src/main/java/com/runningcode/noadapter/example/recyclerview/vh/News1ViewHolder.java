package com.runningcode.noadapter.example.recyclerview.vh;

import com.runningcode.noadapter.example.recyclerview.R;
import com.runningcode.noadapter.example.recyclerview.model.News;
import com.runningcode.noadapter.adpater.BaseVH;
import com.bumptech.glide.Glide;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.runningcode.noadapter.annotation.ViewHolder;

@ViewHolder(cls = News.class, type = 2, filed = "type")
public class News1ViewHolder extends BaseVH<News> {
    private TextView textView;
    private ImageView imageView;

    public News1ViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_news_1);
        textView = itemView.findViewById(R.id.text);
        imageView = itemView.findViewById(R.id.image);
    }

    @Override
    public void bindData(News data) {
        textView.setText(data.title);
        Glide.with(itemView.getContext()).load(data.url).into(imageView);
    }

}
