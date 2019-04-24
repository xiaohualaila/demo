package com.aier.ardemo.ui.fragment;


import android.arch.lifecycle.ViewModel;

import com.aier.ardemo.ui.base.BaseFragment;
import com.aier.ardemo.R;

public class MyFragment extends BaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void init() {

    }


    @Override
    protected ViewModel initViewModel() {
        return null;
    }
}
