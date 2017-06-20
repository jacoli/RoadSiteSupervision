package com.jacoli.roadsitesupervision.DataMonitor;

import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.services.Utils;

import java.util.List;

/**
 * Created by lichuange on 2017/6/20.
 */

public class MonitorSensorListModel extends ResponseBase {
    private List<Sensor> items;

    public List<Sensor> getItems() {
        return items;
    }

    public void setItems(List<Sensor> items) {
        this.items = items;
    }

    public class Sensor {
        private String ID;
        private String SensorCode;
        private String Power;
        private String Value1;
        private String Value2;
        private String Value3;
        private String AddTime;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getSensorCode() {
            return SensorCode;
        }

        public void setSensorCode(String sensorCode) {
            SensorCode = sensorCode;
        }

        public String getPower() {
            return Power;
        }

        public void setPower(String power) {
            Power = power;
        }

        public String getValue1() {
            return Value1;
        }

        public void setValue1(String value1) {
            Value1 = value1;
        }

        public String getValue2() {
            return Value2;
        }

        public void setValue2(String value2) {
            Value2 = value2;
        }

        public String getValue3() {
            return Value3;
        }

        public void setValue3(String value3) {
            Value3 = value3;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String addTime) {
            AddTime = addTime;
        }

        public String getPowerStr() {
            if (Utils.isStringEmpty(getPower())) {
                return "";
            } else {
                return getPower() + "%";
            }
        }

        public String getValStr() {
            String val = "";
            if (!Utils.isStringEmpty(getValue1())) {
                val += getValue1();
            }

            if (!Utils.isStringEmpty(getValue2())) {
                val += ", " + getValue2();
            }

            if (!Utils.isStringEmpty(getValue3())) {
                val += ", " + getValue3();
            }

            return val;
        }
    }
}
