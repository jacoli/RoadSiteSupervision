package com.jacoli.roadsitesupervision.services;

import java.util.List;

/**
 * Created by lichuange on 17/2/11.
 */

public class UnitProjectModel extends MsgResponseBase {

    // 构件
    public class ComponentModel {

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
        private String PZStatus;

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

        public String getPZStatus() {
            return PZStatus;
        }

        public void setPZStatus(String PZStatus) {
            this.PZStatus = PZStatus;
        }

        public String getCheckStatus() {
            return CheckStatus;
        }

        public void setCheckStatus(String checkStatus) {
            CheckStatus = checkStatus;
        }
    }

    // 分部分项工程
    public class SubProjectModel {

        // 序号
        private int Ordinal;

        // 分部分项工程名称
        private String Name;

        // 构件列表数组
        private List<UnitProjectModel.ComponentModel> Components;

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

        public List<UnitProjectModel.ComponentModel> getComponents() {
            return Components;
        }

        public void setComponents(List<UnitProjectModel.ComponentModel> components) {
            Components = components;
        }
    }

    // 分部分项工程列表
    private List<UnitProjectModel.SubProjectModel> SubProjects;

    public List<UnitProjectModel.SubProjectModel> getSubProjects() {
        return SubProjects;
    }

    public void setSubProjects(List<UnitProjectModel.SubProjectModel> subProjects) {
        SubProjects = subProjects;
    }
}
