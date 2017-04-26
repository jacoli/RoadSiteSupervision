package com.jacoli.roadsitesupervision.services;

/**
 * Created by lichuange on 2017/4/18.
 */

public class UpgradeModel extends MsgResponseBase {
    private String Ver;
    private String Update;
    private String URL;

    public String getVer() {
        return Ver;
    }

    public void setVer(String ver) {
        Ver = ver;
    }

    public String getUpdate() {
        return Update;
    }

    public void setUpdate(String update) {
        Update = update;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
