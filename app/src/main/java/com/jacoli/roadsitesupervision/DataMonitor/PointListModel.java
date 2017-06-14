package com.jacoli.roadsitesupervision.DataMonitor;

import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.services.Utils;

import java.util.List;

/**
 * Created by lichuange on 2017/6/14.
 */

public class PointListModel extends ResponseBase {
    public List<Point> getItems() {
        return items;
    }

    public void setItems(List<Point> items) {
        this.items = items;
    }

    private List<Point> items;

    public class Point {
        private String PointName;
        private String PointCode;
        private String MonitorTypeName;
        private String RealTimeVal1;
        private String RealTimeVal2;
        private String RealTimeVal3;
        private String Sunit;

        public String getPointName() {
            return PointName;
        }

        public void setPointName(String pointName) {
            PointName = pointName;
        }

        public String getPointCode() {
            return PointCode;
        }

        public void setPointCode(String pointCode) {
            PointCode = pointCode;
        }

        public String getMonitorTypeName() {
            return MonitorTypeName;
        }

        public void setMonitorTypeName(String monitorTypeName) {
            MonitorTypeName = monitorTypeName;
        }

        public String getRealTimeVal1() {
            return RealTimeVal1;
        }

        public void setRealTimeVal1(String realTimeVal1) {
            RealTimeVal1 = realTimeVal1;
        }

        public String getRealTimeVal2() {
            return RealTimeVal2;
        }

        public void setRealTimeVal2(String realTimeVal2) {
            RealTimeVal2 = realTimeVal2;
        }

        public String getRealTimeVal3() {
            return RealTimeVal3;
        }

        public void setRealTimeVal3(String realTimeVal13) {
            RealTimeVal3 = realTimeVal13;
        }

        public String getSunit() {
            return Sunit;
        }

        public void setSunit(String sunit) {
            Sunit = sunit;
        }


        public String getValStr() {
            String val = "";
            if (!Utils.isStringEmpty(getRealTimeVal1())) {
                val += getRealTimeVal1() + getSunit();
            }

            if (!Utils.isStringEmpty(getRealTimeVal2())) {
                val += ", " + getRealTimeVal2() + getSunit();
            }

            if (!Utils.isStringEmpty(getRealTimeVal3())) {
                val += ", " + getRealTimeVal3() + getSunit();
            }

            return val;
        }
    }
}