package com.jacoli.roadsitesupervision.DataMonitor;

import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.services.Utils;

import java.util.List;

/**
 * Created by lichuange on 2017/6/14.
 */

public class ProjectDetailModel extends ResponseBase {
    // 单位工程
    public class UnitProjectModel {

        // 构件ID
        private String ID;

        // 序号
        private int Ordinal;

        // 构件名称
        private String Name;

        // 构建编码
        private String Code;

        // 施工进度，0表示未开工，1表示正在施工，2表示施工完成
        private int Progress;

        private boolean IsAlarm;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public int getOrdinal() {
            return Ordinal;
        }

        public void setOrdinal(int ordinal) {
            Ordinal = ordinal;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getCode() {
            return Code;
        }

        public void setCode(String code) {
            Code = code;
        }

        public int getProgress() {
            return Progress;
        }

        public void setProgress(int progress) {
            Progress = progress;
        }

        public boolean isAlarm() {
            return IsAlarm;
        }

        public void setAlarm(boolean alarm) {
            IsAlarm = alarm;
        }
    }

    // 单位工程列表
    private List<UnitProjectModel> items;

    public List<UnitProjectModel> getItems() {
        return items;
    }

    public void setItems(List<UnitProjectModel> items) {
        this.items = items;
    }

    // apis
    public void setUnitProjectProgress(String id, int progress) {
        if (Utils.isStringEmpty(id)) {
            return;
        }

        for (UnitProjectModel unitProjectModel: getItems()) {
            if (unitProjectModel.getID().equals(id)) {
                unitProjectModel.setProgress(progress);
            }
        }
    }
}
