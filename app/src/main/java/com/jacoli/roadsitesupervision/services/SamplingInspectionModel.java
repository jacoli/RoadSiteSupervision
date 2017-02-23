package com.jacoli.roadsitesupervision.services;

import java.util.List;

/**
 * Created by lichuange on 17/2/23.
 */

public class SamplingInspectionModel extends MsgResponseBase {
    private String IsExist;
    private String Situation;
    private String Weather;
    private String AirTep;
    private String CheckByName;
    private String CheckPart;
    private String CheckTime;
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

    public String getCheckByName() {
        return CheckByName;
    }

    public void setCheckByName(String checkByName) {
        CheckByName = checkByName;
    }

    public String getCheckPart() {
        return CheckPart;
    }

    public void setCheckPart(String checkPart) {
        CheckPart = checkPart;
    }

    public String getCheckTime() {
        return CheckTime;
    }

    public void setCheckTime(String checkTime) {
        CheckTime = checkTime;
    }

    public List<ImageUrlModel> getPhotoList() {
        return PhotoList;
    }

    public void setPhotoList(List<ImageUrlModel> photoList) {
        PhotoList = photoList;
    }
}

