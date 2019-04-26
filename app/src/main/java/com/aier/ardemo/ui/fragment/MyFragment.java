package com.aier.ardemo.ui.fragment;


import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.aier.ardemo.bean.GloData;
import com.aier.ardemo.bean.Person;
import com.aier.ardemo.ui.activity.AddressActivity;
import com.aier.ardemo.ui.activity.OrderActivity;
import com.aier.ardemo.ui.activity.PersonInfoActivity;
import com.aier.ardemo.ui.base.BaseFragment;
import com.aier.ardemo.R;
import com.aier.ardemo.utils.AdjustBitmap;
import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment {
    @BindView(R.id.my_photo)
    ImageView my_photo;
    @BindView(R.id.tv_name)
    TextView tv_name;

    private static String path = "/sdcard/myHead/";// sd路径
    Person person;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void init() {

    }

    private void showHeadPic() {
        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            Bitmap pic = AdjustBitmap.getCircleBitmap(bt);
            my_photo.setImageBitmap(pic);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showHeadPic();
        person = GloData.getPerson();
        if(TextUtils.isEmpty(person.getUsername())){
            tv_name.setText("");
        }else {
            tv_name.setText(person.getUsername());
        }
    }

    @OnClick({R.id.rl_order,R.id.rl_address,R.id.rl_vr,R.id.rl_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_order:
                startActivity(new Intent(getActivity(), OrderActivity.class));
                break;
            case R.id.rl_address:
                startActivity(new Intent(getActivity(), AddressActivity.class));
                break;
            case R.id.rl_vr:

                break;
            case R.id.rl_account:
               startActivity(new Intent(getActivity(), PersonInfoActivity.class));
                break;
        }

    }

    @Override
    protected ViewModel initViewModel() {
        return null;
    }
}
