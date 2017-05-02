package com.jacoli.roadsitesupervision.ProgressCheck;

import com.jacoli.roadsitesupervision.services.MsgResponseBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichuange on 2017/5/2.
 */

public class ProgressItemsModel extends MsgResponseBase {

    public class Item {
        private String ID;
        private String Ordinal;
        private String Name;
        private String Remark;
        private int Level;
        private List<Item> items;

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

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public int getLevel() {
            return Level;
        }

        public void setLevel(int level) {
            Level = level;
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public List<Item> getFlatItems() {
            List<Item> flatItems = new ArrayList<>();

            flatItems.add(this);

            if (getItems() != null) {
                for (Item subItem : getItems()) {
                    List<Item> items = subItem.getFlatItems();
                    for (Item item : items) {
                        flatItems.add(item);
                    }
                }
            }

            return flatItems;
        }
    }

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getFlatSubItems() {
        List<Item> flatItems = new ArrayList<>();

        if (getItems() != null) {
            for (Item subItem : getItems()) {
                List<Item> items = subItem.getFlatItems();
                for (Item item : items) {
                    flatItems.add(item);
                }
            }
        }

        return flatItems;
    }
}
