package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.os.Handler;

import com.google.gson.Gson;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.EasyRequest;
import com.jacoli.roadsitesupervision.EasyRequest.Processor;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lichuange on 2017/4/19.
 */

public class SupervisionPatrolService {
    private OkHttpClient httpClient = new OkHttpClient();
    private Handler handler = new Handler();
    private static SupervisionPatrolService ourInstance = new SupervisionPatrolService();

    public static SupervisionPatrolService getInstance() {
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

    /*
    * 监理巡查
    * */

    // 获取监理巡查列表

    // 获取监理巡查详情

    // 创建监理巡查

    // 审批监理巡查

    // 回复监理巡查
    public void sendSupervisionPatrolReply(final String id, final String content, final List<String> imgUrls, Callbacks callbacks) {
        if (MainService.getInstance().getLoginModel() == null || !MainService.getInstance().getLoginModel().isLoginSuccess()) {
            onFailed("错误：未登录", callbacks);
            return;
        }

        if (Utils.isStringEmpty(id)) {
            onFailed("错误：ID不能为空", callbacks);
            return;
        }

        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                // TODO
                String url = MainService.getInstance().getServerBaseUrl() + "/APP.ashx?Type=SubmitAssignedMatterReply";

                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("Token", MainService.getInstance().getLoginModel().getToken())
                        .addFormDataPart("AssignedMatterID", id)
                        .addFormDataPart("AssignContent", content);

                if (imgUrls != null) {
                    for (String imgUrl : imgUrls) {
                        builder.addPart(Headers.of("Content-Disposition", "form-data; filename=\"img.png\""),
                                RequestBody.create(MediaType.parse("image/png"), new File(imgUrl)));
                    }
                }

                Request request = new Request.Builder()
                        .url(url)
                        .post(builder.build())
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
