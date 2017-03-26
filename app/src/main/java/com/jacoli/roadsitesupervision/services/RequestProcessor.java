package com.jacoli.roadsitesupervision.services;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by lichuange on 17/3/26.
 */

public interface RequestProcessor {
    Response execute() throws IOException;
    MsgResponseBase parse(String responseJsonString, Gson gson);
    void onSuccess(MsgResponseBase res);
}
