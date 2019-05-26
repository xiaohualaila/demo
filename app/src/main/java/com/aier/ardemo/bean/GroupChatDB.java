package com.aier.ardemo.bean;

import com.aier.ardemo.dbflowdemo.AppDataBase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Rain on 2017/10/26.
 */
@Table(database = AppDataBase.class)
public class GroupChatDB  extends BaseModel  {

    @PrimaryKey(autoincrement = true)
    @Column
    public   int id;

    @Column
    public String message;

    @Column
    public String createtime;

    @Column
    public String username;

    @Column
    public String sex;

    @Column
    public String headimg;

    @Column
    public String image;

    @Column
    public String uid;

    @Column
    public int type;

//    public boolean isMsgType() {
//        String id = GloData.getPerson().getId()+"";
//        if (id.equals(uid))
//            return true;
//        return false;
//    }
}
