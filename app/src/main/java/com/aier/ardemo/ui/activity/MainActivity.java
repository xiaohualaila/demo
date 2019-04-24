package com.aier.ardemo.ui.activity;

import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.aier.ardemo.R;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.ui.fragment.HomeFragment;
import com.aier.ardemo.ui.fragment.MyFragment;
import com.aier.ardemo.weight.BottomView;
import com.baidu.ar.bean.DuMixARConfig;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements BottomView.BottomCallBack{
    @BindView(R.id.bottom_view)
    BottomView bottomView;
    @BindView(R.id.tv_title)
    TextView tv_title;

//    @BindView(R.id.rl_submit)
//    RelativeLayout rl_submit;
    private Fragment mCurrentFrag;
    private FragmentManager fm;
    private Fragment homeFragment;
    private Fragment myFragment;
    private boolean flag = true;
    @Override
    protected void initViews() {
        fm = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        myFragment = new MyFragment();
        switchContent(homeFragment);
        bottomView.setBottomCallBack(this);
    }

    @Override
    protected void initDate(Bundle savedInstanceState) {
        DuMixARConfig.setAppId("16021623"); // 设置App Id
        DuMixARConfig.setAPIKey("ZI0SDxDIWvtMnHvs2scKXC2x"); // 设置API Key
        DuMixARConfig.setSecretKey("ncNvjMB2QpFm6eaU9UGjkNxnk4oPxlIk");    // 设置Secret Key
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected ViewModel initViewModel() {

        return null;
    }


    @OnClick({R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:

//                if(flag){
//                    flag = false;
//                    rl_submit.setVisibility(View.VISIBLE);
//                }else {
//                    flag = true;
//                    rl_submit.setVisibility(View.GONE);
//                }
                Intent intent = new Intent(this, ARActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ar_key", "10301636");
                bundle.putInt("ar_type", 5);
                bundle.putString("ar_path", "");
                bundle.putString("name", "白色椅子");
                bundle.putString("description", "");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void setCallBack(int num) {
        switch (num){
            case 1:
                switchContent(homeFragment);
                tv_title.setText("首页");
                break;
            case 2:
                switchContent(myFragment);
                tv_title.setText("我的");
                break;
            case 3:

                break;
            case 4:

                break;
        }
    }

    /**
     * 动态添加fragment，不会重复创建fragment
     *
     * @param to 将要加载的fragment
     */
    public void switchContent(Fragment to) {
        if (mCurrentFrag != to) {
            if (!to.isAdded()) {// 如果to fragment没有被add则增加一个fragment
                if (mCurrentFrag != null) {
                    fm.beginTransaction().hide(mCurrentFrag).commit();
                }
                fm.beginTransaction()
                        .add(R.id.fl_content, to)
                        .commit();
            } else {
                fm.beginTransaction().hide(mCurrentFrag).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mCurrentFrag = to;
        }
    }

}