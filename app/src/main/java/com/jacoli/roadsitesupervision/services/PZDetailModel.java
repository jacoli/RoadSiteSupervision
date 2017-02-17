package com.jacoli.roadsitesupervision.services;

import java.util.List;

/**
 * Created by lichuange on 17/2/11.
 */

public class PZDetailModel extends MsgResponseBase {

    private String ZJY;

    private String ZZAQY;

    private String SYRY;

    // Items表示旁站表格内容数据
    private List<PZRowModel> items;

    public String getZJY() {
        return ZJY;
    }

    public void setZJY(String ZJY) {
        this.ZJY = ZJY;
    }

    public String getZZAQY() {
        return ZZAQY;
    }

    public void setZZAQY(String ZZAQY) {
        this.ZZAQY = ZZAQY;
    }

    public String getSYRY() {
        return SYRY;
    }

    public void setSYRY(String SYRY) {
        this.SYRY = SYRY;
    }

    public List<PZRowModel> getItems() {
        return items;
    }

    public void setItems(List<PZRowModel> items) {
        this.items = items;
    }

    public class PZRowModel {
        private String ID;
        private String Ordinal;
        private String Name;
        private String SubName;

        // 数值单位或描述等
        private String Description;

        // DataFormat数据格式，0表示int数值型，1表示double数值型，2表示勾选，3表示文本字符串，4表示int数组，5表示double数组
        private String DataFormat;

        // ArrayMinNum表示数组的最小个数，仅DataFormat为数组有效
        private String ArrayMinNum;

        // ArrayMaxNum表示数组的最大个数，仅DataFormat为数组有效
        private String ArrayMaxNum;

        // Value表示旁站表格单项的值
        private String Value;

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

        public String getSubName() {
            return SubName;
        }

        public void setSubName(String subName) {
            SubName = subName;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getDataFormat() {
            return DataFormat;
        }

        public void setDataFormat(String dataFormat) {
            DataFormat = dataFormat;
        }

        public String getArrayMinNum() {
            return ArrayMinNum;
        }

        public void setArrayMinNum(String arrayMinNum) {
            ArrayMinNum = arrayMinNum;
        }

        public String getArrayMaxNum() {
            return ArrayMaxNum;
        }

        public void setArrayMaxNum(String arrayMaxNum) {
            ArrayMaxNum = arrayMaxNum;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String value) {
            Value = value;
        }
    }
}