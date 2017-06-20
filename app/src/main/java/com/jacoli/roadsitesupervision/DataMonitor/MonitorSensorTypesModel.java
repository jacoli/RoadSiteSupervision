package com.jacoli.roadsitesupervision.DataMonitor;

import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;

import java.util.List;

/**
 * Created by lichuange on 2017/6/20.
 */

public class MonitorSensorTypesModel extends ResponseBase {
    private List<SensorType> items;

    public List<SensorType> getItems() {
        return items;
    }

    public void setItems(List<SensorType> items) {
        this.items = items;
    }

    public class SensorType {
        private String ID;
        private String Name;

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
    }
}
