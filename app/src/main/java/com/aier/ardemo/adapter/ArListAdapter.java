package com.aier.ardemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.bean.DataBean;
import com.aier.ardemo.utils.ImageUtils;
import java.util.ArrayList;
import java.util.List;

public class ArListAdapter extends RecyclerView.Adapter<ArListAdapter.ViewHolder> {
    private List<DataBean> list;
    private Context mContext;

    public ArListAdapter(Context context, List<DataBean> list) {
        this.list = list == null ? new ArrayList<>() : list;
        this.mContext = context;
    }

    private OnItemClickListener onItemClickListener;

    //setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ArListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ar_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArListAdapter.ViewHolder holder, int position) {
        DataBean arModel = list.get(position);
        String img_url = arModel.getIcon();
        String ar_name = arModel.getTitle();
        if(img_url == null||img_url.isEmpty()){
            holder.item_img.setImageResource(R.drawable.no_ar);
        }else {
            ImageUtils.imageCircle(mContext, img_url, holder.item_img);
        }

        if(ar_name!=null){
            holder.item_title.setText(ar_name);
        }

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(list.get(position));
            }
        });
    }

    public void setListData(List<DataBean> mData) {
        list.clear();
        this.list = mData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        TextView item_title;

        ViewHolder(View itemView) {
            super(itemView);
            item_img = itemView.findViewById(R.id.item_img);
            item_title = itemView.findViewById(R.id.item_title);
        }
    }

    //回调接口
    public interface OnItemClickListener {
        void onItemClick(DataBean ar);
    }

}
