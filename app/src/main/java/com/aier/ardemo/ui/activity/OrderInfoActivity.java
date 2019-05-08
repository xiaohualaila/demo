package com.aier.ardemo.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.ui.base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;


public class OrderInfoActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_total)
    TextView tv_total;
    @BindView(R.id.rb_weixin)
    ImageView rb_weixin;
    @BindView(R.id.rb_zhifubao)
    ImageView rb_zhifubao;
    private int amount;
    private boolean isWeixinPay = true;

    @Override
    protected void initDate(Bundle savedInstanceState) {
           Bundle bundle = getIntent().getExtras();
           if(bundle!=null){
               amount = bundle.getInt("amount");
           }
    }

    @Override
    protected void initViews() {

        tv_title.setText("订单信息");
        tv_total.setText("金额：" + amount);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_order_info;
    }

    @OnClick({R.id.iv_back,R.id.tv_submit,R.id.rb_weixin,R.id.rb_zhifubao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
                startActivity(new Intent(this,PaySuccessActivity.class));
                break;
            case R.id.rb_weixin:
                rb_weixin.setImageResource(R.drawable.selected);
                rb_zhifubao.setImageResource(R.drawable.not_selected);
                isWeixinPay = true;
                break;
            case R.id.rb_zhifubao:
                rb_weixin.setImageResource(R.drawable.not_selected);
                rb_zhifubao.setImageResource(R.drawable.selected);
                isWeixinPay = false;
                break;
        }

    }
}