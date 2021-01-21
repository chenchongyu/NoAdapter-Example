package com.baidu.adt.example.recyclerview;

import java.util.ArrayList;
import java.util.List;

import com.baidu.adt.example.recyclerview.model.GroupItem;
import com.baidu.adt.example.recyclerview.model.News;
import com.baidu.adt.example.recyclerview.model.User;
import com.baidu.hmi.adpater.NoAdapter;
import com.baidu.hmi.adpater.OnItemClickListener;

import android.os.Bundle;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author： chenchongyu
 * Date: 2019/2/26
 * Description:
 */
public class RecyclerTestAct extends FragmentActivity {
    RecyclerView mRecyclerView;
    List allList = new ArrayList();
    private NoAdapter adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_tvlist);
        mRecyclerView = findViewById(R.id.v_recyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        initData();

    }

    private void initData() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allList.add(new News(1, "乔丹退役",
                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2133214656,"
                        + "3573702769&fm=27&gp=0.jpg"));
        //            for (int i = 0; i < 3; i++) {
        //                allList.add(new GroupItem("第" + i + "条item！"));
        //            }
        allList.add(new News(2, "科比退役2",
                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2133214656,"
                        + "3573702769&fm=27&gp=0.jpg"));
        allList.add(new User(R.drawable.girl));
        for (int i = 4; i < 8; i++) {
            allList.add(new GroupItem("第" + i + "条item！"));
        }
        allList.add(new User(R.drawable.girl3));
        allList.add(new User(R.drawable.girl5));

        adapter = new NoAdapter(allList);
        adapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object data) {
                if (data instanceof GroupItem) {
                    Toast.makeText(RecyclerTestAct.this, ((GroupItem) data).text,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        mRecyclerView.setAdapter(adapter);
        adapter.notifyItemRangeInserted(0, allList.size());

    }
}
