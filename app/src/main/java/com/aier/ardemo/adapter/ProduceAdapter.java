package com.aier.ardemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aier.ardemo.R;
import com.aier.ardemo.model.Produces;
import com.aier.ardemo.weight.AddDeleteView;

import java.util.List;

public class ProduceAdapter extends RecyclerView.Adapter<ProduceAdapter.ViewHolder>
        implements AddDeleteView.OnAddDelClickListener {
    private List<Produces> list;

    public ProduceAdapter(List<Produces> list) {
        this.list = list;
    }

    @Override
    public ProduceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.production_item, parent, false);
        ViewHolder viewHolder = new ProduceAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProduceAdapter.ViewHolder holder, int position) {
        Produces produces = list.get(position);
        if(position==0){
            holder.iv.setImageResource(R.drawable.round_blue);
        }else {
            holder.iv.setImageResource(R.drawable.round_white);
            holder.tv_addr.setBackgroundResource(R.drawable.left_line_gray);
        }
        holder.tv_time.setText(produces.getTime());
        holder.tv_addr.setText(produces.getAddress());
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
        ImageView iv;
        TextView tv_time,tv_addr;
        ViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_addr = itemView.findViewById(R.id.tv_addr);
        }
    }

}
