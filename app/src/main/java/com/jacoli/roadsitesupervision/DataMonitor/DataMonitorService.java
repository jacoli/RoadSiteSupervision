package com.jacoli.roadsitesupervision.DataMonitor;

import android.os.Handler;

import com.google.gson.Gson;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.EasyRequest;
import com.jacoli.roadsitesupervision.EasyRequest.Processor;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.Utils;
import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lichuange on 2017/6/14.
 */

public class DataMonitorService {
    private OkHttpClient httpClient = new OkHttpClient();
    private Handler handler = new Handler();
    private static DataMonitorService ourInstance = new DataMonitorService();

    public static DataMonitorService getInstance() {
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

    // 查询项目详情
    public void sendProjectDetailQuery(Callbacks callbacks) {
        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .add("ProjectID", MainService.getInstance().getLoginModel().getProjectID())
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("GetUnitProjectList"))
                        .post(body)
                        .header("FromAPP", "")
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, ProjectDetailModel.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }

    // 激活单位工程
    public void sendActiveUnitProject(final String Id, Callbacks callbacks) {
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
                        .add("UnitProjectID", Id)
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("ActiveUnitProject"))
                        .post(body)
                        .header("FromAPP", "")
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, ActiveUnitProjectResp.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }

    // 根据单位工程ID获取测点列表
    public void GetPointListByUnitProjectID(final String id, Callbacks callbacks) {
        if (Utils.isStringEmpty(id)) {
            onFailed("错误：ID不能为空", callbacks);
            return;
        }

        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .add("UnitProjectID", id)
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("GetPointListByUnitProjectID"))
                        .post(body)
                        .header("FromAPP", "")
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, PointListModel.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }

    // 获取传感器类型列表
    public void GetMonitorTypeList(Callbacks callbacks) {
        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("GetMonitorTypeList"))
                        .post(body)
                        .header("FromAPP", "")
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, MonitorSensorTypesModel.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }

    // 根据监测/传感器类型ID获取传感器数据列表
    public void GetSensorListByMonitorTypeID(final String id, Callbacks callbacks) {
        if (Utils.isStringEmpty(id)) {
            onFailed("错误：ID不能为空", callbacks);
            return;
        }

        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .add("MonitorTypeID", id)
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("GetSensorListByMonitorTypeID"))
                        .post(body)
                        .header("FromAPP", "")
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, MonitorSensorListModel.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }

    // 根据监测/传感器类型ID获取传感器数据列表
    public void AddSensorData(final String id,
                              final String code,
                              final String value1,
                              final String value2,
                              final String value3,
                              Callbacks callbacks) {
        if (Utils.isStringEmpty(id)) {
            onFailed("错误：ID不能为空", callbacks);
            return;
        }

        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .add("MonitorTypeID", id)
                        .add("SensorCode", Utils.notNullString(code))
                        .add("Value1", Utils.notNullString(value1))
                        .add("Value2", Utils.notNullString(value2))
                        .add("Value3", Utils.notNullString(value3))
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("AddSensorData"))
                        .post(body)
                        .header("FromAPP", "")
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

    // 根据单位工程ID获取测点布局图
    public void GetPointPicByUnitProjectID(final String id, Callbacks callbacks) {
        if (Utils.isStringEmpty(id)) {
            onFailed("错误：ID不能为空", callbacks);
            return;
        }

        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .add("UnitProjectID", id)
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("GetUnitProjectPointPic"))
                        .post(body)
                        .header("FromAPP", "")
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, PointsImageModel.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }

    // 根据单位工程ID获取测点布局图
    public void GetHistroySensorData(final String MonitorPointID, final String DayStr, Callbacks callbacks) {
        if (Utils.isStringEmpty(MonitorPointID)) {
            onFailed("错误：ID不能为空", callbacks);
            return;
        }

        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .add("MonitorPointID", MonitorPointID)
                        .add("Date", Utils.notNullString(DayStr))
                        .add("APP", "Y")
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("GetHistroySensorData"))
                        .post(body)
                        .header("FromAPP", "")
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, GetHistroySensorDataModel.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }

    public void GetPointAlarmHistroy(final boolean IsProcessed, final int page, Callbacks callbacks) {
        EasyRequest req = new EasyRequest(new Processor() {
            @Override
            public Response buildRequestAndWaitingResponse() throws IOException {
                FormBody body = new FormBody.Builder()
                        .add("Token", getToken())
                        .add("ProjectID", MainService.getInstance().getLoginModel().getProjectID())
                        .add("IsProcessed", IsProcessed ? "true" : "false")
                        .add("Page", String.valueOf(page))
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl("GetPointAlarmHistroy"))
                        .post(body)
                        .header("FromAPP", "")
                        .build();

                return httpClient.newCall(request).execute();
            }

            @Override
            public ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson) {
                return gson.fromJson(responseJsonString, GetPointAlarmHistroyModel.class);
            }

            @Override
            public void onSuccessHandleBeforeNotify(ResponseBase responseModel) {}
        }, handler, callbacks);
        req.asyncSend();
    }
}
