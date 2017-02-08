package com.jacoli.roadsitesupervision.services;

import java.util.List;

/**
 * Created by lichuange on 16/4/2.
 */
public class GetSignCheckResponse extends MsgResponseBase {

    private List<SignItemModel> SignItems;
    private List<SensorItemModel> SensorItems;

    public List<SignItemModel> getSignItems() {
        return SignItems;
    }

    public void setSignItems(List<SignItemModel> signItems) {
        SignItems = signItems;
    }

    public List<SensorItemModel> getSensorItems() {
        return SensorItems;
    }

    public void setSensorItems(List<SensorItemModel> sensorItems) {
        SensorItems = sensorItems;
    }
}
