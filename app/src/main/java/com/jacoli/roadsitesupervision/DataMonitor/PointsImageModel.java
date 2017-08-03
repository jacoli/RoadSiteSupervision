package com.jacoli.roadsitesupervision.DataMonitor;

import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;

/**
 * Created by lichuange on 2017/8/3.
 */

public class PointsImageModel extends ResponseBase {
    private String PointPic;

    public String getPointPic() {
        return PointPic;
    }

    public void setPointPic(String pointPic) {
        PointPic = pointPic;
    }
}
