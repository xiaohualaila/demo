package com.aier.ardemo.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aier.ardemo.R;


public class AddShoppingBtn extends LinearLayout implements View.OnClickListener {

    private AddShoppingCallBack callBack;
    private TextView shpping_num;
    private LinearLayout ll_shopping;

    public void setCallBack(AddShoppingCallBack callBack) {
        this.callBack = callBack;
    }

    public AddShoppingBtn(Context context) {
        super(context);
    }

    public AddShoppingBtn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.add_shopping_view, this);

        ll_shopping = view.findViewById(R.id.ll_shopping);
        shpping_num = view.findViewById(R.id.shpping_num);
        ll_shopping.setOnClickListener(this);
        shpping_num.setOnClickListener(this);
    }

    /**
     * 对外提供设置EditText值的方法
     */
    public void setNumber(int number) {
        shpping_num.setText(number + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_shopping:
                callBack.setCallBack();
                break;
        }
    }

    public interface AddShoppingCallBack {
        void setCallBack();
    }
}