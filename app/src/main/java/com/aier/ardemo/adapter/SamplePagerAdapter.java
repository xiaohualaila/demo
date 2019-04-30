package com.aier.ardemo.adapter;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aier.ardemo.R;


public class SamplePagerAdapter extends PagerAdapter {

    private int mSize;
    private int[] imageView = {R.drawable.guidance, R.drawable.guidance2,
            R.drawable.guidance3, R.drawable.guidance4};
    public static Handler mHandler;

    public SamplePagerAdapter() {
        mSize = imageView.length;
    }

    public SamplePagerAdapter(int count) {
        mSize = count;
    }

    @Override
    public int getCount() {
        return mSize;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup view, int position) {
        View v = LayoutInflater.from(view.getContext()).inflate(R.layout.layout_guilance, null);

        ImageView iv_backage = v.findViewById(R.id.iv_backage);
        TextView iv_button = v.findViewById(R.id.iv_button);
        iv_backage.setImageResource(imageView[position]);
        if (position == 3) {
            iv_button.setVisibility(View.VISIBLE);
        }
        iv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = Message.obtain();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        });
        view.addView(v, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return v;
    }
}