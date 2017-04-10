package com.jacoli.roadsitesupervision;

import android.app.Fragment;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by lichuange on 17/3/26.
 */

public class CommonFragment extends Fragment {

    static class MyHandler extends Handler {
        WeakReference<CommonFragment> weakActivity;

        public MyHandler(CommonFragment fragment){
            weakActivity = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakActivity != null) {
                weakActivity.get().onResponse(msg.what);
                weakActivity.get().onResponse(msg.what, msg.obj);
            }
        }
    }

    public MyHandler handler = new MyHandler(this);

    public void onResponse(int msgCode) {
    }

    public void onResponse(int msgCode, Object obj) {
    }



}
