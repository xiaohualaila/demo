package com.aier.ardemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.model.Goods;
import com.aier.ardemo.weight.AddDeleteView;
import java.util.List;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {
    private List<Goods> list;
    public ShoppingAdapter(List<Goods> list) {
        this.list = list;
    }
    private int Total = 0;
    @Override
    public ShoppingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
        ViewHolder viewHolder = new ShoppingAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShoppingAdapter.ViewHolder holder, int position) {
        if(position==0){
            Total=0;
        }
        Goods goods = list.get(position);
        if(goods.isBuy()){
            Total += goods.getPrice();
            holder.iv_choose.setImageResource(R.drawable.success_pic);
        }else {
            holder.iv_choose.setImageResource(R.drawable.not_buy);
        }

        holder.tv_name.setText(goods.getName());
        holder.tv_color.setText(goods.getColor());
        holder.tv_amount.setText("￥"+goods.getPrice());

        holder.iv_choose.setOnClickListener(v -> {
            if (goods.isBuy()){
                holder.iv_choose.setImageResource(R.drawable.not_buy);
                goods.setBuy(false);
                int num = holder.add_dele_btn.getNumber();

                 int amount =num*goods.getPrice();
                 Total = Total - amount;
                 listener.onTotalAmount(Total);
                 holder.add_dele_btn.setNumber(1);

            }else {
                holder.iv_choose.setImageResource(R.drawable.success_pic);
                goods.setBuy(true);

                Total = Total + goods.getPrice();
                listener.onTotalAmount(Total);

            }
        });
        holder.add_dele_btn.setOnAddDelClickListener(new AddDeleteView.OnAddDelClickListener() {
            @Override
            public void onAddClick(View v) {
                if(!goods.isBuy()){
                    return;
                }
                int origin = holder.add_dele_btn.getNumber();
                    origin++;
                    holder.add_dele_btn.setNumber(origin);
                    if(origin!=1){
                        Total +=  goods.getPrice();
                        listener.onTotalAmount(Total);
                    }
            }


            @Override
            public void onDelClick(View v) {
                if(!goods.isBuy()){
                    return;
                }
                int origin = holder.add_dele_btn.getNumber();
                if(origin>1){
                    origin--;
                    holder.add_dele_btn.setNumber(origin);
                    Total -=  goods.getPrice();
                    listener.onTotalAmount(Total);
                }
            }
        });
        listener.onTotalAmount(Total);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_color,tv_amount;
        AddDeleteView add_dele_btn;
        ImageView iv_choose;
        ViewHolder(View itemView) {
            super(itemView);
            iv_choose = itemView.findViewById(R.id.iv_choose);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_color = itemView.findViewById(R.id.tv_color);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            add_dele_btn = itemView.findViewById(R.id.add_dele_btn);
        }
    }


    private BackTotalAmountClick listener;

    public void setBackTotalAmountClick(BackTotalAmountClick listener) {
        if (listener != null) {
            this.listener = listener; }
    }

    public interface BackTotalAmountClick{
        void onTotalAmount(int amount);
    }


}
