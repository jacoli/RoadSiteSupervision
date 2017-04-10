package com.jacoli.roadsitesupervision.services;

import android.widget.ListView;

import java.util.List;

/**
 * Created by lichuange on 17/4/3.
 */

public class StaffListModel extends MsgResponseBase {
    public class Staff {
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

    private List<Staff> items;

    public List<Staff> getItems() {
        return items;
    }

    public void setItems(List<Staff> items) {
        this.items = items;
    }

    public String getStaffName(String staffId) {
        if (!Utils.isStringEmpty(staffId) && getItems() != null) {
            for (Staff staff : getItems()) {
                if (staffId.equals(staff.getID())) {
                    return staff.getName();
                }
            }
        }

        return null;
    }
}
