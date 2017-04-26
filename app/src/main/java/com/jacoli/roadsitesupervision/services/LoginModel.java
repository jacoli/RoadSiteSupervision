package com.jacoli.roadsitesupervision.services;

import java.io.Serializable;

/**
 * Created by lichuange on 16/3/27.
 */
public class LoginModel extends MsgResponseBase implements Serializable {
    private String token;
    private String ID;
    private String Name;
    private String ExpirDate;
    private String ProjectID;
    private boolean PZ; // 旁站权限
    private boolean Patrol; // 巡视权限
    private boolean Check; // 抽检权限
    private boolean SituationReport;
    private boolean AssignedMatter;
    private boolean SupervisionOrder;
    private boolean SupervisionLog;
    private boolean ProgressCheck;
    private boolean SupervisionCheck;

    // 登录是否成功
    public boolean isLoginSuccess() {
        return getStatus() == 0
                && getToken().length() > 0;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public boolean isSituationReport() {
        return SituationReport;
    }

    public void setSituationReport(boolean situationReport) {
        SituationReport = situationReport;
    }

    public boolean isAssignedMatter() {
        return AssignedMatter;
    }

    public void setAssignedMatter(boolean assignedMatter) {
        AssignedMatter = assignedMatter;
    }

    public boolean isSupervisionOrder() {
        return SupervisionOrder;
    }

    public void setSupervisionOrder(boolean supervisionOrder) {
        SupervisionOrder = supervisionOrder;
    }

    public boolean isSupervisionLog() {
        return SupervisionLog;
    }

    public void setSupervisionLog(boolean supervisionLog) {
        SupervisionLog = supervisionLog;
    }
}