package com.aier.ardemo.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.aier.ardemo.R;


/**
 * Created by Administrator on 2018/2/25.
 */

public class DialogSelectFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {
    private static final String TYPE = "type";
    private static final String AGE = "age";
    private OnDialogButtonClickListener buttonClickListner;

    private int type = 1;
    private int age = 1;
    private int type_ = 1;
    private int age_ = 1;
    private TextView type_black;
    private TextView type_whiter;
    private TextView type_red;

    private TextView children;
    private TextView young;
    private TextView middle;
    private TextView old;

    //实现回调功能
    public interface OnDialogButtonClickListener {
         void buttonClick(int type,int age);

    }
    public static DialogSelectFragment getInstance(int type,int age) {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        bundle.putInt(AGE, age);
        DialogSelectFragment versionDialogFragment = new DialogSelectFragment();
        versionDialogFragment.setArguments(bundle);
        return versionDialogFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowManager = getActivity().getWindowManager();
            windowManager.getDefaultDisplay().getMetrics(dm);
            Window win = getDialog().getWindow();
            if (win != null) {
                win.setLayout((dm.widthPixels * 1), ViewGroup.LayoutParams.WRAP_CONTENT);
                win.setBackgroundDrawableResource(android.R.color.transparent);
                WindowManager.LayoutParams params = win.getAttributes();
                params.gravity = Gravity.BOTTOM;
                win.setAttributes(params);
            }
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle.getInt(TYPE);
        age = bundle.getInt(AGE);
    }

    public void show(FragmentManager fragmentManager,OnDialogButtonClickListener buttonClickListner) {
        this.buttonClickListner = buttonClickListner;
        show(fragmentManager, "VersionDialogFragment");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_select, null);
        TextView btn = view.findViewById(R.id.btn);
         type_black = view.findViewById(R.id.type_black);
         type_whiter = view.findViewById(R.id.type_whiter);
         type_red = view.findViewById(R.id.type_red);

         children = view.findViewById(R.id.children);
         young = view.findViewById(R.id.young);
         middle = view.findViewById(R.id.middle);
         old = view.findViewById(R.id.old);

        type_black.setOnClickListener(this);
        type_whiter.setOnClickListener(this);
        type_red.setOnClickListener(this);
        children.setOnClickListener(this);
        young.setOnClickListener(this);
        middle.setOnClickListener(this);
        old.setOnClickListener(this);
        btn.setOnClickListener(this);

        if(type == 1){
            type_black.setTextColor(getResources().getColor(R.color.blue));
        }else if(type == 2){
            type_whiter.setTextColor(getResources().getColor(R.color.blue));
        }else {
            type_red.setTextColor(getResources().getColor(R.color.blue));
        }

        if(age == 1){
            children.setTextColor(getResources().getColor(R.color.blue));
        }else if(age == 2){
            young.setTextColor(getResources().getColor(R.color.blue));
        }else if(age == 3){
            middle.setTextColor(getResources().getColor(R.color.blue));
        }else {
            old.setTextColor(getResources().getColor(R.color.blue));
        }

//        tv_colse.setOnClickListener(v -> buttonClickListner.cancelButtonClick());


        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.type_black:
                type = 1;
                selectType(type);
                break;
            case R.id.type_whiter:
                type = 2;
                selectType(type);
                break;
            case R.id.type_red:
                type = 3;
                selectType(type);
                break;

            case R.id.children:
                age = 1;
                selectAge(age);
                break;
            case R.id.young:
                age = 2;
                selectAge(age);
                break;
            case R.id.middle:
                age = 3;
                selectAge(age);
                break;
            case R.id.old:
                age = 4;
                selectAge(age);
                break;
            case R.id.btn:
                buttonClickListner.buttonClick(type,age);
                break;
        }

    }

    private void selectAge(int age) {
        if(age==age_){
            return;
        }
        if(age == 1){
            children.setTextColor(getResources().getColor(R.color.blue));
        }else if(age == 2){
            young.setTextColor(getResources().getColor(R.color.blue));
        }else if(age == 3){
            middle.setTextColor(getResources().getColor(R.color.blue));
        }else {
            old.setTextColor(getResources().getColor(R.color.blue));
        }
        if(age_ == 1){
            children.setTextColor(getResources().getColor(R.color.gray_1));
        }else if(age_ == 2){
            young.setTextColor(getResources().getColor(R.color.gray_1));
        }else if(age_ == 3){
            middle.setTextColor(getResources().getColor(R.color.gray_1));
        }else {
            old.setTextColor(getResources().getColor(R.color.gray_1));
        }
        age_ =age;
    }

    private void selectType(int type) {
        if(type==type_){
           return;
        }
        if(type == 1){
            type_black.setTextColor(getResources().getColor(R.color.blue));
        }else if(type == 2){
            type_whiter.setTextColor(getResources().getColor(R.color.blue));
        }else {
            type_red.setTextColor(getResources().getColor(R.color.blue));
        }

        if(type_ == 1){
            type_black.setTextColor(getResources().getColor(R.color.gray_1));
        }else if(type_ == 2){
            type_whiter.setTextColor(getResources().getColor(R.color.gray_1));
        }else {
            type_red.setTextColor(getResources().getColor(R.color.gray_1));
        }
        type_ =type;
    }

}
