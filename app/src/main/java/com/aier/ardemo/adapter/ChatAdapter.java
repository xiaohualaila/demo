package com.aier.ardemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.bean.GroupChatDB;
import com.aier.ardemo.utils.AdjustBitmap;
import com.aier.ardemo.utils.ImageUtils;
import com.aier.ardemo.weight.CircleImageView;
import com.aier.ardemo.weight.ScaleImageView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<GroupChatDB> list;
    Context context;
    public ChatAdapter(List<GroupChatDB> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        if(viewType==0){
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_item_msg_text_left, parent, false);
            return new ViewHolderLeft(view);
        }else {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_item_msg_text_right, parent, false);
            return new ViewHolderRight(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
         GroupChatDB groupChat = list.get(position);
         switch (getItemViewType(position)){
             case 0:
                 ViewHolderLeft holderLeft = (ViewHolderLeft) holder;
                 holderLeft.tvSendTime.setText(groupChat.createtime);
                 holderLeft.tvUserName.setText(groupChat.username);
                 holderLeft.tvContent.setText(groupChat.message);
                 holderLeft.iv_userhead.setImageResource(R.drawable.xiaobai);
                 if(!TextUtils.isEmpty(groupChat.image)){
                     Log.i("sss","图片地址"+groupChat.image);
//                     ImageUtils.image(context,groupChat.image, holderLeft.iv_image);
//                     holderLeft.iv_image.setVisibility(View.VISIBLE);

                     ScaleImageView imageView =holderLeft.iv_image;
                     imageView.setInitSize(imageView.getWidth(), imageView.getHeight());
                     ImageUtils.image(context,groupChat.image, holderLeft.iv_image);
                     holderLeft.iv_image.setVisibility(View.VISIBLE);
                 }else{
                     holderLeft.iv_image.setVisibility(View.GONE);
                 }
                 break;
             case 1:
                  ViewHolderRight holderRight = (ViewHolderRight) holder;
                 holderRight.tvSendTime.setText(groupChat.createtime);
                 holderRight.tvUserName.setText(groupChat.username);
                 holderRight.tvContent.setText(groupChat.message);
                 Bitmap bt = BitmapFactory.decodeFile(groupChat.headimg);// 从SD卡中找头像，转换成Bitmap
                 if (bt != null) {
                     Bitmap pic = AdjustBitmap.getCircleBitmap(bt);
                     holderRight.iv_userhead.setImageBitmap(pic);
                 }
                 break;
         }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        GroupChatDB groupChat = list.get(position);
        boolean isComMsg = groupChat.isMsgType();
        if (isComMsg) {
            return 1;
        } else {
            return 0;
        }
    }

    class ViewHolderLeft extends RecyclerView.ViewHolder{
        TextView tvSendTime,tvUserName,tvContent;
        CircleImageView iv_userhead;
        ScaleImageView iv_image;

        public ViewHolderLeft(View itemView) {
            super(itemView);
            tvSendTime =  itemView.findViewById(R.id.tv_sendtime);
            tvUserName =  itemView.findViewById(R.id.tv_username);
            tvContent =  itemView.findViewById(R.id.tv_chatcontent);
            iv_userhead =  itemView.findViewById(R.id.iv_userhead);
            iv_image =  itemView.findViewById(R.id.img);
        }
    }

     class ViewHolderRight extends RecyclerView.ViewHolder{
         TextView tvSendTime,tvUserName,tvContent;
         ImageView iv_userhead;

        public ViewHolderRight(View itemView) {
            super(itemView);
            tvSendTime =  itemView.findViewById(R.id.tv_sendtime);
            tvUserName =  itemView.findViewById(R.id.tv_username);
            tvContent =  itemView.findViewById(R.id.tv_chatcontent);
            iv_userhead =  itemView.findViewById(R.id.iv_userhead);
        }
    }
}
