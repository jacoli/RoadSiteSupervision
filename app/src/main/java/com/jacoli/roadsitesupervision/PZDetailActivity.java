package com.jacoli.roadsitesupervision;

import android.app.Activity;
import android.os.Bundle;

public class PZDetailActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pzdetail);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("施工旁站");
    }
}
