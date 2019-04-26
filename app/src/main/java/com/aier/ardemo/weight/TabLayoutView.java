package com.aier.ardemo.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aier.ardemo.R;

import butterknife.ButterKnife;


public class TabLayoutView extends LinearLayout implements View.OnClickListener,TabLayout.OnTabSelectedListener{
    private int before_state = 1;
    private LinearLayout ll_style,ll_material;
    private TabLayout tab;
    private TextView tv_black_style,tv_white_style,tv_red_style,tv_child_style,
            tv_middle_style,tv_youth_style,tv_old_style;

    private TabCallBack TabCallBack;
    public void setBottomCallBack(TabCallBack TabCallBack) {
        this.TabCallBack = TabCallBack;
    }

    public TabLayoutView(Context context) {
        super(context);
    }

    public TabLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.tablayout_view, this);
        tab = view.findViewById(R.id.tab);
        tab.addTab(tab.newTab().setText("材料"));
        tab.addTab(tab.newTab().setText("款式"));
        tab.addOnTabSelectedListener(this);

        ll_style = findViewById(R.id.ll_style);
        ll_material = findViewById(R.id.ll_material);
        tv_black_style = findViewById(R.id.tv_black_style);
        tv_white_style = findViewById(R.id.tv_white_style);
        tv_red_style = findViewById(R.id.tv_red_style);

        tv_child_style = findViewById(R.id.tv_child_style);
        tv_middle_style = findViewById(R.id.tv_middle_style);
        tv_youth_style = findViewById(R.id.tv_youth_style);
        tv_old_style = findViewById(R.id.tv_old_style);

        tv_black_style.setOnClickListener(this);
        tv_white_style.setOnClickListener(this);
        tv_red_style.setOnClickListener(this);
        tv_child_style.setOnClickListener(this);
        tv_middle_style.setOnClickListener(this);
        tv_youth_style.setOnClickListener(this);
        tv_old_style.setOnClickListener(this);
        tv_black_style.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_black_style:
                setBottomViewState(1);
                break;
            case R.id.tv_white_style:
                setBottomViewState(2);
                break;
            case R.id.tv_red_style:
                setBottomViewState(3);
                break;
            case R.id.tv_child_style:
                setBottomViewState(4);
                break;
            case R.id.tv_middle_style:
                setBottomViewState(5);
                break;
            case R.id.tv_youth_style:
                setBottomViewState(6);
                break;
            case R.id.tv_old_style:
                setBottomViewState(7);
                break;
        }
    }

    private void setBottomViewState(int flag) {
        if (before_state != flag) {
            if (flag == 1) {
                tv_black_style.setTextColor(getResources().getColor(R.color.white));
                TabCallBack.setCallBack(1);
            } else if (flag == 2) {
                tv_white_style.setTextColor(getResources().getColor(R.color.white));
                TabCallBack.setCallBack(2);
            } else if (flag == 3) {
                tv_red_style.setTextColor(getResources().getColor(R.color.white));
                TabCallBack.setCallBack(3);
            }
            else if (flag == 4) {
                tv_child_style.setTextColor(getResources().getColor(R.color.white));
                TabCallBack.setCallBack(4);
            } else if (flag == 5) {
                tv_middle_style.setTextColor(getResources().getColor(R.color.white));
                TabCallBack.setCallBack(5);
            } else if (flag == 6) {
                tv_youth_style.setTextColor(getResources().getColor(R.color.white));
                TabCallBack.setCallBack(6);
            }else if (flag == 7) {
                tv_old_style.setTextColor(getResources().getColor(R.color.white));
                TabCallBack.setCallBack(7);
            }

            if (before_state == 1) {
                tv_black_style.setTextColor(getResources().getColor(R.color.tab_bg));
            } else if (before_state == 2) {
                tv_white_style.setTextColor(getResources().getColor(R.color.tab_bg));
            } else if (before_state == 3) {
                tv_red_style.setTextColor(getResources().getColor(R.color.tab_bg));
            } else if (before_state == 4) {
                tv_child_style.setTextColor(getResources().getColor(R.color.tab_bg));
            } else if (before_state == 5) {
                tv_middle_style.setTextColor(getResources().getColor(R.color.tab_bg));
            } else if (before_state == 6) {
                tv_youth_style.setTextColor(getResources().getColor(R.color.tab_bg));
            } else if (before_state == 7) {
                tv_old_style.setTextColor(getResources().getColor(R.color.tab_bg));
            }
            before_state = flag;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int positon = tab.getPosition();
        Log.i("sss","sss"+positon);
        if(positon==0){
            ll_style.setVisibility(GONE);
            ll_material.setVisibility(VISIBLE);
            setBottomViewState(4);
        }else {
            ll_style.setVisibility(VISIBLE);
            ll_material.setVisibility(GONE);
            setBottomViewState(1);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public interface TabCallBack{
        void setCallBack(int num);
    }

}
