package com.jacoli.roadsitesupervision.DataMonitor;

import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;

import java.util.List;

/**
 * Created by lichuange on 2017/10/9.
 */

public class GetPointAlarmHistroyModel extends ResponseBase {
    private int counts;
    private int pageCounts;
    private List<Item> items;

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public int getPageCounts() {
        return pageCounts;
    }

    public void setPageCounts(int pageCounts) {
        this.pageCounts = pageCounts;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    class Item {
        private String ID;
        private String ProjectName;
        private String UnitProjectName;
        private String PointCode;
        private String MonitorTypeName;
        private int ProcessStatus;
        private String AddTime;
        private String ProcessByName;
        private String ProcessTime;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getProjectName() {
            return ProjectName;
        }

        public void setProjectName(String projectName) {
            ProjectName = projectName;
        }

        public String getUnitProjectName() {
            return UnitProjectName;
        }

        public void setUnitProjectName(String unitProjectName) {
            UnitProjectName = unitProjectName;
        }

        public String getPointCode() {
            return PointCode;
        }

        public void setPointCode(String pointCode) {
            PointCode = pointCode;
        }

        public String getMonitorTypeName() {
            return MonitorTypeName;
        }

        public void setMonitorTypeName(String monitorTypeName) {
            MonitorTypeName = monitorTypeName;
        }

        public int getProcessStatus() {
            return ProcessStatus;
        }

        public String getProcessStatusStr() {
            if (ProcessStatus == 0) {
                return "未处理";
            } else if (ProcessStatus == 1) {
                return "处治完毕";
            } else {
                return "传感器故障";
            }
        }


        public void setProcessStatus(int processStatus) {
            ProcessStatus = processStatus;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String addTime) {
            AddTime = addTime;
        }

        public String getProcessByName() {
            return ProcessByName;
        }

        public void setProcessByName(String processByName) {
            ProcessByName = processByName;
        }

        public String getProcessTime() {
            return ProcessTime;
        }

        public void setProcessTime(String processTime) {
            ProcessTime = processTime;
        }
    }
}