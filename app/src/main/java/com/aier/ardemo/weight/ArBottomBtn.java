package com.aier.ardemo.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aier.ardemo.R;


public class ArBottomBtn extends LinearLayout implements View.OnClickListener{
    private int before_state = 1;
    private ARBottomCallBack bottomCallBack;
    private TextView tv_buy_zhen,tv_find_similar,tv_fast;
    public void setARBottomCallBack(ARBottomCallBack bottomCallBack) {
        this.bottomCallBack = bottomCallBack;
    }

    public ArBottomBtn(Context context) {
        super(context);
    }

    public ArBottomBtn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
      View view = LayoutInflater.from(context).inflate(R.layout.ar_bottom_view, this);

        tv_buy_zhen = view.findViewById(R.id.tv_buy_zhen);//买真品
        tv_find_similar = view.findViewById(R.id.tv_find_similar);//找相似
        tv_fast = view.findViewById(R.id.tv_fast);//立即购买

        tv_buy_zhen.setOnClickListener(this);
        tv_find_similar.setOnClickListener(this);
        tv_fast.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_buy_zhen:
                setBottomViewState(8);
                break;
            case R.id.tv_find_similar:
                setBottomViewState(9);
                break;
            case R.id.tv_fast:
                setBottomViewState(10);
                break;

        }
    }

    private void setBottomViewState(int mAccount) {
        if (before_state != mAccount) {
            if (mAccount == 8) {

//                tv_buy_zhen.setTextColor(getResources().getColor(R.color.colorPrimary));
//                tv_find_similar.setTextColor(getResources().getColor(R.color.tab_bg));
//                tv_fast.setTextColor(getResources().getColor(R.color.tab_bg));
                bottomCallBack.setCallBack(8);
            } else if (mAccount == 9) {
                bottomCallBack.setCallBack(9);
            }else if (mAccount == 10) {
                bottomCallBack.setCallBack(10);
            }
            before_state = mAccount;
        }
    }
    public interface ARBottomCallBack{
        void setCallBack(int num);
    }
}
