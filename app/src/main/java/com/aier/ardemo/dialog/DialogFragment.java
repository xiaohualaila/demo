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
import android.widget.TextView;
import com.aier.ardemo.R;


/**
 * Created by Administrator on 2018/2/25.
 */

public class DialogFragment extends android.support.v4.app.DialogFragment {

    private OnDialogClickListener buttonClickListner;
    //实现回调功能
    public interface OnDialogClickListener {
         void cancelButtonClick();

    }
    public static DialogFragment getInstance() {
        DialogFragment versionDialogFragment = new DialogFragment();
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
             //   win.setLayout((dm.widthPixels * 1), ViewGroup.LayoutParams.WRAP_CONTENT);
                win.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                win.setBackgroundDrawableResource(android.R.color.transparent);
                WindowManager.LayoutParams params = win.getAttributes();
                params.gravity = Gravity.CENTER;
                win.setAttributes(params);
            }
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void show(FragmentManager fragmentManager,OnDialogClickListener buttonClickListner) {
        this.buttonClickListner = buttonClickListner;
        show(fragmentManager, "VersionDialogFragment");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_voice, null);
        TextView btn = view.findViewById(R.id.btn);
        btn.setOnClickListener(v -> buttonClickListner.cancelButtonClick());
        builder.setView(view);
        return builder.create();
    }

}
