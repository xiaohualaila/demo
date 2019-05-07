package com.aier.ardemo.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.adapter.MyRecyclerViewAdapter;
import com.aier.ardemo.model.Goods;
import com.aier.ardemo.ui.base.BaseActivity;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

public class ShoppingActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    MyRecyclerViewAdapter mAdapter;
    private List<Goods> list;
    @Override
    protected void initViews() {
        tv_title.setText("购物车");

        mRecyclerView =   findViewById(R.id.recyclerView);
        //设置RecyclerView管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //初始化适配器
        mAdapter = new MyRecyclerViewAdapter(list);
        //设置添加或删除item时的动画，这里使用默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initDate(Bundle savedInstanceState) {
        list = new ArrayList();
        list.add(new Goods("儿童椅","红色",1000));
        list.add(new Goods("老头靠椅","红色",1200));
        list.add(new Goods("藤椅","黄色",650));
        list.add(new Goods("白色椅子","白色",900));



    }

    @Override
    protected int getLayout() {
        return R.layout.activity_shop;
    }

    @OnClick(R.id.iv_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
