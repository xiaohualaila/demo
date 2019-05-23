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

public class CourierAdapter extends RecyclerView.Adapter<CourierAdapter.ViewHolder>
        implements AddDeleteView.OnAddDelClickListener {
    private List<Produces> list;

    public CourierAdapter(List<Produces> list) {
        this.list = list;
    }

    @Override
    public CourierAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courier_item, parent, false);
        ViewHolder viewHolder = new CourierAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CourierAdapter.ViewHolder holder, int position) {
        Produces produces = list.get(position);
        holder.time.setText(produces.getTime());
        holder.tv_state.setText(produces.getAddress());
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

        TextView tv_state,time;
        ViewHolder(View itemView) {
            super(itemView);
            tv_state = itemView.findViewById(R.id.tv_state);
            time = itemView.findViewById(R.id.time);
        }
    }

}
