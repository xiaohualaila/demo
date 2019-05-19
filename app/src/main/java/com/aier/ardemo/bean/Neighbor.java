package com.aier.ardemo.bean;

import java.io.Serializable;

/**
 * author : Rain
 * time : 2017/10/25 0025
 * explain :
 */

public class Neighbor implements Serializable {
    private String nid;

    private String nimage;

    private String nname;

    private String cid;

    private Integer totalnumber;

    private String createtime;

    private String remark;

    private String status;


    @Override
    public String toString() {
        return "Neighbor{" +
                "nid='" + nid + '\'' +
                ", nimage='" + nimage + '\'' +
                ", nname='" + nname + '\'' +
                ", cid='" + cid + '\'' +
                ", totalnumber=" + totalnumber +
                ", createtime='" + createtime + '\'' +
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getNimage() {
        return nimage;
    }

    public void setNimage(String nimage) {
        this.nimage = nimage;
    }

    public String getNname() {
        return nname;
    }

    public void setNname(String nname) {
        this.nname = nname;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Integer getTotalnumber() {
        return totalnumber;
    }

    public void setTotalnumber(Integer totalnumber) {
        this.totalnumber = totalnumber;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
