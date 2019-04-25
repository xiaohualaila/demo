package com.aier.ardemo.ui.fragment;


import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.view.View;

import com.aier.ardemo.ui.activity.OrderInfoActivity;
import com.aier.ardemo.ui.base.BaseFragment;
import com.aier.ardemo.R;
import butterknife.OnClick;

public class MyFragment extends BaseFragment {



    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void init() {

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

                break;
        }

    }

    @Override
    protected ViewModel initViewModel() {
        return null;
    }
}
