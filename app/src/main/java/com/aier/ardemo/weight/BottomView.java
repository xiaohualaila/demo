package com.aier.ardemo.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aier.ardemo.R;


public class BottomView extends LinearLayout implements View.OnClickListener{
    private ImageView home,my,yuyin;
    private int before_state = 1;
    private BottomCallBack bottomCallBack;
    private LinearLayout ll_home,ll_my;
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

        ll_home = view.findViewById(R.id.ll_home);
        ll_my = view.findViewById(R.id.ll_my);
        home = view.findViewById(R.id.iv_home);
        my = view.findViewById(R.id.iv_my);
        iv_home_text = view.findViewById(R.id.iv_home_text);
        iv_my_text = view.findViewById(R.id.iv_my_text);
        yuyin = view.findViewById(R.id.iv_yuyin);
        ll_home.setOnClickListener(this);
        ll_my.setOnClickListener(this);
        iv_home_text.setOnClickListener(this);
        iv_my_text.setOnClickListener(this);
        yuyin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_home:
                setBottomViewState(1);
                break;
            case R.id.ll_my:
                setBottomViewState(2);
                break;
//            case R.id.iv_home_text:
//                setBottomViewState(1);
//                break;
//            case R.id.iv_my_text:
//                setBottomViewState(2);
//                break;
            case R.id.iv_yuyin:
                bottomCallBack.setTabCallBack(3);
                break;
        }
    }

    private void setBottomViewState(int mAccount) {
        if (before_state != mAccount) {
            if (mAccount == 1) {
                home.setImageResource(R.drawable.home_);
                my.setImageResource(R.drawable.my);
                iv_home_text.setTextColor(getResources().getColor(R.color.colorPrimary));
                iv_my_text.setTextColor(getResources().getColor(R.color.tab_bg));
                bottomCallBack.setTabCallBack(1);
            } else if (mAccount == 2) {
                bottomCallBack.setTabCallBack(2);
                home.setImageResource(R.drawable.home);
                my.setImageResource(R.drawable.my_);
                iv_home_text.setTextColor(getResources().getColor(R.color.tab_bg));
                iv_my_text.setTextColor(getResources().getColor(R.color.colorPrimary));
            }

            before_state = mAccount;
        }
    }
    public interface BottomCallBack{
        void setTabCallBack(int num);
    }
}
