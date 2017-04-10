package com.jacoli.roadsitesupervision.services;

import java.util.List;

/**
 * Created by lichuange on 17/3/26.
 */

public class AssignedMatterDetailModel extends MsgResponseBase {
    private String Subject;
    private String SenderName;
    private String ReceiverName;
    private String AddTime;
    private String AssignContent;
    private List<ImageUrlModel> PhotoList;
    private List<Reply> Reply;

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

    public String getAssignContent() {
        return AssignContent;
    }

    public void setAssignContent(String assignContent) {
        AssignContent = assignContent;
    }

    public List<ImageUrlModel> getPhotoList() {
        return PhotoList;
    }

    public void setPhotoList(List<ImageUrlModel> photoList) {
        PhotoList = photoList;
    }

    public List<AssignedMatterDetailModel.Reply> getReply() {
        return Reply;
    }

    public void setReply(List<AssignedMatterDetailModel.Reply> reply) {
        Reply = reply;
    }

    public class Reply {
        private String ReplyName;
        private String AddTime;
        private String AssignContent;
        private List<ImageUrlModel> PhotoList;

        public String getReplyName() {
            return ReplyName;
        }

        public void setReplyName(String replyName) {
            ReplyName = replyName;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String addTime) {
            AddTime = addTime;
        }

        public String getAssignContent() {
            return AssignContent;
        }

        public void setAssignContent(String assignContent) {
            AssignContent = assignContent;
        }

        public List<ImageUrlModel> getPhotoList() {
            return PhotoList;
        }

        public void setPhotoList(List<ImageUrlModel> photoList) {
            PhotoList = photoList;
        }
    }
}
