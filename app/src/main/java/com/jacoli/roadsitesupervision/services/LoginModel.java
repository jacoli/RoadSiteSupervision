package com.jacoli.roadsitesupervision.services;

import java.io.Serializable;

/**
 * Created by lichuange on 16/3/27.
 */
public class LoginModel extends MsgResponseBase implements Serializable {
    private String token;
    private String Name;
    private String ExpirDate;
    private String ProjectID;

    // 旁站权限
    private boolean PZ;

    // 巡视权限
    private boolean Patrol;

    // 抽检权限
    private boolean Check;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public boolean isPZ() {
        return PZ;
    }

    public void setPZ(boolean PZ) {
        this.PZ = PZ;
    }

    public boolean isPatrol() {
        return Patrol;
    }

    public void setPatrol(boolean patrol) {
        Patrol = patrol;
    }

    public boolean isCheck() {
        return Check;
    }

    public void setCheck(boolean check) {
        Check = check;
    }

    // 登录是否成功
    public boolean isLoginSuccess() {
        return getStatus() == 0
                && getToken().length() > 0;
    }
}