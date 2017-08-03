package com.jacoli.roadsitesupervision.Upgrade;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.BuildConfig;
import com.jacoli.roadsitesupervision.Upgrade.config.SystemParams;
import com.jacoli.roadsitesupervision.services.Utils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.simple.eventbus.EventBus;

/**
 * Created by Song on 2016/11/2.
 */
public class A extends Application {

    public PushAgent mPushAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        SystemParams.init(this);

        mPushAgent = PushAgent.getInstance(this);

        mPushAgent.setNotificaitonOnForeground(false);
        mPushAgent.setResourcePackageName("com.jacoli.roadsitesupervision");

        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                EventBus.getDefault().post(new RemoteNotificationRegistered());
                //注册成功会返回device token
                Log.i("RemoteNotify", "deviceToken = " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

        mPushAgent.setDebugMode(BuildConfig.DEBUG);

        // 点击通知有四种处理模式，如果选择自定义处理方式，则会调用到下面的处理函数。
        // 暂不考虑使用自定义模式
//        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
//            @Override
//            public void dealWithCustomAction(Context context, UMessage msg) {
//                Log.i("RemoteNotify", "notificationClickHandler msg = " + msg.custom + "extra:" + msg.extra.toString());
//                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//            }
//        };
//        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        // 收到推送，一种是推送通知，一种是自定义消息
        UmengMessageHandler messageHandler = new UmengMessageHandler(){

            // 推送通知
            @Override
            public void dealWithNotificationMessage(Context var1, UMessage var2) {
                super.dealWithNotificationMessage(var1, var2);

                if (var2.extra != null) {
                    String type = var2.extra.get("type");
                    if (!Utils.isStringEmpty(type)) {
                        EventBus.getDefault().post(new RemoteNotificationMsg(type));
                    }
                }
            }

            // 自定义消息
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                //Log.i("RemoteNotify", "msg = " + msg.custom);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        initImagloader(getApplicationContext());
    }

    private void initImagloader(Context context) {
//        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
//                "photoview/Cache");// 获取到缓存的目录地址
//        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                // 线程池内加载的数量
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                //.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }
}
