package com.aier.ardemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aier.ardemo.R;
import com.aier.ardemo.bean.GroupChatDB;
import com.aier.ardemo.utils.AdjustBitmap;
import com.aier.ardemo.utils.ImageUtils;
import com.aier.ardemo.weight.CircleImageView;

import java.util.List;

/**
 * Created by Rain on 2017/10/26.
 */

public class ChatGroupAdapter extends BaseAdapter {

    //ListView视图的内容由IMsgViewType决定
    public static interface IMsgViewType {
        //对方发来的信息
        int IMVT_COM_MSG = 0;
        //自己发出的信息
        int IMVT_TO_MSG = 1;
    }

    private Context mContext;
    private List<GroupChatDB> list;
    private LayoutInflater mInflater;

    public ChatGroupAdapter(Context mContext, List<GroupChatDB> list) {
        this.mContext = mContext;
        this.list = list;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setList(List<GroupChatDB> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //获取项的类型
    public int getItemViewType(int position) {
        GroupChatDB groupChat = list.get(position);
        if (groupChat.isMsgType())
            return IMsgViewType.IMVT_TO_MSG;
        return IMsgViewType.IMVT_COM_MSG;

    }

    //获取项的类型数
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 2;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        GroupChatDB groupChat = list.get(position);
        boolean isComMsg = groupChat.isMsgType();

        if (isComMsg) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
                viewHolder = new ViewHolder();
                viewHolder.tvSendTime =  convertView.findViewById(R.id.tv_sendtime);
                viewHolder.tvUserName =  convertView.findViewById(R.id.tv_username);
                viewHolder.tvContent =  convertView.findViewById(R.id.tv_chatcontent);
                viewHolder.iv_userhead =  convertView.findViewById(R.id.iv_userhead);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvSendTime.setText(groupChat.createtime);
            viewHolder.tvUserName.setText(groupChat.username);
            viewHolder.tvContent.setText(groupChat.message);
//            ImageUtils.imageCircle(mContext, groupChat.headimg, viewHolder.iv_userhead);
            Bitmap bt = BitmapFactory.decodeFile(groupChat.headimg);// 从SD卡中找头像，转换成Bitmap
            Log.i("sss","图片地址"+groupChat.headimg);
            if (bt != null) {
                Bitmap pic = AdjustBitmap.getCircleBitmap(bt);
                viewHolder.iv_userhead.setImageBitmap(pic);
            }
        } else {

            ViewHolderLeft viewHolderLeft;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);
                viewHolderLeft = new ViewHolderLeft();
                viewHolderLeft.tvSendTime =  convertView.findViewById(R.id.tv_sendtime);
                viewHolderLeft.tvUserName =  convertView.findViewById(R.id.tv_username);
                viewHolderLeft.tvContent =  convertView.findViewById(R.id.tv_chatcontent);
                viewHolderLeft.iv_userhead =  convertView.findViewById(R.id.iv_userhead);
                viewHolderLeft.iv_image =  convertView.findViewById(R.id.img);
                convertView.setTag(viewHolderLeft);
            }else {
                viewHolderLeft = (ViewHolderLeft) convertView.getTag();
            }

            viewHolderLeft.tvSendTime.setText(groupChat.createtime);
            viewHolderLeft.tvUserName.setText(groupChat.username);
            viewHolderLeft.tvContent.setText(groupChat.message);
            viewHolderLeft.iv_userhead.setImageResource(R.drawable.xiaobai);
            if(!TextUtils.isEmpty(groupChat.image)){
                Log.i("sss","图片地址"+groupChat.image);
                ImageUtils.image(mContext,groupChat.image, viewHolderLeft.iv_image);
            }
        }
        return convertView;
    }

    //通过ViewHolder显示项的内容
    static class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public ImageView iv_userhead;
    }

    //通过ViewHolder显示项的内容
    static class ViewHolderLeft {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public CircleImageView iv_userhead;
        public ImageView iv_image;
    }
}
