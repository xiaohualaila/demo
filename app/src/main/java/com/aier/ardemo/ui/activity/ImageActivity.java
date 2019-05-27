package com.aier.ardemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import com.aier.ardemo.R;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.weight.ZoomImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/4/19.
 */

public class ImageActivity extends BaseActivity {
    @BindView(R.id.img)
    ZoomImageView img;

    private  String urlString;


    private void getPic(String url, final ZoomImageView img) {
        Glide.with(this)
                .load(url)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new ImageViewTarget<GlideDrawable>(img) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        img.setImageDrawable(resource);
                    }
                });
    }

    @OnClick({R.id.btn_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    protected void initDate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        urlString =intent.getStringExtra("photoUrl");
    }

    @Override
    protected void initViews() {
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        getPic(urlString,img);
    }

    @Override
    protected int getLayout() {
        return  R.layout.dialog_img;
    }
}
