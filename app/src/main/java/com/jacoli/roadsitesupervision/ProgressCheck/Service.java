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
import com.jacoli.roadsitesupervision.services.Utils;

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

    // 获取工序目录
    public void sendQueryProgressItems(Callbacks callbacks) {
        if (!isLogined()) {
            onFailed("错误：未登录", callbacks);
            return;
        }

        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("GetAllComponentProcess"))
                        .post(body)
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, ProgressItemsModel.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }

    // 获取进度巡查记录
    public void sendQueryProgressCheckItems(final String ComponentID, Callbacks callbacks) {
        if (!isLogined()) {
            onFailed("错误：未登录", callbacks);
            return;
        }

        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .add("ComponentID", ComponentID)
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("GetUnFileProgressCheck"))
                        .post(body)
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, ProgressCheckItemsModel.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }

    // 监理巡查归档
    public void sendSubmitProgressCheck(final String ComponentID, final String ComponentProcessID, Callbacks callbacks) {
        if (!isLogined()) {
            onFailed("错误：未登录", callbacks);
            return;
        }

        if (Utils.isStringEmpty(ComponentID)
                || Utils.isStringEmpty(ComponentProcessID)) {
            onFailed("错误：ID不能为空", callbacks);
            return;
        }

        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .add("ComponentID", ComponentID)
                        .add("ComponentProcessID", ComponentProcessID)
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("SubmitProgressCheck"))
                        .post(body)
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, ResponseBase.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }

    // 监理巡查归档
    public void sendFileProgressCheck(final String ComponentID, Callbacks callbacks) {
        if (!isLogined()) {
            onFailed("错误：未登录", callbacks);
            return;
        }

        if (Utils.isStringEmpty(ComponentID)) {
            onFailed("错误：ID不能为空", callbacks);
            return;
        }

        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .add("ComponentID", ComponentID)
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("CompleteComponent_ProgressCheck"))
                        .post(body)
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, ResponseBase.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }
}
