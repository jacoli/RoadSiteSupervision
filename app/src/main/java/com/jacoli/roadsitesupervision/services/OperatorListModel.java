package com.jacoli.roadsitesupervision.services;

import java.util.List;

/**
 * Created by lichuange on 17/2/11.
 */

public class OperatorListModel extends MsgResponseBase {
    // SYRY表示试验人员姓名数组
    private List<String> SYRY;

    // ZJY表示质检员姓名数组
    private List<String> ZJY;

    // ZZAQY表示专职安全员姓名数组
    private List<String> ZZAQY;
}
