package com.aier.ardemo.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.ui.base.BaseActivity;
import java.lang.ref.WeakReference;
import butterknife.BindView;
import butterknife.OnClick;

public class PaySuccessActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    private Handler handler = new Handler();

    @Override
    protected void initViews() {
        tv_title.setText("支付结果");
        tv_right.setVisibility(View.VISIBLE);
        MyHandler myHandler=new MyHandler(this);
        myHandler.postDelayed(() -> finish(), 5000);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_pay;
    }



    @OnClick({R.id.iv_back,R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private static class MyHandler extends Handler{
        private WeakReference<PaySuccessActivity> mActivity;

        public MyHandler(PaySuccessActivity activity){
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }


}
