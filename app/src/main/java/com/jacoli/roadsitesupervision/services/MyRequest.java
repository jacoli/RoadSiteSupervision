package com.jacoli.roadsitesupervision.services;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by lichuange on 17/3/26.
 */

public class MyRequest {
    private RequestAndResponseHandler processor;
    private int successMsg;
    private int failureMsg;
    private Handler handler;

    MyRequest(Handler handler, int successMsg, int failureMsg, RequestAndResponseHandler processor) {
        this.handler = handler;
        this.successMsg = successMsg;
        this.failureMsg = failureMsg;
        this.processor = processor;
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
        if (processor != null && handler != null && successMsg > 0 && failureMsg > 0) {
            Runnable networkTask = new Runnable() {

                @Override
                public void run() {
                    try {
                        Response response = processor.buildRequestAndWaitingResponse();

                        if (response.isSuccessful()) {
                            String responseJsonString = response.body().string();

                            Log.i("MyRequest", responseJsonString);

                            responseJsonString = responsePrevProcess(responseJsonString);

                            try {
                                Gson gson = new GsonBuilder()
                                        .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                        .create();

                                MsgResponseBase model = processor.jsonModelParsedFromResponseString(responseJsonString, gson);

                                if (model != null && model.isSuccess()) {
                                    processor.onSuccessHandleBeforeNotify(model);
                                    notifyMsg(handler, successMsg, model);
                                }
                                else {
                                    notifyMsg(handler, failureMsg);
                                }
                            }
                            catch (Exception ex) {
                                Log.e("MyRequest", ex.toString());
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
