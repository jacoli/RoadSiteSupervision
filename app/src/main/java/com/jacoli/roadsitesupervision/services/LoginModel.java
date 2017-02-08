package com.jacoli.roadsitesupervision.services;

import android.app.Activity;
import android.content.SharedPreferences;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by lichuange on 16/3/27.
 */
public class LoginModel extends MsgResponseBase implements Serializable {
    private String token;
    private String ExpirDate;
    private String ProjectID;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpirDate() {
        return ExpirDate;
    }

    public void setExpirDate(String expirDate) {
        ExpirDate = expirDate;
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public boolean isLoginSuccess() {
        return getStatus() == 0
                && getToken().length() > 0;
    }
}