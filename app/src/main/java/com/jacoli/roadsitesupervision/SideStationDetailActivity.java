package com.jacoli.roadsitesupervision;

import android.app.Activity;
import android.os.Bundle;

public class SideStationDetailActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_station_detail);

        createTitleBar();
        titleBar.setLeftText("返回");
    }
}
