package com.jacoli.roadsitesupervision.SupervisionPatrol;

import com.jacoli.roadsitesupervision.services.MsgResponseBase;
import java.util.List;

/**
 * Created by lichuange on 2017/4/22.
 */

public class SupervisionPatrolListModel extends MsgResponseBase {
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public class Item {
        private String ID;
        private String ProjectName;
        private String ProjectPart;
        private String AddBy;
        private String AddTime;
        private String LastUpdateBy;
        private String LastUpdateTime;
        private String Status;
        private int SupervisionCheckStatus;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getProjectName() {
            return ProjectName;
        }

        public void setProjectName(String projectName) {
            ProjectName = projectName;
        }

        public String getProjectPart() {
            return ProjectPart;
        }

        public void setProjectPart(String projectPart) {
            ProjectPart = projectPart;
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

        public String getLastUpdateBy() {
            return LastUpdateBy;
        }

        public void setLastUpdateBy(String lastUpdateBy) {
            LastUpdateBy = lastUpdateBy;
        }

        public String getLastUpdateTime() {
            return LastUpdateTime;
        }

        public void setLastUpdateTime(String lastUpdateTime) {
            LastUpdateTime = lastUpdateTime;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public int getSupervisionCheckStatus() {
            return SupervisionCheckStatus;
        }

        public void setSupervisionCheckStatus(int supervisionCheckStatus) {
            SupervisionCheckStatus = supervisionCheckStatus;
        }
    }
}