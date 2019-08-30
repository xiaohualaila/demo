package com.aier.ardemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.bean.DataBean;
import java.util.ArrayList;
import java.util.List;

public class ChildAdapter extends BaseAdapter {
    List<DataBean> mList = new ArrayList();
    private Context mContext;

    private MyOnItemClickListener myOnItemClickListener;

    public void setMyOnItemClickListener(MyOnItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }

    public ChildAdapter(Context context ) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return getCount() == 0 ? null : mList.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(List<DataBean> mList) {
        this.mList = mList;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.child_item, null);
            viewHolder = new ViewHolder();
            viewHolder.child_ar = view.findViewById(R.id.ar_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        DataBean bean = mList.get(i);
        if(bean!=null){
            viewHolder.child_ar.setText(bean.getMaterial());
        }

        viewHolder.child_ar.setOnClickListener(v -> {
            myOnItemClickListener.myOnClick(i);
        });
        return view;

    }

    public interface MyOnItemClickListener {
        void myOnClick(int position);
    }

    class ViewHolder {
        TextView child_ar;
    }



}
