package com.jacoli.roadsitesupervision.services;

import java.util.List;

/**
 * Created by lichuange on 17/2/10.
 */

public class SubProjectsResponse extends MsgResponseBase {

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

    // 子工程
    public class SubProjectModel {

        // 序号
        private int Ordinal;

        // 子工程名称
        private String Name;

        // 构件列表数组
        private List<ComponentModel> Components;

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

        public List<ComponentModel> getComponents() {
            return Components;
        }

        public void setComponents(List<ComponentModel> components) {
            Components = components;
        }
    }

    // 子工程列表
    private List<SubProjectModel> SubProjects;

    public List<SubProjectModel> getSubProjects() {
        return SubProjects;
    }

    public void setSubProjects(List<SubProjectModel> subProjects) {
        SubProjects = subProjects;
    }
}
