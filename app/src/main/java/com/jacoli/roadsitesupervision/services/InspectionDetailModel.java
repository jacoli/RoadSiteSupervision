package com.jacoli.roadsitesupervision.services;

import java.util.List;

/**
 * Created by Administrator on 2017/2/26.
 */

public class InspectionDetailModel extends MsgResponseBase {
    private String IsExist;
    private String Situation;
    private String Weather;
    private String AirTep;
    private List<ImageUrlModel> PhotoList;

    public String getIsExist() {
        return IsExist;
    }

    public void setIsExist(String isExist) {
        IsExist = isExist;
    }

    public String getSituation() {
        return Situation;
    }

    public void setSituation(String situation) {
        Situation = situation;
    }

    public String getWeather() {
        return Weather;
    }

    public void setWeather(String weather) {
        Weather = weather;
    }

    public String getAirTep() {
        return AirTep;
    }

    public void setAirTep(String airTep) {
        AirTep = airTep;
    }

    public List<ImageUrlModel> getPhotoList() {
        return PhotoList;
    }

    public void setPhotoList(List<ImageUrlModel> photoList) {
        PhotoList = photoList;
    }
}
