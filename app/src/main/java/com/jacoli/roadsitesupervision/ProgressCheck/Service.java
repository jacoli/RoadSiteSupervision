package com.jacoli.roadsitesupervision.ProgressCheck;

import android.os.Handler;

import com.google.gson.Gson;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.EasyRequest;
import com.jacoli.roadsitesupervision.EasyRequest.Processor;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.SupervisionPatrol.ApproverListModel;
import com.jacoli.roadsitesupervision.SupervisionPatrol.SupervisionPatrolService;
import com.jacoli.roadsitesupervision.services.MainService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lichuange on 2017/5/2.
 */

public class Service {
    private OkHttpClient httpClient = new OkHttpClient();
    private Handler handler = new Handler();
    private static Service ourInstance = new Service();

    public static Service getInstance() {
        return ourInstance;
    }

    private void onFailed(final String error, final Callbacks callbacks) {
        if (callbacks != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callbacks.onFailed(error);
                }
            });
        }
    }

    private boolean isLogined() {
        if (MainService.getInstance().getLoginModel() == null || !MainService.getInstance().getLoginModel().isLoginSuccess()) {
            return false;
        } else {
            return true;
        }
    }

    private String getToken() {
        return MainService.getInstance().getLoginModel().getToken();
    }

    private String requestUrl(String type) {
        return MainService.getInstance().getServerBaseUrl() + "/APP.ashx?Type=" + type;
    }

    // 获取审批人列表
    public void sendQueryApproverList(final String SupervisionCheckItemID, Callbacks callbacks) {
        if (!isLogined()) {
            onFailed("错误：未登录", callbacks);
            return;
        }

        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .add("SupervisionCheckItemID", SupervisionCheckItemID)
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("GetAllSupCheckItemApprovalList"))
                        .post(body)
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, ApproverListModel.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }
}
