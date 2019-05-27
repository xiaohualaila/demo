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
import com.aier.ardemo.model.WeatherResponseBean;
import com.aier.ardemo.netapi.HttpApi;
import com.aier.ardemo.netapi.URLConstant;
import com.aier.ardemo.netsubscribe.OrderSubscribe;
import com.aier.ardemo.netutils.OnSuccessAndFaultListener;
import com.aier.ardemo.netutils.OnSuccessAndFaultSub;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.utils.GsonUtils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private boolean isBuy = true;
    private int myAmount;
    private int pro_num;

    @Override
    protected void initDate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String ar_key = bundle.getString(Config.AR_KEY);
            list = new ArrayList();
            if(ar_key.equals("10302537")){//切换黑胡桃
                list.add(new Goods("10302537","椅座","中年款","黑胡桃木","黑胡桃木",1800,1,true));
                list.add(new Goods("10302537","椅背","中年款","黑胡桃木","黑胡桃木",1000,3,true));
                list.add(new Goods("10302537","扶手","中年款","黑胡桃木","黑胡桃木",1000,2,true));
            }else if(ar_key.equals("10302518")){
                list.add(new Goods("10302518","椅座","中年款","白腊木","白腊木",500,1,true));
                list.add(new Goods("10302518","椅背","中年款","白腊木","白腊木",300,3,true));
                list.add(new Goods("10302518","木扶手","中年款","白腊木","白腊木",200,2,true));
            }else {
                list.add(new Goods("10302527","椅座","中年款","红橡木","红橡木",1100,1,true));
                list.add(new Goods("10302527","椅背","中年款","红橡木","红橡木",1000,3,true));
                list.add(new Goods("10302527","扶手","中年款","红橡木","红橡木",900,2,true));
            }
        }
    }

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
                    toastShort("没有要提交的订单！");
                      return;
                }

                Goods goods = list.get(0);
                Intent intent = new Intent(ShoppingActivity.this, OrderInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("total", myAmount);
                bundle.putInt("price", goods.getPrice());
                bundle.putString("name", goods.getName());
                bundle.putString("style", goods.getStyle());
                bundle.putString("material", goods.getMaterial());
                bundle.putString("pro_id", goods.getPro_id());
                bundle.putInt("pro_num", pro_num);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onTotalAmount(int amount,int num) {
        myAmount = amount;
        pro_num = num;
        Log.i("sss","amount" + amount + "num" +num);
        tv_sum.setText("￥"+amount);
    }
}
