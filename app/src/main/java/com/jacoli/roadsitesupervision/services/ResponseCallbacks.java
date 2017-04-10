package com.jacoli.roadsitesupervision.services;

/**
 * Created by lichuange on 17/4/3.
 */

public interface ResponseCallbacks {
    void onSuccess(MsgResponseBase responseModel);
    void onFailed(String error);
}
