package com.jacoli.roadsitesupervision;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import java.lang.ref.WeakReference;

public class CommonActivity extends Activity {

    static class MyHandler extends Handler {
        WeakReference<CommonActivity> weakActivity;

        public MyHandler(CommonActivity fragment){
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public TitleBar titleBar;

    public void createTitleBar() {
        titleBar = (TitleBar) findViewById(R.id.title_bar);

        titleBar.setImmersive(true);

        //titleBar.setBackgroundColor(CommonActivity.getColor(getBaseContext(), R.color.bg_title_bar));

        titleBar.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.bg_title_bar));

        //titleBar.setLeftImageResource(R.mipmap.back_green);
        titleBar.setLeftText("返回");
        titleBar.setLeftTextColor(Color.WHITE);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleBar.setTitle("文章详情");
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setSubTitleColor(Color.GREEN);
        titleBar.setDividerColor(Color.GRAY);
    }
}
