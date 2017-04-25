package com.jacoli.roadsitesupervision.SupervisionPatrol;

import com.jacoli.roadsitesupervision.services.ImageUrlModel;
import com.jacoli.roadsitesupervision.services.MsgResponseBase;
import java.util.List;

/**
 * Created by lichuange on 2017/4/23.
 */

public class DetailModel extends MsgResponseBase {
    private String ProjectPart;
    private String Description;
    private String AddByName;
    private String AddTime;
    private String ApprovalBy;
    private String ApprovalByName;
    private String ApprovalTime;
    private String ApprovalComment;
    private String ReceiverName;
    private String Progress;
    private List<Item> CheckType;
    private List<ImageUrlModel> PhotoList;
    private List<Reply> Reply;

    public String getProjectPart() {
        return ProjectPart;
    }

    public void setProjectPart(String projectPart) {
        ProjectPart = projectPart;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAddByName() {
        return AddByName;
    }

    public void setAddByName(String addByName) {
        AddByName = addByName;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }

    public String getApprovalBy() {
        return ApprovalBy;
    }

    public void setApprovalBy(String approvalBy) {
        ApprovalBy = approvalBy;
    }

    public String getApprovalByName() {
        return ApprovalByName;
    }

    public void setApprovalByName(String approvalByName) {
        ApprovalByName = approvalByName;
    }

    public String getApprovalTime() {
        return ApprovalTime;
    }

    public void setApprovalTime(String approvalTime) {
        ApprovalTime = approvalTime;
    }

    public String getApprovalComment() {
        return ApprovalComment;
    }

    public void setApprovalComment(String approvalComment) {
        ApprovalComment = approvalComment;
    }

    public String getReceiverName() {
        return ReceiverName;
    }

    public void setReceiverName(String receiverName) {
        ReceiverName = receiverName;
    }

    public String getProgress() {
        return Progress;
    }

    public void setProgress(String progress) {
        Progress = progress;
    }

    public List<Item> getCheckType() {
        return CheckType;
    }

    public void setCheckType(List<Item> checkType) {
        CheckType = checkType;
    }

    public List<ImageUrlModel> getPhotoList() {
        return PhotoList;
    }

    public void setPhotoList(List<ImageUrlModel> photoList) {
        PhotoList = photoList;
    }

    public List<DetailModel.Reply> getReply() {
        return Reply;
    }

    public void setReply(List<DetailModel.Reply> reply) {
        Reply = reply;
    }

    public class Reply {
        private String ReplyName;
        private String AddTime;
        private String ReplyContent;
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

        public String getReplyContent() {
            return ReplyContent;
        }

        public void setReplyContent(String replyContent) {
            ReplyContent = replyContent;
        }

        public List<ImageUrlModel> getPhotoList() {
            return PhotoList;
        }

        public void setPhotoList(List<ImageUrlModel> photoList) {
            PhotoList = photoList;
        }
    }

    public class Item {
        private String ID;
        private String Ordinal;
        private String Name;
        private String Remark;
        private String Code;
        private int Level;
        private boolean IsCheck;
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

        public boolean isCheck() {
            return IsCheck;
        }

        public void setCheck(boolean check) {
            IsCheck = check;
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public String getCheckItemsDescription() {
            String desc = "";
            if (getItems() != null && getItems().size() > 0) {

                for (Item item : getItems()) {
                    if (item.isCheck()) {
                        desc += item.getName() + "(否)\n";
                    } else {
                        desc += item.getCheckItemsDescription();
                    }
                }
            }

            return desc;
        }
    }

    public String getCheckTypeDescription() {
        if (getCheckType() != null && getCheckType().size() > 0) {
            return getCheckType().get(0).getName();
        } else {
            return "";
        }
    }

    public String getCheckItemsDescription() {
        String desc = "";
        if (getCheckType() != null && getCheckType().size() > 0) {
            for (Item item : getCheckType()) {
                desc += item.getCheckItemsDescription();
            }
        }

        return desc;
    }
}

