package com.jacoli.roadsitesupervision.EasyRequest;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import okhttp3.Response;

/**
 * Created by lichuange on 17/4/3.
 */

public class EasyRequest {
    private Processor processor;
    private Handler handler;
    private Callbacks callbacks;

    public EasyRequest(Processor processor, Handler handler, Callbacks callbacks) {
        this.processor = processor;
        this.handler = handler;
        this.callbacks = callbacks;
    }

    public void asyncSend() {
        if (processor == null) {
            onFailed("请求参数错误");
            return;
        }

        Runnable networkTask = new Runnable() {

            @Override
            public void run() {
                try {
                    Response response = processor.buildRequestAndWaitingResponse();

                    if (response.isSuccessful()) {
                        String responseJsonString = response.body().string();

                        Log.i("EasyRequest", responseJsonString);

                        try {
                            Gson gson = new GsonBuilder()
                                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                    .create();

                            ResponseBase model = processor.jsonModelParsedFromResponseString(responseJsonString, gson);

                            if (model != null && model.isSuccess()) {
                                processor.onSuccessHandleBeforeNotify(model);
                                onSuccess(model);
                            }
                            else {
                                onFailed(model == null ? "" : model.getMsg());
                            }
                        }
                        catch (Exception ex) {
                            Log.e("MyRequest", ex.toString());
                            onFailed("");
                        }
                    }
                    else {
                        onFailed("");
                    }
                }
                catch (IOException e) {
                    onFailed("");
                }
            }
        };

        new Thread(networkTask).start();
    }

    private void onSuccess(final ResponseBase responseModel) {
        if (handler != null && callbacks != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callbacks.onSuccess(responseModel);
                }
            });
        }
    }

    private void onFailed(final String error) {
        if (handler != null && callbacks != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callbacks.onFailed(error);
                }
            });
        }
    }
}
