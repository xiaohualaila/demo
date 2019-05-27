package com.aier.ardemo.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aier.ardemo.R;
import com.aier.ardemo.bean.GloData;
import com.aier.ardemo.bean.Person;
import com.aier.ardemo.dialog.PayPassDialog;
import com.aier.ardemo.dialog.PayPassView;
import com.aier.ardemo.netapi.HttpApi;
import com.aier.ardemo.netapi.URLConstant;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.utils.SharedPreferencesUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderInfoActivity extends BaseActivity {


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
    private int total;
    private int price;
    private String produce_name;
    private String style;
    private String material;
    private String pro_id;
    private int pro_num;
    private Person person;


    private boolean isWeixinPay = true;
    @Override
    protected void initDate(Bundle savedInstanceState) {
        person = GloData.getPerson();
           Bundle bundle = getIntent().getExtras();
           if(bundle!=null){
               total = bundle.getInt("total");
               price = bundle.getInt("price");
               produce_name = bundle.getString("name","");
               style = bundle.getString("style","");
               material = bundle.getString("material","");
               pro_id = bundle.getString("pro_id","");
               pro_num = bundle.getInt("pro_num");
           }
          String  add = SharedPreferencesUtil.getString(this,"addr","");
          if(!TextUtils.isEmpty(add)){
              addr.setText(add);
          }else {
              addr.setText("");
          }
    }

    @Override
    protected void initViews() {
        if(!TextUtils.isEmpty(person.getUsername())){
            name.setText(person.getUsername());
        }else {
            name.setText("南康家居");
            person.setUsername("南康家居");
        }
        tv_title.setText("订单信息");
        tv_total.setText("金额：" + total);
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
                        dialog.dismiss();
                        //6位输入完成,回调
                        Log.i("sss", passContent);
                        updateOrder();

//
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

    /**
     * 上传订单接口
     */
    private void updateOrder(){
             try {
                JSONObject object =new JSONObject();
                JSONObject param =new JSONObject();
                 JSONArray products =new JSONArray();
                 JSONObject pro =new JSONObject();
                object.put("method","NKCLOUDAPI_UPDATEORDER");
                 param.put("user_account","test");//用户
                 param.put("total_price",total);//总价


                 pro.put("commodity",pro_id);//商品id
                 pro.put("number",pro_num);//商品数量
                 pro.put("price",price);//商品价格
                 pro.put("socialcode","111");//
                 pro.put("style",style);//款式
                 pro.put("material",material);//材料
                 products.put(pro);

                 param.put("products",products);
                object.put("params",param);
                Log.i("xxxx object",object.toString() );

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),object.toString());
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(URLConstant.BASE_URL)
                        .build();
                HttpApi service = retrofit.create(HttpApi.class);
                Call<ResponseBody> call = service.getDataForBody(body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // 已经转换为想要的类型了
                        try {
                            if( response.body()!=null){
                                String str = response.body().string();
                                Log.i("xxxx","str " +str);

                                startActivity(new Intent(OrderInfoActivity.this,PaySuccessActivity.class));
                                finish();

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        Log.i("xxxx","str " +t.getMessage());
                        toastLong("购买失败！");
                    }


                });
            }catch (Exception e){
                e.getMessage();
            }


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){
            String result = data.getStringExtra("result");//得到新Activity 关闭后返回的数据
            addr.setText(result);
        }
    }

}