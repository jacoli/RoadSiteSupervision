package com.jacoli.roadsitesupervision.SupervisionPatrol;

import com.jacoli.roadsitesupervision.services.MsgResponseBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichuange on 2017/4/22.
 */

public class CheckItemsModel extends MsgResponseBase {
    public class Item implements Serializable {
        private String ID;
        private String Ordinal;
        private String Name;
        private String Remark;
        private String Code;
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

        public String getCode() {
            return Code;
        }

        public void setCode(String code) {
            Code = code;
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

        // 是否多页选择
        public boolean shouldMultiPageSelection() {
            return getLevel() > 2;
        }
    }

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getCheckTypes() {
        return getItems();
    }

    public String[] getCheckTypeDescriptions() {
        final List<CheckItemsModel.Item> items = getCheckTypes();
        List<String> typeNames = new ArrayList<>();
        for (CheckItemsModel.Item item : items) {
            typeNames.add(item.getName());
        }
        String[] strArr = new String[typeNames.size()];
        typeNames.toArray(strArr);

        return strArr;
    }
}
