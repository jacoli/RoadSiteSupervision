package com.jacoli.roadsitesupervision.services;

import java.util.List;

/**
 * Created by lichuange on 17/2/11.
 */

public class OperatorListModel extends MsgResponseBase {
    // SYRY表示试验人员姓名数组
    private List<String> SYRY;

    // ZJY表示质检员姓名数组
    private List<String> ZJY;

    // ZZAQY表示专职安全员姓名数组
    private List<String> ZZAQY;


    public List<String> getSYRY() {
        return SYRY;
    }

    public void setSYRY(List<String> SYRY) {
        this.SYRY = SYRY;
    }

    public List<String> getZJY() {
        return ZJY;
    }

    public void setZJY(List<String> ZJY) {
        this.ZJY = ZJY;
    }

    public List<String> getZZAQY() {
        return ZZAQY;
    }

    public void setZZAQY(List<String> ZZAQY) {
        this.ZZAQY = ZZAQY;
    }
}
