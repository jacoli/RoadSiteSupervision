package com.jacoli.roadsitesupervision.services;

import java.util.List;

/**
 * Created by lichuange on 17/2/11.
 */

public class ComponentDetailModel extends MsgResponseBase {

    public class PZModel {
        //
        private String ID;

        //
        private String PZName;

        // Status表示状态，0 未归档，1 已归档
        private int Status;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getPZName() {
            return PZName;
        }

        public void setPZName(String PZName) {
            this.PZName = PZName;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int status) {
            Status = status;
        }
    }

    private List<PZModel> items;

    public List<PZModel> getItems() {
        return items;
    }

    public void setItems(List<PZModel> items) {
        this.items = items;
    }
}
