package com.jacoli.roadsitesupervision.SupervisionPatrol;

import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.MsgResponseBase;
import com.jacoli.roadsitesupervision.services.Utils;

import java.util.ArrayList;
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

//        // 临时方案，根据tag返回审批人列表
//        String tag = "JL01";
//        String userName = MainService.getInstance().getLoginModel().getName();
//        if (!Utils.isStringEmpty(userName)) {
//            if (userName.contains("JL01")) {
//                tag = "JL01";
//            }
//            if (userName.contains("JL02")) {
//                tag = "JL02";
//            }
//            if (userName.contains("JL03")) {
//                tag = "JL03";
//            }
//            if (userName.contains("JL04")) {
//                tag = "JL04";
//            }
//            if (userName.contains("JL05")) {
//                tag = "JL05";
//            }
//            if (userName.contains("JL06")) {
//                tag = "JL06";
//            }
//        }
//
//        List<Item> processedItems = new ArrayList<>();
//
//        for (Item item: items) {
//            if (item.getName().contains(tag)) {
//                processedItems.add(item);
//            }
//        }
//
//        return processedItems;
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
