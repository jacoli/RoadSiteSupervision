package com.jacoli.roadsitesupervision.ProjectConfigs;

/**
 * Created by lichuange on 2017/5/6.
 */

public class Configs {
    // 公路监理系统
    public static final int project_type_8001 = 1;

    // 320监理系统
    static public int project_type_8002 = 2;

    // 十里埠监理系统
    static public int project_type_8003 = 3;

    // 320业主系统
    static public int project_type_9008 = 4;

    // 返回项目系统类型
    static public int projectType() {
        return Configs.project_type_8002;
    }

    // 默认服务器端口
    static public String defaultServerPort() {
        int projectType = Configs.projectType();
        if (projectType == project_type_8001) {
            return "8001";
        } else if (projectType == project_type_8002) {
            return "8002";
        } else if (projectType == project_type_8003) {
            return "8003";
        } else if (projectType == project_type_9008) {
            return "9008";
        } else {
            return "8001";
        }
    }

    // 默认服务器地址
    static public String defaultServerAddress() {
        return "http://118.178.92.22:" + Configs.defaultServerPort();
    }
}
