package com.jacoli.roadsitesupervision.services;

import java.util.List;

/**
 * Created by lichuange on 17/3/26.
 */

public class MyAssignedMattersModel extends MsgResponseBase {

    public class Item {
        private String ID;
        private String Subject;
        private String SenderName;
        private String ReceiverName;
        private String AddTime;
        private String ReplyName;
        private String ReplyTime;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getSubject() {
            return Subject;
        }

        public void setSubject(String subject) {
            Subject = subject;
        }

        public String getSenderName() {
            return SenderName;
        }

        public void setSenderName(String senderName) {
            SenderName = senderName;
        }

        public String getReceiverName() {
            return ReceiverName;
        }

        public void setReceiverName(String receiverName) {
            ReceiverName = receiverName;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String addTime) {
            AddTime = addTime;
        }

        public String getReplyName() {
            return ReplyName;
        }

        public void setReplyName(String replyName) {
            ReplyName = replyName;
        }

        public String getReplyTime() {
            return ReplyTime;
        }

        public void setReplyTime(String replyTime) {
            ReplyTime = replyTime;
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