package com.jacoli.roadsitesupervision;

import android.app.Activity;
import android.os.Bundle;

public class ComponetDetailActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_componet_detail);

        createTitleBar();
        titleBar.setLeftText("返回");
    }
}
