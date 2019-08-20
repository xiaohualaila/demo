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
import com.aier.ardemo.utils.SharedPreferencesUtil;
import com.aier.ardemo.weight.AddDeleteView;
import com.google.gson.Gson;

import java.util.List;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {
    private List<DataBean> list;
    private Context mContext;
    private int shopping_num;

    public ShoppingAdapter(List<DataBean> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    private double Total = 0;

    @Override
    public ShoppingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
        ViewHolder viewHolder = new ShoppingAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShoppingAdapter.ViewHolder holder, int position) {
        shopping_num = SharedPreferencesUtil.getInt(mContext, "shoppingData", "shopping_num", 0);

        if (position == 0) {
            Total = 0;
        }
        DataBean goods = list.get(position);
        if (goods.isBuy()) {
            Total += goods.getPrice() * goods.getNum();
            holder.iv_choose.setImageResource(R.drawable.success_pic);
        } else {
            holder.iv_choose.setImageResource(R.drawable.not_buy);
        }
        ImageUtils.image(mContext, goods.getIcon(), holder.iv_goods_img);

        holder.tv_name.setText(goods.getTitle());
        holder.tv_desp.setText(goods.getDesp());
        holder.tv_amount.setText("￥" + goods.getPrice());
        holder.add_dele_btn.setNumber(goods.getNum());
        holder.iv_choose.setOnClickListener(v -> {
            if (goods.isBuy()) {
                holder.iv_choose.setImageResource(R.drawable.not_buy);
                goods.setBuy(false);
                int num = holder.add_dele_btn.getNumber();
                double amount = num * goods.getPrice();
                Total = Total - amount;
                shopping_num = shopping_num - goods.getNum();
                SharedPreferencesUtil.putString(mContext, "shoppingData", "shoppings", new Gson().toJson(list));
                listener.onTotalAmount(Total,shopping_num);
            } else {
                holder.iv_choose.setImageResource(R.drawable.success_pic);
                goods.setBuy(true);
                Total = Total + goods.getPrice() * goods.getNum();
                shopping_num = shopping_num + goods.getNum();
                SharedPreferencesUtil.putString(mContext, "shoppingData", "shoppings", new Gson().toJson(list));
                listener.onTotalAmount(Total,shopping_num);
            }
        });
        holder.add_dele_btn.setOnAddDelClickListener(new AddDeleteView.OnAddDelClickListener() {
            @Override
            public void onAddClick(View v) {
                if (!goods.isBuy()) {
                    return;
                }
                int origin = holder.add_dele_btn.getNumber();
                origin++;
                holder.add_dele_btn.setNumber(origin);
                Total += goods.getPrice();
                goods.setNum(origin);
                shopping_num++;
                SharedPreferencesUtil.putString(mContext, "shoppingData", "shoppings", new Gson().toJson(list));
                listener.onTotalAmount(Total,shopping_num);
            }

            @Override
            public void onDelClick(View v) {
                if (!goods.isBuy()) {
                    return;
                }
                int origin = holder.add_dele_btn.getNumber();
                if (origin > 1) {
                    origin--;
                    holder.add_dele_btn.setNumber(origin);
                    Total -= goods.getPrice();
                    goods.setNum(origin);
                    shopping_num--;
                    SharedPreferencesUtil.putString(mContext, "shoppingData", "shoppings", new Gson().toJson(list));
                    listener.onTotalAmount(Total,shopping_num);
                }
            }
        });
        listener.onTotalAmount(Total, shopping_num);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_desp, tv_amount;
        AddDeleteView add_dele_btn;
        ImageView iv_choose, iv_goods_img;

        ViewHolder(View itemView) {
            super(itemView);
            iv_goods_img = itemView.findViewById(R.id.iv_goods_img);
            iv_choose = itemView.findViewById(R.id.iv_choose);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_desp = itemView.findViewById(R.id.tv_desp);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            add_dele_btn = itemView.findViewById(R.id.add_dele_btn);
        }
    }


    private BackTotalAmountClick listener;

    public void setBackTotalAmountClick(BackTotalAmountClick listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }

    public interface BackTotalAmountClick {
        void onTotalAmount(double amount,int shopping_num);
    }
}
