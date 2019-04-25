package com.aier.ardemo.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aier.ardemo.R;

public class AddDeleteView extends LinearLayout {

    private OnAddDelClickListener listener;

    private TextView tv_number;

    public void setOnAddDelClickListener(OnAddDelClickListener listener) { 
        if (listener != null) {
            this.listener = listener; }
    }

    public interface OnAddDelClickListener{
        void onAddClick(View v);
        void onDelClick(View v);
    }

    public AddDeleteView(Context context) {
        this(context,null);
    }

    public AddDeleteView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AddDeleteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr); initView(context,attrs,defStyleAttr);
    }
    private void initView(Context context,AttributeSet attrs,int defStyleAttr){
        View.inflate(context, R.layout.add_delete,this);
        TextView but_add = findViewById(R.id.tv_add);
        TextView but_delete = findViewById(R.id.tv_delete);
        tv_number = findViewById(R.id.tv_num);
        tv_number.setText("1");
        but_add.setOnClickListener(view -> listener.onAddClick(view));
        but_delete.setOnClickListener(view -> listener.onDelClick(view));
    } /**
     * 对外提供设置EditText值的方法
     */
    public void setNumber(int number){
        if (number>0){
            tv_number.setText(number+"");
        }
    } /**
     * 得到控件原来的值
     */
    public int getNumber(){ 
        int number = 0; 
        try {
            String numberStr = tv_number.getText().toString().trim(); 
            number = Integer.valueOf(numberStr);
        } catch (Exception e) { 
            number = 0;
        }
        return number; 
    }

}
