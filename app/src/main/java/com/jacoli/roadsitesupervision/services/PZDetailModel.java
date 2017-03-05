package com.jacoli.roadsitesupervision.services;

import android.util.Log;

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

        public void setMultiValue(String value, int index) {
            int maxNum = 0;
            String[] values = {};
            try {
                maxNum = Integer.parseInt(getArrayMaxNum());
                values = getValue().split(Utils.MultipartSeparator);
            }
            catch (Exception ex) {
                Log.e("", ex.toString());
            }

            if (index < maxNum) {
                String newValue = "";

                for (int i = 0; i < maxNum; ++i) {
                    if (i == index) {
                        newValue += value;
                    }
                    else {
                        if (i < values.length) {
                            newValue += values[i];
                        }
                        else {
                            newValue += "";
                        }
                    }

                    if (i < maxNum - 1) {
                        newValue += Utils.MultipartSeparator;
                    }
                }

                setValue(newValue);
            }
        }

        static final int SubValueIndexMax = 2;
        static final String SubValueSeparator = "/";

        public String getSubValueAtIndexBelowThree(int index) {
            if (index < 0 || index > SubValueIndexMax) {
                return "";
            }

            String[] values = {};
            try {
                values = getValue().split(SubValueSeparator);
            }
            catch (Exception ex) {
                Log.e("", ex.toString());
            }

            if (index < values.length) {
                return values[index];
            }
            else {
                return "";
            }
        }

        public void setSubValueAtIndexBelowThree(String value, int index) {
            if (index < 0 || index > SubValueIndexMax) {
                return;
            }

            String[] values = {};
            try {
                values = getValue().split(SubValueSeparator);
            }
            catch (Exception ex) {
                Log.e("", ex.toString());
            }

            String newValue = "";

            for (int i = 0; i < SubValueIndexMax + 1; ++i) {
                if (i == index) {
                    newValue += value;
                }
                else {
                    if (i < values.length) {
                        newValue += values[i];
                    }
                    else {
                        newValue += "";
                    }
                }

                if (i < SubValueIndexMax) {
                    newValue += SubValueSeparator;
                }
            }

            setValue(newValue);
        }
    }
}
