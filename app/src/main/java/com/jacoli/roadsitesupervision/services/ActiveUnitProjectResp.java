package com.jacoli.roadsitesupervision.services;

/**
 * Created by lichuange on 17/2/11.
 */

public class ActiveUnitProjectResp extends MsgResponseBase {
    // 构件ID
    private String ID;

    // 施工进度，0表示未开工，1表示正在施工，2表示施工完成
    private int Progress;

    // 旁站状态，0表示未旁站，2表示旁站完成
    private int PZStatus;

    // 抽检状态，0表示抽检未完成，2表示抽检完成
    private String CheckStatus;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getProgress() {
        return Progress;
    }

    public void setProgress(int progress) {
        Progress = progress;
    }

    public int getPZStatus() {
        return PZStatus;
    }

    public void setPZStatus(int PZStatus) {
        this.PZStatus = PZStatus;
    }

    public String getCheckStatus() {
        return CheckStatus;
    }

    public void setCheckStatus(String checkStatus) {
        CheckStatus = checkStatus;
    }
}
