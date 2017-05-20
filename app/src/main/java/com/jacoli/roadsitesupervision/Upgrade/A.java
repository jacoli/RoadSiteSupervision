package com.jacoli.roadsitesupervision.Upgrade;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.BuildConfig;
import com.jacoli.roadsitesupervision.Upgrade.config.SystemParams;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by Song on 2016/11/2.
 */
public class A extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemParams.init(this);

        PushAgent mPushAgent = PushAgent.getInstance(this);

        mPushAgent.setNotificaitonOnForeground(false);

        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.i("RemoteNotify", "deviceToken = " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

        mPushAgent.setDebugMode(BuildConfig.DEBUG);

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Log.i("RemoteNotify", "notificationClickHandler msg = " + msg.custom + "extra:" + msg.extra.toString());
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);


        UmengMessageHandler messageHandler = new UmengMessageHandler(){

            @Override
            public void dealWithNotificationMessage(Context var1, UMessage var2) {
                super.dealWithNotificationMessage(var1, var2);

                Log.i("RemoteNotify", "dealWithNotificationMessage msg = " + var2.custom + "extra:" + var2.extra.toString());
            }

            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                Log.i("RemoteNotify", "msg = " + msg.custom);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);
    }
}
