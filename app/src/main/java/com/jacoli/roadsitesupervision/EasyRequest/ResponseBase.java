package com.jacoli.roadsitesupervision.EasyRequest;

/**
 * Created by lichuange on 17/4/3.
 */

public class ResponseBase {
    private int Status;
    private String Msg;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public boolean isSuccess() {
        return Status == 0;
    }
}
