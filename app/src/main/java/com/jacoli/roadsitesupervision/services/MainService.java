package com.jacoli.roadsitesupervision.services;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainService {
    public static final int MSG_LOGIN_SUCCESS = 0x1001;
    public static final int MSG_LOGIN_FAILED = 0x1002;
    public static final int MSG_LOGOUT_SUCCESS = 0x1003;
    public static final int MSG_LOGOUT_FAILED = 0x1004;
    public static final int MSG_QUERY_PROJECTS_SUCCESS = 0x2001;
    public static final int MSG_QUERY_PROJECTS_FAILED = 0x2002;
    public static final int MSG_QUERY_PROJECT_DETAIL_SUCCESS = 0x3001;
    public static final int MSG_QUERY_PROJECT_DETAIL_FAILED = 0x3002;
    public static final int MSG_ACTIVE_UNIT_PROJECT_SUCCESS = 0x4001;
    public static final int MSG_ACTIVE_UNIT_PROJECT_FAILED = 0x4002;
    public static final int MSG_QUERY_UNIT_PROJECT_DETAIL_SUCCESS = 0x5001;
    public static final int MSG_QUERY_UNIT_PROJECT_DETAIL_FAILED = 0x5002;
    public static final int MSG_ACTIVE_COMPONENT_SUCCESS = 0x6001;
    public static final int MSG_ACTIVE_COMPONENT_FAILED = 0x6002;
    public static final int MSG_DELETE_SENSOR_CHECK_SUCCESS = 0x7001;
    public static final int MSG_DELETE_SENSOR_CHECK_FAILED = 0x7002;
    public static final int MSG_SEND_EXPLORE_SUCCESS = 0x8001;
    public static final int MSG_SEND_EXPLORE_FAILED = 0x8002;
    public static final int MSG_DELETE_ALL_SENSOR_CHECK_SUCCESS = 0x9001;
    public static final int MSG_DELETE_ALL_SENSOR_CHECK_FAILED = 0x9002;

    public static final int MSG_LOAD_EXPLORE_PARAMS_META_SUCCESS = 0x10001;

    public static final int MSG_DELETE_SIGN_CHECK_SUCCESS = 0xa001;
    public static final int MSG_DELETE_SIGN_CHECK_FAILED = 0xa002;

    public String serverBaseUrl = "http://118.178.92.22:8001";

    private OkHttpClient httpClient;
    private LoginModel loginModel;

    private static MainService ourInstance = new MainService();

    public static MainService getInstance() {
        return ourInstance;
    }

    private MainService() {
        httpClient = new OkHttpClient();
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

                        //responseStr = "{\"Status\":0,\"Msg\":\"OK\",\"ProjectName\":\"杭州到宁波\",\"LineName\":\"杭千高速\",\"ControlStartStack\":\"K0+-760\",\"ControlEndStack\":\"K2+720\",\"GZ1\":\"K1+100\",\"GZ1Lon\":\"\",\"GZ1Lat\":\"\",\"SchemeImage\":\"http://139.196.200.114:80/Maintain/rule/SchemeImage/1-12.png\",\"SignCount\":\"12\",\"items\":[{\"SignNumber\":\"b32\",\"SignName\":\"解除限速60\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-12-5.png\",\"StackNumber\":\"K0+-760\"},{\"SignNumber\":\"b38\",\"SignName\":\"解除禁止超车\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-14.png\",\"StackNumber\":\"K0+-760\"},{\"SignNumber\":\"b3\",\"SignName\":\"施工长度标志\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-3.png\",\"StackNumber\":\"K1+100\"},{\"SignNumber\":\"b47\",\"SignName\":\"附设警示灯的路栏\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-3-6.png\",\"StackNumber\":\"K1+100\"},{\"SignNumber\":\"b14\",\"SignName\":\"向右导向\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-7-2.png\",\"StackNumber\":\"K1+160\"},{\"SignNumber\":\"b5\",\"SignName\":\"两车道向右变一车道\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-5-1.png\",\"StackNumber\":\"K1+595\"},{\"SignNumber\":\"b36\",\"SignName\":\"解除限速20\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-12-9.png\",\"StackNumber\":\"K1+595\"},{\"SignNumber\":\"b50\",\"SignName\":\"夜间语音提示设施\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-3-9.png\",\"StackNumber\":\"K1+595\"},{\"SignNumber\":\"b54\",\"SignName\":\"警示频闪灯\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-3-11.png\",\"StackNumber\":\"K1+595\"},{\"SignNumber\":\"b23\",\"SignName\":\"限速60\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-11-5.png\",\"StackNumber\":\"K1+770\"},{\"SignNumber\":\"b21\",\"SignName\":\"限速80\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-11-3.png\",\"StackNumber\":\"K1+970\"},{\"SignNumber\":\"b2\",\"SignName\":\"施工距离标志\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-2.png\",\"StackNumber\":\"K2+720\"}]}";
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

                        //responseStr = "{\"Status\":0,\"Msg\":\"OK\",\"ProjectName\":\"杭州到宁波\",\"LineName\":\"杭千高速\",\"ControlStartStack\":\"K0+-760\",\"ControlEndStack\":\"K2+720\",\"GZ1\":\"K1+100\",\"GZ1Lon\":\"\",\"GZ1Lat\":\"\",\"SchemeImage\":\"http://139.196.200.114:80/Maintain/rule/SchemeImage/1-12.png\",\"SignCount\":\"12\",\"items\":[{\"SignNumber\":\"b32\",\"SignName\":\"解除限速60\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-12-5.png\",\"StackNumber\":\"K0+-760\"},{\"SignNumber\":\"b38\",\"SignName\":\"解除禁止超车\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-14.png\",\"StackNumber\":\"K0+-760\"},{\"SignNumber\":\"b3\",\"SignName\":\"施工长度标志\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-3.png\",\"StackNumber\":\"K1+100\"},{\"SignNumber\":\"b47\",\"SignName\":\"附设警示灯的路栏\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-3-6.png\",\"StackNumber\":\"K1+100\"},{\"SignNumber\":\"b14\",\"SignName\":\"向右导向\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-7-2.png\",\"StackNumber\":\"K1+160\"},{\"SignNumber\":\"b5\",\"SignName\":\"两车道向右变一车道\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-5-1.png\",\"StackNumber\":\"K1+595\"},{\"SignNumber\":\"b36\",\"SignName\":\"解除限速20\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-12-9.png\",\"StackNumber\":\"K1+595\"},{\"SignNumber\":\"b50\",\"SignName\":\"夜间语音提示设施\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-3-9.png\",\"StackNumber\":\"K1+595\"},{\"SignNumber\":\"b54\",\"SignName\":\"警示频闪灯\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-3-11.png\",\"StackNumber\":\"K1+595\"},{\"SignNumber\":\"b23\",\"SignName\":\"限速60\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-11-5.png\",\"StackNumber\":\"K1+770\"},{\"SignNumber\":\"b21\",\"SignName\":\"限速80\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-11-3.png\",\"StackNumber\":\"K1+970\"},{\"SignNumber\":\"b2\",\"SignName\":\"施工距离标志\",\"SignImageURL\":\"http://139.196.200.114:80/Maintain/rule/SignImage/WEB/A-1-2.png\",\"StackNumber\":\"K2+720\"}]}";
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
}
