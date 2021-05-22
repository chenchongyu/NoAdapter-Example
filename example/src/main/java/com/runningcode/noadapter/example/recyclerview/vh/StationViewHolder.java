package com.runningcode.noadapter.example.recyclerview.vh;

import com.runningcode.noadapter.example.recyclerview.R;
import com.runningcode.noadapter.example.recyclerview.model.GroupItem;
import com.runningcode.noadapter.adpater.adpater.BaseVH;

import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.runningcode.noadapter.annotation.ViewHolder;


@ViewHolder
public class StationViewHolder extends BaseVH<GroupItem> {
    private TextView textView;

    public StationViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_main);
        textView = itemView.findViewById(R.id.v_title);
    }

    @Override
    public void bindData(GroupItem data) {
        textView.setText(data.text);
    }
}
