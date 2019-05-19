package com.aier.ardemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aier.ardemo.R;
import com.aier.ardemo.bean.GroupChatDB;
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            if (isComMsg) {
                //如果是对方发来的消息，则显示的是左气泡
                convertView = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
            } else {
                //如果是自己发出的消息，则显示的是右气泡
                convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);
            }
            viewHolder = new ViewHolder();
            viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
            viewHolder.iv_userhead = (ImageView) convertView.findViewById(R.id.iv_userhead);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvSendTime.setText(groupChat.createtime);
        viewHolder.tvUserName.setText(groupChat.username);
        viewHolder.tvContent.setText(groupChat.message);
//        ImageUtils.imageCircle(mContext, BaseApiService.BASE_URL + groupChat.getHeadimg(), viewHolder.iv_userhead);
        return convertView;
    }

    //通过ViewHolder显示项的内容
    static class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public ImageView iv_userhead;
    }
}
