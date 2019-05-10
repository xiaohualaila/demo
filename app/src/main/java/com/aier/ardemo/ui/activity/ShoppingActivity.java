package com.aier.ardemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aier.ardemo.Config;
import com.aier.ardemo.R;
import com.aier.ardemo.adapter.ShoppingAdapter;
import com.aier.ardemo.dialog.PayPassDialog;
import com.aier.ardemo.dialog.PayPassView;
import com.aier.ardemo.model.Goods;
import com.aier.ardemo.ui.base.BaseActivity;


import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

public class ShoppingActivity extends BaseActivity implements ShoppingAdapter.BackTotalAmountClick {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    ShoppingAdapter mAdapter;
    @BindView(R.id.tv_sum)
    TextView tv_sum;
    @BindView(R.id.iv_choose)
    ImageView iv_choose;

    private List<Goods> list;
    private boolean isBuy = false;
    private int myAmount;
    @Override
    protected void initViews() {
        tv_title.setText("购物车");

        mRecyclerView = findViewById(R.id.recyclerView);
        //设置RecyclerView管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //初始化适配器
        mAdapter = new ShoppingAdapter(list);
        mAdapter.setBackTotalAmountClick(this);
        //设置添加或删除item时的动画，这里使用默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
           String ar_key = bundle.getString(Config.AR_KEY);

        }
    }

    @Override
    protected void initDate(Bundle savedInstanceState) {
        list = new ArrayList();
        list.add(new Goods("儿童椅","红色",1000,false));
        list.add(new Goods("老头靠椅","红色",1200,false));
        list.add(new Goods("藤椅","黄色",650,false));
        list.add(new Goods("白色椅子","白色",900,false));



    }

    @Override
    protected int getLayout() {
        return R.layout.activity_shop;
    }

    @OnClick({R.id.iv_back,R.id.iv_choose,R.id.bt_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_choose:
                isBuy = !isBuy;
                for(int i = 0;i<list.size();i++){
                    list.get(i).setBuy(isBuy);
                }
                mAdapter.notifyDataSetChanged();
                if(isBuy){
                    iv_choose.setImageResource(R.drawable.success_pic);
                }else {
                    iv_choose.setImageResource(R.drawable.not_buy);
                }
                break;
            case R.id.bt_submit:
                if(myAmount==0){
                    showToast("没有要提交的订单！");
                      return;
                }



                payDialog();

                break;
        }
    }


    private void payDialog() {
        final PayPassDialog dialog = new PayPassDialog(this);
        dialog.getPayViewPass()
                .setPayClickListener(new PayPassView.OnPayClickListener() {
                    @Override
                    public void onPassFinish(String passContent) {
                        //6位输入完成,回调
                        Log.i("sss", passContent);

                        Intent intent = new Intent(ShoppingActivity.this, OrderInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("amount", myAmount);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();


                    }

                    @Override
                    public void onPayClose() {
                        dialog.dismiss();
                        //关闭回调
                    }

                    @Override
                    public void onPayForget() {
                        dialog.dismiss();
                        //点击忘记密码回调
                    }
                });
    }



    @Override
    public void onTotalAmount(int amount) {
        myAmount = amount;
        Log.i("sss","amount" + amount);
        tv_sum.setText("￥"+amount);
    }
}
