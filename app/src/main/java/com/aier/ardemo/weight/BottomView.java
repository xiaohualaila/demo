package com.aier.ardemo.weight;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aier.ardemo.R;


public class BottomView extends LinearLayout implements View.OnClickListener{
    private ImageView yuyin;
    private int before_state = 1;
    private BottomCallBack bottomCallBack;
    private TextView iv_home_text,iv_my_text;
    public void setBottomCallBack(BottomCallBack bottomCallBack) {
        this.bottomCallBack = bottomCallBack;
    }

    public BottomView(Context context) {
        super(context);
    }

    public BottomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
      View view = LayoutInflater.from(context).inflate(R.layout.bottom_view, this);

        iv_home_text = view.findViewById(R.id.iv_home_text);
        iv_my_text = view.findViewById(R.id.iv_my_text);
        yuyin = view.findViewById(R.id.iv_yuyin);
        iv_home_text.setOnClickListener(this);
        iv_my_text.setOnClickListener(this);
        yuyin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.iv_home_text:
                setBottomViewState(1);
                break;
            case R.id.iv_my_text:
                setBottomViewState(2);
                break;
            case R.id.iv_yuyin:
                bottomCallBack.setTabCallBack(3);
                break;
        }
    }

    private void setBottomViewState(int mAccount) {
        if (before_state != mAccount) {
            if (mAccount == 1) {

                iv_home_text.setTextColor(getResources().getColor(R.color.blue));
                Drawable drawable_home_= getResources().getDrawable(R.drawable.home_);
                drawable_home_.setBounds(0,0,drawable_home_.getMinimumWidth(),drawable_home_.getMinimumHeight());
                iv_home_text.setCompoundDrawables(null,drawable_home_,null,null);

                iv_my_text.setTextColor(getResources().getColor(R.color.tab_bg));
                Drawable drawable_my = getResources().getDrawable(R.drawable.my);
                drawable_my.setBounds(0,0,drawable_my.getMinimumWidth(),drawable_my.getMinimumHeight());
                iv_my_text.setCompoundDrawables(null,drawable_my,null,null);
                bottomCallBack.setTabCallBack(1);
            } else if (mAccount == 2) {
                bottomCallBack.setTabCallBack(2);
                iv_home_text.setTextColor(getResources().getColor(R.color.tab_bg));

                Drawable drawable_home = getResources().getDrawable(R.drawable.home);
                drawable_home.setBounds(0,0,drawable_home.getMinimumWidth(),drawable_home.getMinimumHeight());

                iv_home_text.setCompoundDrawables(null,drawable_home,null,null);
                iv_my_text.setTextColor(getResources().getColor(R.color.blue));
                Drawable drawable_my_ = getResources().getDrawable(R.drawable.my_);
                drawable_my_.setBounds(0,0,drawable_my_.getMinimumWidth(),drawable_my_.getMinimumHeight());
                iv_my_text.setCompoundDrawables(null,drawable_my_,null,null);
            }

            before_state = mAccount;
        }
    }
    public interface BottomCallBack{
        void setTabCallBack(int num);
    }
}
