package com.jacoli.roadsitesupervision.services;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.lang.reflect.Type;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainService {

    // 旁站
    public static final int project_detail_type_pz = 0x1001;
    // 质量巡视
    public static final int project_detail_type_quality_inspection = 0x1002;
    // 安全巡视
    public static final int project_detail_type_safety_inspection = 0x1003;
    // 环保巡视
    public static final int project_detail_type_environmental_inspection = 0x1004;
    // 质量抽检
    public static final int project_detail_type_quality_sampling_inspection = 0x1005;


    public static final int MSG_LOGIN_SUCCESS = 0x1001;
    public static final int MSG_LOGIN_FAILED = 0x1002;
    public static final int MSG_QUERY_PROJECT_DETAIL_SUCCESS = 0x3001;
    public static final int MSG_QUERY_PROJECT_DETAIL_FAILED = 0x3002;
    public static final int MSG_ACTIVE_UNIT_PROJECT_SUCCESS = 0x4001;
    public static final int MSG_ACTIVE_UNIT_PROJECT_FAILED = 0x4002;
    public static final int MSG_QUERY_UNIT_PROJECT_DETAIL_SUCCESS = 0x5001;
    public static final int MSG_QUERY_UNIT_PROJECT_DETAIL_FAILED = 0x5002;
    public static final int MSG_ACTIVE_COMPONENT_SUCCESS = 0x6001;
    public static final int MSG_ACTIVE_COMPONENT_FAILED = 0x6002;
    public static final int MSG_QUERY_COMPONENT_DETAIL_SUCCESS = 0x7001;
    public static final int MSG_QUERY_COMPONENT_DETAIL_FAILED = 0x7002;
    public static final int MSG_QUERY_OPERATOR_LIST_SUCCESS = 0x8001;
    public static final int MSG_QUERY_OPERATOR_LIST_FAILED = 0x8002;
    public static final int MSG_QUERY_PZ_DETAIL_SUCCESS = 0x9001;
    public static final int MSG_QUERY_PZ_DETAIL_FAILED = 0x9002;
    public static final int MSG_SUBMIT_PZ_DETAIL_SUCCESS = 0xa001;
    public static final int MSG_SUBMIT_PZ_DETAIL_FAILED = 0xa002;
    public static final int MSG_FINISH_COMPONENT_SUCCESS = 0xb001;
    public static final int MSG_FINISH_COMPONENT_FAILED = 0xb002;
    public static final int MSG_QUERY_INSPECTION_DETAIL_SUCCESS = 0xc001;
    public static final int MSG_QUERY_INSPECTION_DETAIL_FAILED = 0xc002;
    public static final int MSG_SUBMIT_INSPECTION_DETAIL_SUCCESS = 0xc003;
    public static final int MSG_SUBMIT_INSPECTION_DETAIL_FAILED = 0xc004;
    public static final int MSG_QUERY_COMPONENT_SAMPLING_INSPECTION_SUCCESS = 0xd001;
    public static final int MSG_QUERY_COMPONENT_SAMPLING_INSPECTION_FAILED = 0xd002;
    public static final int MSG_SUBMIT_COMPONENT_SAMPLING_INSPECTION_SUCCESS = 0xd003;
    public static final int MSG_SUBMIT_COMPONENT_SAMPLING_INSPECTION_FAILED = 0xd004;
    public static final int MSG_QUERY_WEATHER_SUCCESS = 0xf001;
    public static final int MSG_QUERY_WEATHER_FAILED = 0xf002;

    public String serverBaseUrl = "http://118.178.92.22:8001";

    private OkHttpClient httpClient;
    private LoginModel loginModel;
    private WeatherModel weatherModel;

    public WeatherModel getWeatherModel() { return weatherModel; }

    private static MainService ourInstance = new MainService();

    public static MainService getInstance() {
        return ourInstance;
    }

    private MainService() {
        httpClient = new OkHttpClient();

        weatherModel = new WeatherModel();
        weatherModel.setWeather("");
        weatherModel.setAirTep("");
    }

    public boolean setServerAddress(String address, String port) {
        if (address.length() == 0) {
            return false;
        }

        if (port.length() == 0) {
            return false;
        }

        Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        Matcher matcher = pattern.matcher(address); //以验证127.400.600.2为例
        if (!matcher.matches()) {
            return false;
        }

        serverBaseUrl = "http://" + address + ":" + port;

        return true;
    }

    public LoginModel getLoginModel() {
        return loginModel;
    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }

    private String responsePrevProcess(String inString) {
        return inString;
//        if (inString != null && inString.length() > 0) {
//            if (inString.startsWith("(")) {
//                inString = inString.substring(1);
//                if (inString.endsWith(")")) {
//                    inString = inString.substring(0, inString.length() - 1);
//                }
//            }
//            String outString = inString.replace("\\\"", "\"");
//            return outString;
//        }
//        else {
//            return "";
//        }
    }

    private void notifyMsg(Handler handler, int msgCode) {
        if (handler != null) {
            Message msg = new Message();
            msg.what = msgCode;
            handler.sendMessage(msg);
        }
    }

    private void notifyMsg(Handler handler, int msgCode, MsgResponseBase model) {
        if (handler != null) {
            Message msg = new Message();
            msg.what = msgCode;
            msg.obj = model;
            handler.sendMessage(msg);
        }
    }

    public boolean login(final String username, final String password, final Handler handler) {
        if (username.length() == 0 || password.length() == 0) {
            return false;
        }

        if (getLoginModel() != null) {
            getLoginModel().setToken("");
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    String url = serverBaseUrl + "/APP.ashx?Type=Login";

                    FormBody body = new FormBody.Builder()
                            .add("Username", username)
                            .add("Password", password)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();

                        Log.i("MainService", responseStr);

                        responseStr = responsePrevProcess(responseStr);
                        Gson gson = new Gson();
                        setLoginModel(gson.fromJson(responseStr, LoginModel.class));

                        if (getLoginModel() != null
                                && getLoginModel().isLoginSuccess()) {
                            Message msg = new Message();
                            msg.what = MSG_LOGIN_SUCCESS;
                            msg.obj = getLoginModel();
                            handler.sendMessage(msg);
                        }
                        else {
                            notifyLoginFailed(handler);
                        }
                    }
                    else {
                        notifyLoginFailed(handler);
                    }
                }
                catch (IOException e) {
                    notifyLoginFailed(handler);
                }
            }
        };

        new Thread(networkTask).start();

        return true;
    }

    public boolean logout() {
//        if (!getLoginModel().isLoginSuccess()) {
//            return false;
//        }

        //final String token = getLoginModel().getToken();

        setLoginModel(null);

//        Runnable networkTask = new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    String url = serverBaseUrl + "/Maintain/APP.ashx?Type=Loginout";
//
//                    FormBody body = new FormBody.Builder()
//                            .add("Token", token)
//                            .build();
//
//                    Request request = new Request.Builder()
//                            .url(url)
//                            .post(body)
//                            .build();
//
//                    getLoginModel().setToken("");
//                    Response response = httpClient.newCall(request).execute();
//                    if (response.isSuccessful()) {
//                        String responseStr = response.body().string();
//                        Log.i("MainService", responseStr);
//                    }
//                    else {
//                    }
//                }
//                catch (IOException e) {
//                }
//            }
//        };
//
//        new Thread(networkTask).start();

        return true;
    }

    private void notifyLoginFailed(Handler handler) {
        if (handler != null) {
            Message msg = new Message();
            msg.what = MSG_LOGIN_FAILED;
            handler.sendMessage(msg);
        }
    }

    // 查询项目详情
    public boolean sendProjectDetailQuery(final Handler handler) {
        if (getLoginModel() == null || !getLoginModel().isLoginSuccess()) {
            return false;
        }

        if (getLoginModel().getProjectID().length() == 0) {
            return false;
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    String url = serverBaseUrl + "/APP.ashx?Type=GetUnitProjectList";

                    FormBody body = new FormBody.Builder()
                            .add("Token", getLoginModel().getToken())
                            .add("ProjectID", getLoginModel().getProjectID())
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();

                        Log.i("MainService", responseStr);

                        responseStr = responsePrevProcess(responseStr);

                        Gson gson = new Gson();
                        ProjectDetailModel res = gson.fromJson(responseStr, ProjectDetailModel.class);

                        if (res != null && res.isSuccess()) {
                            // 通知UI
                            notifyMsg(handler, MSG_QUERY_PROJECT_DETAIL_SUCCESS, res);
                        }
                        else {
                            notifyMsg(handler, MSG_QUERY_PROJECT_DETAIL_FAILED);
                        }
                    }
                    else {
                        notifyMsg(handler, MSG_QUERY_PROJECT_DETAIL_FAILED);
                    }
                }
                catch (IOException e) {
                    notifyMsg(handler, MSG_QUERY_PROJECT_DETAIL_FAILED);
                }
            }
        };

        new Thread(networkTask).start();
        return true;
    }

    // 激活单位工程
    public boolean sendActiveUnitProject(final String ProjectID, final Handler handler) {
        if (getLoginModel() == null || !getLoginModel().isLoginSuccess()) {
            return false;
        }

        if (ProjectID.length() == 0) {
            return false;
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    String url = serverBaseUrl + "/APP.ashx?Type=ActiveUnitProject";

                    FormBody body = new FormBody.Builder()
                            .add("Token", getLoginModel().getToken())
                            .add("UnitProjectID", ProjectID)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();

                        Log.i("MainService", responseStr);

                        responseStr = responsePrevProcess(responseStr);

                        Gson gson = new Gson();
                        ActiveUnitProjectResp res = gson.fromJson(responseStr, ActiveUnitProjectResp.class);

                        if (res != null && res.isSuccess()) {
                            res.setID(ProjectID);

                            notifyMsg(handler, MSG_ACTIVE_UNIT_PROJECT_SUCCESS, res);
                        }
                        else {
                            notifyMsg(handler, MSG_ACTIVE_UNIT_PROJECT_FAILED);
                        }
                    }
                    else {
                        notifyMsg(handler, MSG_ACTIVE_UNIT_PROJECT_FAILED);
                    }
                }
                catch (IOException e) {
                    notifyMsg(handler, MSG_ACTIVE_UNIT_PROJECT_FAILED);
                }
            }
        };

        new Thread(networkTask).start();
        return true;
    }

    // 查询单位工程详情
    public boolean sendUnitProjectDetailQuery(final String ProjectID, final Handler handler) {
        if (getLoginModel() == null || !getLoginModel().isLoginSuccess()) {
            return false;
        }

        if (ProjectID.length() == 0) {
            return false;
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    String url = serverBaseUrl + "/APP.ashx?Type=GetComponentList";

                    FormBody body = new FormBody.Builder()
                            .add("Token", getLoginModel().getToken())
                            .add("UnitProjectID", ProjectID)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();

                        Log.i("MainService", responseStr);

                        responseStr = responsePrevProcess(responseStr);

                        Gson gson = new Gson();
                        UnitProjectModel res = gson.fromJson(responseStr, UnitProjectModel.class);

                        if (res != null && res.isSuccess()) {
                            // 通知UI
                            notifyMsg(handler, MSG_QUERY_UNIT_PROJECT_DETAIL_SUCCESS, res);
                        }
                        else {
                            notifyMsg(handler, MSG_QUERY_UNIT_PROJECT_DETAIL_FAILED);
                        }
                    }
                    else {
                        notifyMsg(handler, MSG_QUERY_UNIT_PROJECT_DETAIL_FAILED);
                    }
                }
                catch (IOException e) {
                    notifyMsg(handler, MSG_QUERY_UNIT_PROJECT_DETAIL_FAILED);
                }
            }
        };

        new Thread(networkTask).start();
        return true;
    }

    // 激活构件
    public boolean sendActiveComponent(final String id, final Handler handler) {
        if (getLoginModel() == null || !getLoginModel().isLoginSuccess()) {
            return false;
        }

        if (id.length() == 0) {
            return false;
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    String url = serverBaseUrl + "/APP.ashx?Type=ActiveComponent";

                    FormBody body = new FormBody.Builder()
                            .add("Token", getLoginModel().getToken())
                            .add("ComponentID", id)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();

                        Log.i("MainService", responseStr);

                        responseStr = responsePrevProcess(responseStr);

                        Gson gson = new Gson();
                        ActiveComponetResp res = gson.fromJson(responseStr, ActiveComponetResp.class);

                        if (res != null && res.isSuccess()) {
                            res.setID(id);
                            notifyMsg(handler, MSG_ACTIVE_COMPONENT_SUCCESS, res);
                        }
                        else {
                            notifyMsg(handler, MSG_ACTIVE_COMPONENT_FAILED);
                        }
                    }
                    else {
                        notifyMsg(handler, MSG_ACTIVE_COMPONENT_FAILED);
                    }
                }
                catch (IOException e) {
                    notifyMsg(handler, MSG_ACTIVE_COMPONENT_FAILED);
                }
            }
        };

        new Thread(networkTask).start();
        return true;
    }

    // 查询构件详情
    public boolean sendComponentDetailQuery(final String id, final Handler handler) {
        if (getLoginModel() == null || !getLoginModel().isLoginSuccess()) {
            return false;
        }

        if (id.length() == 0) {
            return false;
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    String url = serverBaseUrl + "/APP.ashx?Type=GetPZContentList";

                    FormBody body = new FormBody.Builder()
                            .add("Token", getLoginModel().getToken())
                            .add("ComponentID", id)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();

                        Log.i("MainService", responseStr);

                        responseStr = responsePrevProcess(responseStr);

                        Gson gson = new Gson();
                        ComponentDetailModel res = gson.fromJson(responseStr, ComponentDetailModel.class);

                        if (res != null && res.isSuccess()) {
                            // 通知UI
                            notifyMsg(handler, MSG_QUERY_COMPONENT_DETAIL_SUCCESS, res);
                        }
                        else {
                            notifyMsg(handler, MSG_QUERY_COMPONENT_DETAIL_FAILED);
                        }
                    }
                    else {
                        notifyMsg(handler, MSG_QUERY_COMPONENT_DETAIL_FAILED);
                    }
                }
                catch (IOException e) {
                    notifyMsg(handler, MSG_QUERY_COMPONENT_DETAIL_FAILED);
                }
            }
        };

        new Thread(networkTask).start();
        return true;
    }

    // 获取质检员、专职安全员、试验人员姓名列表
    public boolean sendOperatorListQuery(final Handler handler) {
        if (getLoginModel() == null || !getLoginModel().isLoginSuccess()) {
            return false;
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    String url = serverBaseUrl + "/APP.ashx?Type=GetZJYList";

                    FormBody body = new FormBody.Builder()
                            .add("Token", getLoginModel().getToken())
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();

                        Log.i("MainService", responseStr);

                        responseStr = responsePrevProcess(responseStr);

                        Gson gson = new Gson();
                        OperatorListModel res = gson.fromJson(responseStr, OperatorListModel.class);

                        if (res != null && res.isSuccess()) {
                            // 通知UI
                            notifyMsg(handler, MSG_QUERY_OPERATOR_LIST_SUCCESS, res);
                        }
                        else {
                            notifyMsg(handler, MSG_QUERY_OPERATOR_LIST_FAILED);
                        }
                    }
                    else {
                        notifyMsg(handler, MSG_QUERY_OPERATOR_LIST_FAILED);
                    }
                }
                catch (IOException e) {
                    notifyMsg(handler, MSG_QUERY_OPERATOR_LIST_FAILED);
                }
            }
        };

        new Thread(networkTask).start();
        return true;
    }

    // 获取旁站内容详情
    public boolean sendPZDetailQuery(final String id, final Handler handler) {
        if (getLoginModel() == null || !getLoginModel().isLoginSuccess()) {
            return false;
        }

        if (id.length() == 0) {
            return false;
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    String url = serverBaseUrl + "/APP.ashx?Type=GetPZContentDetail";

                    FormBody body = new FormBody.Builder()
                            .add("Token", getLoginModel().getToken())
                            .add("PZContentID", id)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();

                        Log.i("MainService", responseStr);

                        responseStr = responsePrevProcess(responseStr);

                        try {
                            Gson gson = new GsonBuilder()
                                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                    .create();

                            PZDetailModel res = gson.fromJson(responseStr, PZDetailModel.class);

                            if (res != null && res.isSuccess()) {
                                // 通知UI
                                notifyMsg(handler, MSG_QUERY_PZ_DETAIL_SUCCESS, res);
                            }
                            else {
                                notifyMsg(handler, MSG_QUERY_PZ_DETAIL_FAILED);
                            }
                        }
                        catch (Exception ex) {
                            Log.e("MainService", ex.toString());
                            notifyMsg(handler, MSG_QUERY_PZ_DETAIL_FAILED);
                        }
                    }
                    else {
                        notifyMsg(handler, MSG_QUERY_PZ_DETAIL_FAILED);
                    }
                }
                catch (IOException e) {
                    notifyMsg(handler, MSG_QUERY_PZ_DETAIL_FAILED);
                }
            }
        };

        new Thread(networkTask).start();
        return true;
    }

    // 提交旁站详情
    public boolean sendSubmitPZContentDetail(final String id, final Map<String, String> params, final Handler handler) {
        if (getLoginModel() == null || !getLoginModel().isLoginSuccess()) {
            return false;
        }

        if (id.length() == 0) {
            return false;
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    String url = serverBaseUrl + "/APP.ashx?Type=SubmitPZContentDetail";

                    MultipartBody.Builder builder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("Token", getLoginModel().getToken())
                            .addFormDataPart("PZContentID", id);

                    if (weatherModel.getWeather() != null && weatherModel.getWeather().length() > 0) {
                        builder.addFormDataPart("Weather", weatherModel.getWeather());
                    }
                    if (weatherModel.getAirTep() != null && weatherModel.getAirTep().length() > 0) {
                        builder.addFormDataPart("AirTep", weatherModel.getAirTep());
                    }

                    if (params != null) {
                        for (String key : params.keySet()) {
                            String value = params.get(key);
                            if (value != null) {
                                builder.addFormDataPart(key, value);
                            }
                        }
                    }

                    Log.i("MainService params = ", params.toString());

                    Request request = new Request.Builder()
                            .url(url)
                            .post(builder.build())
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();

                        Log.i("MainService", responseStr);

                        responseStr = responsePrevProcess(responseStr);

                        Gson gson = new Gson();
                        MsgResponseBase res = gson.fromJson(responseStr, MsgResponseBase.class);

                        if (res != null && res.isSuccess()) {
                            // 通知UI
                            notifyMsg(handler, MSG_SUBMIT_PZ_DETAIL_SUCCESS, res);
                        }
                        else {
                            notifyMsg(handler, MSG_SUBMIT_PZ_DETAIL_FAILED);
                        }
                    }
                    else {
                        notifyMsg(handler, MSG_SUBMIT_PZ_DETAIL_FAILED);
                    }
                }
                catch (IOException e) {
                    notifyMsg(handler, MSG_SUBMIT_PZ_DETAIL_FAILED);
                }
            }
        };

        new Thread(networkTask).start();
        return true;
    }

    // 标记构件施工完成
    public boolean sendFinishComponent(final String id, final Handler handler) {
        if (getLoginModel() == null || !getLoginModel().isLoginSuccess()) {
            return false;
        }

        if (id.length() == 0) {
            return false;
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    String url = serverBaseUrl + "/APP.ashx?Type=CompleteComponent";

                    FormBody body = new FormBody.Builder()
                            .add("Token", getLoginModel().getToken())
                            .add("ComponentID", id)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();

                        Log.i("MainService", responseStr);

                        responseStr = responsePrevProcess(responseStr);

                        Gson gson = new Gson();
                        FinishComponentResp res = gson.fromJson(responseStr, FinishComponentResp.class);

                        if (res != null && res.isSuccess()) {
                            res.setID(id);
                            notifyMsg(handler, MSG_FINISH_COMPONENT_SUCCESS, res);
                        }
                        else {
                            notifyMsg(handler, MSG_FINISH_COMPONENT_FAILED);
                        }
                    }
                    else {
                        notifyMsg(handler, MSG_FINISH_COMPONENT_FAILED);
                    }
                }
                catch (IOException e) {
                    notifyMsg(handler, MSG_FINISH_COMPONENT_FAILED);
                }
            }
        };

        new Thread(networkTask).start();
        return true;
    }

    // 获取巡视详情
    public boolean sendQueryComponentInspectionDetail(final String id, final String type, final Handler handler) {
        if (getLoginModel() == null || !getLoginModel().isLoginSuccess()) {
            return false;
        }

        if (id.length() == 0) {
            return false;
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    String url = serverBaseUrl + "/APP.ashx?Type=GetProjectPatrolDay";

                    FormBody body = new FormBody.Builder()
                            .add("Token", getLoginModel().getToken())
                            .add("ProjectID", id)
                            .add("PatrolType", type)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();

                        Log.i("MainService", responseStr);

                        responseStr = responsePrevProcess(responseStr);

                        try {
                            Gson gson = new GsonBuilder()
                                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                    .create();

                            InspectionDetailModel res = gson.fromJson(responseStr, InspectionDetailModel.class);

                            if (res != null && res.isSuccess()) {
                                // 通知UI
                                notifyMsg(handler, MSG_QUERY_INSPECTION_DETAIL_SUCCESS, res);
                            }
                            else {
                                notifyMsg(handler, MSG_QUERY_INSPECTION_DETAIL_FAILED);
                            }
                        }
                        catch (Exception ex) {
                            Log.e("MainService", ex.toString());
                            notifyMsg(handler, MSG_QUERY_INSPECTION_DETAIL_FAILED);
                        }
                    }
                    else {
                        notifyMsg(handler, MSG_QUERY_INSPECTION_DETAIL_FAILED);
                    }
                }
                catch (IOException e) {
                    Log.e("MainService", e.toString());
                    notifyMsg(handler, MSG_QUERY_INSPECTION_DETAIL_FAILED);
                }
            }
        };

        new Thread(networkTask).start();
        return true;
    }

    // 提交巡视详情
    public boolean sendSubmitInspectionDetail(final String id,
                                              final String type,
                                              final String Situation,
                                              final String delFiles,
                                              final List<String> imgUrls,
                                              final Handler handler) {
        if (getLoginModel() == null || !getLoginModel().isLoginSuccess()) {
            return false;
        }

        if (id.length() == 0) {
            return false;
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    String url = serverBaseUrl + "/APP.ashx?Type=SubmitProjectPatrolAPP";

                    MultipartBody.Builder builder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("Token", getLoginModel().getToken())
                            .addFormDataPart("ProjectID", id)
                            .addFormDataPart("PatrolType", type)
                            .addFormDataPart("Situation", Situation == null ? "" : Situation);

                    if (weatherModel.getWeather() != null && weatherModel.getWeather().length() > 0) {
                        builder.addFormDataPart("Weather", weatherModel.getWeather());
                    }
                    if (weatherModel.getAirTep() != null && weatherModel.getAirTep().length() > 0) {
                        builder.addFormDataPart("AirTep", weatherModel.getAirTep());
                    }

                    for (String imgUrl : imgUrls) {
                        builder.addPart(Headers.of("Content-Disposition", "form-data; filename=\"img.png\""),
                                RequestBody.create(MediaType.parse("image/png"), new File(imgUrl)));
                    }

                    builder.addFormDataPart("DelFile", delFiles);

                    Request request = new Request.Builder()
                            .url(url)
                            .post(builder.build())
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();

                        Log.i("MainService", responseStr);

                        responseStr = responsePrevProcess(responseStr);

                        Gson gson = new Gson();
                        MsgResponseBase res = gson.fromJson(responseStr, MsgResponseBase.class);

                        if (res != null && res.isSuccess()) {
                            // 通知UI
                            notifyMsg(handler, MSG_SUBMIT_INSPECTION_DETAIL_SUCCESS, res);
                        }
                        else {
                            notifyMsg(handler, MSG_SUBMIT_INSPECTION_DETAIL_FAILED);
                        }
                    }
                    else {
                        notifyMsg(handler, MSG_SUBMIT_INSPECTION_DETAIL_FAILED);
                    }
                }
                catch (IOException e) {
                    notifyMsg(handler, MSG_SUBMIT_INSPECTION_DETAIL_FAILED);
                }
            }
        };

        new Thread(networkTask).start();
        return true;
    }

    // 获取构件抽检详情
    public boolean sendQueryComponentSamplingInspection(final String id, final Handler handler) {
        if (getLoginModel() == null || !getLoginModel().isLoginSuccess()) {
            return false;
        }

        if (id.length() == 0) {
            return false;
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    String url = serverBaseUrl + "/APP.ashx?Type=GetComponentCheckData";

                    FormBody body = new FormBody.Builder()
                            .add("Token", getLoginModel().getToken())
                            .add("ComponentID", id)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();

                        Log.i("MainService", responseStr);

                        responseStr = responsePrevProcess(responseStr);

                        try {
                            Gson gson = new GsonBuilder()
                                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                    .create();

                            SamplingInspectionModel res = gson.fromJson(responseStr, SamplingInspectionModel.class);

                            if (res != null && res.isSuccess()) {
                                // 通知UI
                                notifyMsg(handler, MSG_QUERY_COMPONENT_SAMPLING_INSPECTION_SUCCESS, res);
                            }
                            else {
                                notifyMsg(handler, MSG_QUERY_COMPONENT_SAMPLING_INSPECTION_FAILED);
                            }
                        }
                        catch (Exception ex) {
                            Log.e("MainService", ex.toString());
                            notifyMsg(handler, MSG_QUERY_COMPONENT_SAMPLING_INSPECTION_FAILED);
                        }
                    }
                    else {
                        notifyMsg(handler, MSG_QUERY_COMPONENT_SAMPLING_INSPECTION_FAILED);
                    }
                }
                catch (IOException e) {
                    notifyMsg(handler, MSG_QUERY_COMPONENT_SAMPLING_INSPECTION_FAILED);
                }
            }
        };

        new Thread(networkTask).start();
        return true;
    }

    // 提交抽检详情
    public boolean sendSubmitComponentSamplingInspection(final String id,
                                                         final String Situation,
                                                         final String delFiles,
                                                         final List<String> imgUrls,
                                                         final Handler handler) {
        if (getLoginModel() == null || !getLoginModel().isLoginSuccess()) {
            return false;
        }

        if (id.length() == 0) {
            return false;
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    String url = serverBaseUrl + "/APP.ashx?Type=SubmitComponentCheckData";

                    MultipartBody.Builder builder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("Token", getLoginModel().getToken())
                            .addFormDataPart("ComponentID", id)
                            .addFormDataPart("Situation", Situation == null ? "" : Situation);

                    if (weatherModel.getWeather() != null && weatherModel.getWeather().length() > 0) {
                        builder.addFormDataPart("Weather", weatherModel.getWeather());
                    }
                    if (weatherModel.getAirTep() != null && weatherModel.getAirTep().length() > 0) {
                        builder.addFormDataPart("AirTep", weatherModel.getAirTep());
                    }

                    for (String imgUrl : imgUrls) {
                        builder.addPart(Headers.of("Content-Disposition", "form-data; filename=\"img.png\""),
                                RequestBody.create(MediaType.parse("image/png"), new File(imgUrl)));
                    }

                    builder.addFormDataPart("DelFile", delFiles);

                    Request request = new Request.Builder()
                            .url(url)
                            .post(builder.build())
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();

                        Log.i("MainService", responseStr);

                        responseStr = responsePrevProcess(responseStr);

                        Gson gson = new Gson();
                        MsgResponseBase res = gson.fromJson(responseStr, MsgResponseBase.class);

                        if (res != null && res.isSuccess()) {
                            // 通知UI
                            notifyMsg(handler, MSG_SUBMIT_COMPONENT_SAMPLING_INSPECTION_SUCCESS, res);
                        }
                        else {
                            notifyMsg(handler, MSG_SUBMIT_COMPONENT_SAMPLING_INSPECTION_FAILED);
                        }
                    }
                    else {
                        notifyMsg(handler, MSG_SUBMIT_COMPONENT_SAMPLING_INSPECTION_FAILED);
                    }
                }
                catch (IOException e) {
                    Log.e("MainService", e.toString());
                    notifyMsg(handler, MSG_SUBMIT_COMPONENT_SAMPLING_INSPECTION_FAILED);
                }
            }
        };

        new Thread(networkTask).start();
        return true;
    }

    // 获取天气
    public boolean sendQueryWeather(final String id, final Handler handler) {
        if (getLoginModel() == null || !getLoginModel().isLoginSuccess()) {
            return false;
        }

        if (id.length() == 0) {
            return false;
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    String url = serverBaseUrl + "/APP.ashx?Type=GetWeather";

                    FormBody body = new FormBody.Builder()
                            .add("Token", getLoginModel().getToken())
                            .add("ProjectID", id)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();

                        Log.i("MainService", responseStr);

                        responseStr = responsePrevProcess(responseStr);

                        try {
                            Gson gson = new GsonBuilder()
                                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                    .create();

                            WeatherModel res = gson.fromJson(responseStr, WeatherModel.class);

                            if (res != null && res.isSuccess()) {
                                // 通知UI
                                weatherModel = res;
                                notifyMsg(handler, MSG_QUERY_WEATHER_SUCCESS, res);
                            }
                            else {
                                notifyMsg(handler, MSG_QUERY_WEATHER_FAILED);
                            }
                        }
                        catch (Exception ex) {
                            Log.e("MainService", ex.toString());
                            notifyMsg(handler, MSG_QUERY_WEATHER_FAILED);
                        }
                    }
                    else {
                        notifyMsg(handler, MSG_QUERY_WEATHER_FAILED);
                    }
                }
                catch (IOException e) {
                    notifyMsg(handler, MSG_QUERY_WEATHER_FAILED);
                }
            }
        };

        new Thread(networkTask).start();
        return true;
    }

}
