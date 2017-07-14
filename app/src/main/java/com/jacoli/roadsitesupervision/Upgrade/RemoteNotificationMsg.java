package com.jacoli.roadsitesupervision.Upgrade;

/**
 * Created by lichuange on 2017/7/14.
 */

//"extra": {
//        "type": "assigned_matters" // 业务类型，交办事项assigned_matters，巡查patrol
//        }

public class RemoteNotificationMsg {

    public RemoteNotificationMsg(String type) {
        this.type = type;
    }

    public String type; // "type": "assigned_matters" // 业务类型，交办事项assigned_matters，巡查patrol
}
