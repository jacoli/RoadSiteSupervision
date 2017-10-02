package com.jacoli.roadsitesupervision.DataMonitor;

import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;

import java.util.List;

/**
 * Created by lichuange on 2017/10/2.
 */

public class GetHistroySensorDataModel extends ResponseBase {
    private String PointName;
    private String EarlyWarningThreshold;
    private String Threshold;
    private List<Item> items;

    public String getPointName() {
        return PointName;
    }

    public void setPointName(String pointName) {
        PointName = pointName;
    }

    public String getEarlyWarningThreshold() {
        return EarlyWarningThreshold;
    }

    public void setEarlyWarningThreshold(String earlyWarningThreshold) {
        EarlyWarningThreshold = earlyWarningThreshold;
    }

    public String getThreshold() {
        return Threshold;
    }

    public void setThreshold(String threshold) {
        Threshold = threshold;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    class Item {
        private String SensorCode;
        private String Col1;
        private String Col2;
        private String Col3;
        private String Col4;
        private String Col5;
        private String AddTime;

        public String getSensorCode() {
            return SensorCode;
        }

        public void setSensorCode(String sensorCode) {
            SensorCode = sensorCode;
        }

        public String getCol1() {
            return Col1;
        }

        public void setCol1(String col1) {
            Col1 = col1;
        }

        public String getCol2() {
            return Col2;
        }

        public void setCol2(String col2) {
            Col2 = col2;
        }

        public String getCol3() {
            return Col3;
        }

        public void setCol3(String col3) {
            Col3 = col3;
        }

        public String getCol4() {
            return Col4;
        }

        public void setCol4(String col4) {
            Col4 = col4;
        }

        public String getCol5() {
            return Col5;
        }

        public void setCol5(String col5) {
            Col5 = col5;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String addTime) {
            AddTime = addTime;
        }
    }
}
