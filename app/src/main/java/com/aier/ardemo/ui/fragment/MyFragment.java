package com.aier.ardemo.ui.fragment;


import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.aier.ardemo.ui.activity.OrderInfoActivity;
import com.aier.ardemo.ui.activity.PersonInfoActivity;
import com.aier.ardemo.ui.base.BaseFragment;
import com.aier.ardemo.R;
import com.aier.ardemo.weight.RoundImageView;

import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment {
    @BindView(R.id.my_photo)
    RoundImageView my_photo;
    private static String path = "/sdcard/myHead/";// sd路径

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void init() {

    }

    private void showHeadPic() {
        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            my_photo.setImageDrawable(drawable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showHeadPic();
    }

    @OnClick({R.id.rl_order,R.id.rl_address,R.id.rl_vr,R.id.rl_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_order:
                startActivity(new Intent(getActivity(), OrderInfoActivity.class));
                break;
            case R.id.rl_address:

                break;
            case R.id.rl_vr:

                break;
            case R.id.rl_account:
               startActivity(new Intent(getActivity(), PersonInfoActivity.class));
                break;
        }

    }

    @Override
    protected ViewModel initViewModel() {
        return null;
    }
}
