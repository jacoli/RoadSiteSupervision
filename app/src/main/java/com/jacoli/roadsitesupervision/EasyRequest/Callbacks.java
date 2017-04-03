package com.jacoli.roadsitesupervision.EasyRequest;

/**
 * Created by lichuange on 17/4/3.
 */

public interface Callbacks {
    void onSuccess(ResponseBase responseModel);
    void onFailed(String error);
}
