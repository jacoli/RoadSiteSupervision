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

import okhttp3.FormBody;
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

    /*
    * 监理巡查
    * */

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
                        .add("ProjectID", MainService.getInstance().getLoginModel().getProjectID())
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

    // 获取巡查明细列表
    public void sendQuerySupervisionPatrolCheckItemList(Callbacks callbacks) {
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
                        .url(requestUrl("GetAllSupervisionCheckItem"))
                        .post(body)
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, CheckItemsModel.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }

    // 获取监理巡查列表
    public void sendQuerySupervisionPatrolList(Callbacks callbacks) {
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
                        .url(requestUrl("GetMySupervisionCheckList"))
                        .post(body)
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, SupervisionPatrolListModel.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }

    // 获取监理巡查详情
    public void sendQueryDetail(final String SupervisionCheckID, Callbacks callbacks) {
        if (!isLogined()) {
            onFailed("错误：未登录", callbacks);
            return;
        }

        if (Utils.isStringEmpty(SupervisionCheckID)) {
            onFailed("参数错误", callbacks);
            return;
        }

        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .add("SupervisionCheckID", SupervisionCheckID)
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("GetSupervisionCheckDetail"))
                        .post(body)
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, DetailModel.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }

    // 创建监理巡查
    public void sendCreateSupervisionPatrol(final String projectPart,
                                            final String typeId,
                                            final String itemIds,
                                            final String description,
                                            final String approvalById,
                                            final List<String> imgUrls,
                                            Callbacks callbacks) {
        if (!isLogined()) {
            onFailed("错误：未登录", callbacks);
            return;
        }

        if (Utils.isStringEmpty(typeId)) {
            onFailed("参数错误", callbacks);
            return;
        }

        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("Token", MainService.getInstance().getLoginModel().getToken())
                        .addFormDataPart("ProjectID", MainService.getInstance().getLoginModel().getProjectID())
                        .addFormDataPart("ProjectPart", projectPart)
                        .addFormDataPart("CheckTypeID", typeId)
                        .addFormDataPart("ItemIDs", itemIds)
                        .addFormDataPart("Description", Utils.notNullString(description))
                        .addFormDataPart("ApprovalBy", approvalById);

                if (imgUrls != null) {
                    for (String imgUrl : imgUrls) {
                        builder.addPart(Headers.of("Content-Disposition", "form-data; filename=\"img.png\""),
                                RequestBody.create(MediaType.parse("image/png"), new File(imgUrl)));
                    }
                }

                Request request = new Request.Builder()
                        .url(requestUrl("SubmitSupervisionCheck"))
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

    // 审批监理巡查
    public void sendSupervisionPatrolApproval(final String Id, final String comment, final String type, final String receiverId, Callbacks callbacks) {
        if (!isLogined()) {
            onFailed("错误：未登录", callbacks);
            return;
        }

        if (Utils.isStringEmpty(Id)) {
            onFailed("错误：ID不能为空", callbacks);
            return;
        }

        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .add("SupervisionCheckID", Id)
                        .add("ApprovalComment", comment)
                        .add("OperType", type)
                        .add("ReceiverID", receiverId)
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("SupervisionCheckApproval"))
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

    // 回复监理巡查
    public void sendSupervisionPatrolReply(final String id, final String content, final List<String> imgUrls, Callbacks callbacks) {
        if (!isLogined()) {
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
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("Token", MainService.getInstance().getLoginModel().getToken())
                        .addFormDataPart("SupervisionCheckID", id)
                        .addFormDataPart("ReplyContent", content);

                if (imgUrls != null) {
                    for (String imgUrl : imgUrls) {
                        builder.addPart(Headers.of("Content-Disposition", "form-data; filename=\"img.png\""),
                                RequestBody.create(MediaType.parse("image/png"), new File(imgUrl)));
                    }
                }

                Request request = new Request.Builder()
                        .url(requestUrl("SubmitSupervisionCheckReply"))
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

    // 监理巡查归档
    public void sendFinishSupervisionPatrol(final String Id, Callbacks callbacks) {
        if (!isLogined()) {
            onFailed("错误：未登录", callbacks);
            return;
        }

        if (Utils.isStringEmpty(Id)) {
            onFailed("错误：ID不能为空", callbacks);
            return;
        }

        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .add("SupervisionCheckID", Id)
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("FileSupervisionCheck"))
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
