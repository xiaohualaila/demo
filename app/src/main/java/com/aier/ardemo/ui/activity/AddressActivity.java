package com.aier.ardemo.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class AddressActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_add)
    EditText et_add;
    @Override
    protected void initViews() {
        tv_title.setText("收货地址");
    }

    @Override
    protected void initDate(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_addr;
    }


    @OnClick({R.id.iv_back,R.id.tv_determine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_determine:
                String addr =et_add.getText().toString().trim();
                if(TextUtils.isEmpty(addr)){
                    showToast("地址不能为空!");
                    return;
                }
                SharedPreferencesUtil.putString(this,"addr",addr);
                Intent intent = getIntent();
                intent.putExtra("result", addr);
                setResult(RESULT_OK, intent);
                finish();
                break;

        }
    }
}
