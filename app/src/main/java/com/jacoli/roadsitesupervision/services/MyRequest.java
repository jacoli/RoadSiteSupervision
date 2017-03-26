package com.jacoli.roadsitesupervision.services;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Created by lichuange on 17/3/26.
 */

public class MyRequest {
    private RequestProcessor processor;
    private int successMsg;
    private int failureMsg;
    private Handler handler;

    MyRequest(RequestProcessor processor, int successMsg, int failureMsg, Handler handler) {
        this.processor = processor;
        this.successMsg = successMsg;
        this.failureMsg = failureMsg;
        this.handler = handler;
    }

    private String responsePrevProcess(String inString) {
        return inString;
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

    boolean run() {
        if (processor != null) {
            Runnable networkTask = new Runnable() {

                @Override
                public void run() {
                    try {
                        Response response = processor.execute();

                        if (response.isSuccessful()) {
                            String responseStr = response.body().string();

                            Log.i("MainService", responseStr);

                            responseStr = responsePrevProcess(responseStr);

                            try {
                                Gson gson = new GsonBuilder()
                                        .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                        .create();

                                MsgResponseBase res = processor.parse(responseStr, gson);

                                if (res != null && res.isSuccess()) {
                                    processor.onSuccess(res);
                                    notifyMsg(handler, successMsg, res);
                                }
                                else {
                                    notifyMsg(handler, failureMsg);
                                }
                            }
                            catch (Exception ex) {
                                Log.e("MainService", ex.toString());
                                notifyMsg(handler, failureMsg);
                            }
                        }
                        else {
                            notifyMsg(handler, failureMsg);
                        }
                    }
                    catch (IOException e) {
                        notifyMsg(handler, failureMsg);
                    }
                }
            };

            new Thread(networkTask).start();
            return true;
        }

        return false;
    }
}
