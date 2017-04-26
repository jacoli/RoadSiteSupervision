package com.jacoli.roadsitesupervision.SupervisionPatrol;

import com.jacoli.roadsitesupervision.services.MsgResponseBase;

import java.util.List;

/**
 * Created by lichuange on 2017/4/22.
 */

public class ApproverListModel extends MsgResponseBase {

    public class Item {
        private String ID;
        private String Ordinal;
        private String Name;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getOrdinal() {
            return Ordinal;
        }

        public void setOrdinal(String ordinal) {
            Ordinal = ordinal;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getIDAtPosition(int position) {
        if (position >= 0 && getItems() != null && getItems().size() > position) {
            return getItems().get(position).getID();
        } else {
            return null;
        }
    }
}
