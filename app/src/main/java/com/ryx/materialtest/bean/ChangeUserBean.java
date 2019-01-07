package com.ryx.materialtest.bean;

import java.io.Serializable;

/**
 * Created by Ryx on 2019/1/7.
 */
public class ChangeUserBean implements Serializable {
    private String nameinfo;
    private String maileInfo;

    public String getMaileInfo() {
        return maileInfo;
    }

    public void setMaileInfo(String maileInfo) {
        this.maileInfo = maileInfo;
    }

    public ChangeUserBean(String nameinfo, String maileInfo) {
        this.nameinfo = nameinfo;
        this.maileInfo = maileInfo;
    }

    public ChangeUserBean(String nameinfo) {
        this.nameinfo = nameinfo;
    }

    public ChangeUserBean() {
    }

    public String getNameinfo() {
        return nameinfo;
    }

    public void setNameinfo(String nameinfo) {
        this.nameinfo = nameinfo;
    }
}
