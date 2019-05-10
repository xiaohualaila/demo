package com.aier.ardemo.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.dialog.PayPassDialog;
import com.aier.ardemo.dialog.PayPassView;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.utils.SharedPreferencesUtil;

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
    @BindView(R.id.addr)
    TextView addr;
    private int amount;
    private boolean isWeixinPay = true;

    @Override
    protected void initDate(Bundle savedInstanceState) {
           Bundle bundle = getIntent().getExtras();
           if(bundle!=null){
               amount = bundle.getInt("amount");
           }
          String  add = SharedPreferencesUtil.getString(this,"addr","");
          if(TextUtils.isEmpty(add)){
              addr.setText(add);
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

    @OnClick({R.id.iv_back,R.id.tv_submit,R.id.rb_weixin,R.id.rb_zhifubao,R.id.iv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
                String str =addr.getText().toString();
                if(TextUtils.isEmpty(str)){
                    showToast("请添加收货地址！");
                    return;
                }
                payDialog();
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
            case R.id.iv_add:
                startActivityForResult(new Intent(this, AddressActivity.class), 11);
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
                        startActivity(new Intent(OrderInfoActivity.this,PaySuccessActivity.class));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = data.getStringExtra("result");//得到新Activity 关闭后返回的数据
        addr.setText(result);
    }

}