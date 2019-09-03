package com.aier.ardemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.bean.GloData;
import com.aier.ardemo.bean.Person;
import com.aier.ardemo.dialog.PayPassDialog;
import com.aier.ardemo.dialog.PayPassView;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.ui.contract.OrderContract;
import com.aier.ardemo.ui.presenter.OrderPresenter;
import com.aier.ardemo.utils.SharedPreferencesUtil;
import butterknife.BindView;
import butterknife.OnClick;

public class OrderInfoActivity extends BaseActivity implements OrderContract.View {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tv_total)
    TextView tv_total;
    @BindView(R.id.rb_weixin)
    ImageView rb_weixin;
    @BindView(R.id.rb_zhifubao)
    ImageView rb_zhifubao;
    @BindView(R.id.addr)
    TextView addr;
    private String produces;
    private double total_price;
    private int pro_num;
    private Person person;

    private boolean isWeixinPay = true;
    private OrderPresenter presenter;
    private static Handler handler = new Handler();
    @Override
    protected void initDate() {
        presenter = new OrderPresenter(this);
        person = GloData.getPerson();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            total_price = bundle.getDouble("total_price");
            pro_num= bundle.getInt("pro_num");
            produces = bundle.getString("produces");
        }
    }

    @Override
    protected void initViews() {
        String address = SharedPreferencesUtil.getString(this, "addr", "");
        if (!TextUtils.isEmpty(address)) {
            addr.setText(address);
        } else {
            addr.setText("江西省赣州市南康区家具小镇");
        }
        if (!TextUtils.isEmpty(person.getUsername())) {
            name.setText(person.getUsername());
        } else {
            name.setText("南康家居");
            person.setUsername("南康家居");
        }
        tv_title.setText("订单信息");
        tv_total.setText("金额：" + total_price);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_order_info;
    }

    @OnClick({R.id.iv_back, R.id.tv_submit, R.id.rb_weixin, R.id.rb_zhifubao, R.id.iv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
                String str = addr.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    toastShort("请添加收货地址！");
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
                        handler.postDelayed(() -> {
                            dialog.dismiss();
                            //6位输入完成,回调
                            presenter.updateOrder(person.getUsername(),total_price,pro_num,produces);
                        },500);

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
        if (data != null) {
            String result = data.getStringExtra("result");//得到新Activity 关闭后返回的数据
            addr.setText(result);
        }
    }

    @Override
    public void getDataSuccess() {
        SharedPreferencesUtil.putString(mContext, "shoppingData", "shoppings","");
        SharedPreferencesUtil.putInt(mContext, "shoppingData", "shopping_num", 0);
        startActiviys(PaySuccessActivity.class);
        finish();
    }

    @Override
    public void getDataFail() {
        toastLong("购买失败！");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dispose();
    }
}