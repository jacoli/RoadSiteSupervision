package com.jacoli.roadsitesupervision.services;

import java.util.List;

/**
 * Created by lichuange on 17/2/10.
 */

// 项目详情
public class ProjectDetailModel extends MsgResponseBase {

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

        // 旁站状态，0表示未旁站，2表示旁站完成
        private int PZStatus;

        // 抽检状态，0表示抽检未完成，2表示抽检完成
        private String CheckStatus;

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

        public int getPZStatus() {
            return PZStatus;
        }

        public void setPZStatus(int PZStatus) {
            this.PZStatus = PZStatus;
        }

        public String getCheckStatus() {
            return CheckStatus;
        }

        public void setCheckStatus(String checkStatus) {
            CheckStatus = checkStatus;
        }
    }

    // 项目名称
    private String UnitProjectName;

    // 单位工程列表
    private List<UnitProjectModel> items;

    public String getUnitProjectName() {
        return UnitProjectName;
    }

    public void setUnitProjectName(String unitProjectName) {
        UnitProjectName = unitProjectName;
    }

    public List<UnitProjectModel> getItems() {
        return items;
    }

    public void setItems(List<UnitProjectModel> items) {
        this.items = items;
    }

    // apis
    public UnitProjectModel queryUnitProjectModelWithID(String ID) {
        for (UnitProjectModel unitProjectModel : items) {
            if (unitProjectModel.getID().equals(ID)) {
                return unitProjectModel;
            }
        }

        return null;
    }
}
