package com.jacoli.roadsitesupervision.ProjectConfigs;

import com.jacoli.roadsitesupervision.BuildConfig;

/**
 * Created by lichuange on 2017/5/6.
 */

public class Configs {
    // 默认服务器端口
    static public String defaultServerPort() {
        return BuildConfig.ServerPort;
    }

    // 默认服务器地址
    static public String defaultServerAddress() {
        return BuildConfig.ServerAddress + ":" + Configs.defaultServerPort();
    }
}
