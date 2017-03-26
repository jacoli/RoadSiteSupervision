package com.jacoli.roadsitesupervision.services;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by lichuange on 17/3/26.
 */

public interface RequestAndResponseHandler {
    Response buildRequestAndWaitingResponse() throws IOException;
    MsgResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson);
    void onSuccessHandleBeforeNotify(MsgResponseBase responseModel);
}
