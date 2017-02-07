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

public class CommonActivity extends Activity {

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
