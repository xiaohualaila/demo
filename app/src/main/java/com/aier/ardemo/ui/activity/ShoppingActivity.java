package com.aier.ardemo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.adapter.ShoppingAdapter;
import com.aier.ardemo.bean.DataBean;
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

    private List<DataBean> list;
    private boolean isBuy = true;
    private double myAmount;
    private int pro_num;

    @Override
    protected void initDate() {
        list = new ArrayList<>();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            DataBean ar_model = (DataBean) bundle.getSerializable("ar_model");
            list.add(ar_model);
        }
    }

    @Override
    protected void initViews() {
        tv_title.setText("购物车");
        mRecyclerView = findViewById(R.id.recyclerView);
        //设置RecyclerView管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //初始化适配器
        mAdapter = new ShoppingAdapter(list,this);
        mAdapter.setBackTotalAmountClick(this);
        //设置添加或删除item时的动画，这里使用默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);

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
                if(pro_num==0){
                    toastShort("没有要提交的订单！");
                      return;
                }

                DataBean goods = list.get(0);
                Intent intent = new Intent(ShoppingActivity.this, OrderInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("total", myAmount);
                bundle.putDouble("price", goods.getPrice());
                bundle.putString("name", goods.getTitle());
                bundle.putString("style", "");
                bundle.putString("desp", goods.getDesp());
                bundle.putInt("pro_id", goods.getGid());
                bundle.putInt("pro_num", pro_num);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onTotalAmount(double amount,int num) {
        myAmount = amount;
        pro_num = num;
        Log.i("sss","amount" + amount + "num" +num);
        tv_sum.setText("￥"+amount);
    }

    public static final void starShoppingAc(Context context, DataBean bean){
        Intent intent = new Intent(context, ShoppingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ar_model",bean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
