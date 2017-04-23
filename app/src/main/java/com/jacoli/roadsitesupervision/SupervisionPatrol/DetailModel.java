package com.jacoli.roadsitesupervision.SupervisionPatrol;

import com.jacoli.roadsitesupervision.services.MsgResponseBase;

/**
 * Created by lichuange on 2017/4/23.
 */

public class DetailModel extends MsgResponseBase {
}


//阿里云地址：
//        http://118.178.92.22:8001/APP.ashx?Type=GetSupervisionCheckDetail
//        POST参数
//        Token=xxxxxxx&SupervisionCheckID=xxx
//        SupervisionCheckID：监理巡查ID
//        服务器返回：
//        {
//        "Status": 0,
//        "Msg": "OK",
//        "ProjectPart": "测试一下",
//        "CheckType":与GetAllSupervisionCheckItem的返回结构基本相同,
//        "Description": "测试一下下",
//        "AddByName": "张1(JL01)",
//        "AddTime": "2017-04-20 20:26:16",
//        "SupervisionCheckStatus": 1,
//        "ApprovalBy": "59df6347-18a9-4ec4-a56a-3fc6782c51ab",
//        "ApprovalByName": "张2(JL01)",
//        "ApprovalTime": "2017-04-20 20:29:21",
//        "ApprovalComment": "要整改",
//        "ReceiverName": "张三(JL02)",
//        "Progress": "张三(JL02) 回复",
//        "PhotoList": [
//        {
//        "Ordinal": 0,
//        "WebPath": "xxxxx"
//        },
//        {
//        "Ordinal": 1,
//        "WebPath": "xxxxx"
//        }
//        ],
//        "Reply": [
//        {
//        "ReplyName": "张三(JL02)",
//        "AddTime": "2017-04-20 20:35:24",
//        "ReplyContent": "改好了",
//        "PhotoList": []
//        },
//        {
//        "ReplyName": "张三(JL02)",
//        "AddTime": "2017-04-20 20:51:42",
//        "ReplyContent": "肯定好了",
//        "PhotoList": [
//        {
//        "Ordinal": 0,
//        "WebPath": "xxxxx"
//        },
//        {
//        "Ordinal": 1,
//        "WebPath": "xxxxx"
//        }
//        ]
//        }
//        ]
//        }
//
//        ProjectPart：工程地点部位
//        CheckType：检查细目。嵌套层级，最高层只有1个数据，即检查大项
//        ID：监理巡查细目ID
//        Ordinal:序号
//        Name：细目名称
//        Remark：备注
//        Code：细目编号（最底层才会有）
//        IsCheck：是否勾选（最底层才会有）
//        Level：层级，最底层为1，上一层为2，以此类推。
//        Description：补充描述
//        AddByName：上报人姓名
//        AddTime：上报时间
//        SupervisionCheckStatus：监理巡查状态，0待审核，1审核中，2已归档
//        ApprovalBy：审批人ID
//        ApprovalByName：审批人姓名
//        ApprovalTime：审批时间
//        ApprovalComment：处理意见
//        ReceiverName：承办人姓名
//        Progress：最新进展
//        PhotoList：上报时上传的照片
//        Ordinal序号
//        WebPath照片的网址
//        Reply：回复列表
//        ReplyName：回复人姓名
//        AddTime:回复时间
//        ReplyContent：内容
//        PhotoList：照片列表
//        Ordinal序号
//        WebPath照片的网址