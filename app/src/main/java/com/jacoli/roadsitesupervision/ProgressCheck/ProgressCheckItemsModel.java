package com.jacoli.roadsitesupervision.ProgressCheck;

import com.jacoli.roadsitesupervision.services.MsgResponseBase;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichuange on 2017/5/2.
 */

public class ProgressCheckItemsModel extends MsgResponseBase {

    public class Item {
        private String ID;
        private String AddBy;
        private String AddTime;
        private String Part;
        private String ProjectName;
        private String ProcessName;
        private String ProcessCode;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getAddBy() {
            return AddBy;
        }

        public void setAddBy(String addBy) {
            AddBy = addBy;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String addTime) {
            AddTime = addTime;
        }

        public String getPart() {
            return Part;
        }

        public void setPart(String part) {
            Part = part;
        }

        public String getProjectName() {
            return ProjectName;
        }

        public void setProjectName(String projectName) {
            ProjectName = projectName;
        }

        public String getProcessName() {
            return ProcessName;
        }

        public void setProcessName(String processName) {
            ProcessName = processName;
        }

        public String getProcessCode() {
            return ProcessCode;
        }

        public void setProcessCode(String processCode) {
            ProcessCode = processCode;
        }
    }

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}