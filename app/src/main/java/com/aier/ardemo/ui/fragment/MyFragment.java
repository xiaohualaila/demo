package com.aier.ardemo.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aier.ardemo.bean.GloData;
import com.aier.ardemo.bean.Person;
import com.aier.ardemo.ui.activity.PersonInfoActivity;
import com.aier.ardemo.ui.base.BaseFragment;
import com.aier.ardemo.R;
import com.aier.ardemo.utils.AdjustBitmap;
import com.aier.ardemo.utils.SharedPreferencesUtil;
import com.karics.library.zxing.android.CaptureActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.baidu.speech.audio.MicrophoneServer.TAG;

public class MyFragment extends BaseFragment {
    @BindView(R.id.my_photo)
    ImageView my_photo;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_bind_phone)
    TextView tv_bind_phone;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    boolean isBind = false;

    private static String path = "/sdcard/myHead/";// sd路径
    Person person;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void init() {
        isBind = SharedPreferencesUtil.getBoolean(getActivity(),"bind",false);
        if(isBind){
            tv_bind_phone.setBackgroundDrawable(getResources().getDrawable(R.drawable.vr_gray_btn));
            tv_bind_phone.setText("取消绑定");
            img.setVisibility(View.VISIBLE);
            tv_no_data.setVisibility(View.GONE);
        }else {
            tv_bind_phone.setBackgroundDrawable(getResources().getDrawable(R.drawable.vr_blue_btn));
            tv_bind_phone.setText("绑定手机");
            img.setVisibility(View.GONE);
            tv_no_data.setVisibility(View.VISIBLE);
        }
    }

    private void showHeadPic() {
        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            Bitmap pic = AdjustBitmap.getCircleBitmap(bt);
            my_photo.setImageBitmap(pic);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showHeadPic();
        person = GloData.getPerson();
        if(TextUtils.isEmpty(person.getUsername())){
            tv_name.setText("");
        }else {
            tv_name.setText(person.getUsername());
        }
    }

    @OnClick({R.id.tv_setup,R.id.tv_bind_phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_setup:
                startActivity(new Intent(getActivity(), PersonInfoActivity.class));
                break;
            case R.id.tv_bind_phone:
                if(!isBind){
                    Intent intent = new Intent(getActivity(), CaptureActivity.class);
                    intent.putExtra("from","bind");
                    startActivityForResult(intent,123);
                }else {
                    showToast("取消绑定成功！");
                    tv_bind_phone.setBackgroundDrawable(getResources().getDrawable(R.drawable.vr_blue_btn));
                    tv_bind_phone.setText("绑定手机");
                    isBind = false;
                    img.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data==null)
            return;
        String result = data.getStringExtra("result");

        Log.i("sss","result"+ result);
        showToast("绑定成功！");
        tv_bind_phone.setBackgroundDrawable(getResources().getDrawable(R.drawable.vr_gray_btn));
        tv_bind_phone.setText("取消绑定");
        SharedPreferencesUtil.putBoolean(getActivity(),"bind",true);
        isBind = true;
        img.setVisibility(View.VISIBLE);
        tv_no_data.setVisibility(View.GONE);
    }

}
