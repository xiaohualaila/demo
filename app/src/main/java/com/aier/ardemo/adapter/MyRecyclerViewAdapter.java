package com.aier.ardemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.model.Goods;
import com.aier.ardemo.weight.AddDeleteView;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>
        implements AddDeleteView.OnAddDelClickListener {
    private List<Goods> list;

    public MyRecyclerViewAdapter(List<Goods> list) {
        this.list = list;
    }

    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
        ViewHolder viewHolder = new MyRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.ViewHolder holder, int position) {
        Goods goods = list.get(position);
        holder.tv_name.setText(goods.getName());
        holder.tv_color.setText(goods.getColor());
        holder.tv_amount.setText("￥"+goods.getAmount());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAddClick(View v) {

    }

    @Override
    public void onDelClick(View v) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_color,tv_amount;
        AddDeleteView add_dele_btn;
        ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_color = itemView.findViewById(R.id.tv_color);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            add_dele_btn = itemView.findViewById(R.id.add_dele_btn);
        }
    }

}
