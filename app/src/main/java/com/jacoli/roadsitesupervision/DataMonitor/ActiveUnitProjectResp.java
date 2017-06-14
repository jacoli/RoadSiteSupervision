package com.jacoli.roadsitesupervision.DataMonitor;

import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;

/**
 * Created by lichuange on 2017/6/14.
 */

public class ActiveUnitProjectResp extends ResponseBase {
    // 施工进度，0表示未开工，1表示正在施工，2表示施工完成
    private int Progress;

    public int getProgress() {
        return Progress;
    }

    public void setProgress(int progress) {
        Progress = progress;
    }
}
