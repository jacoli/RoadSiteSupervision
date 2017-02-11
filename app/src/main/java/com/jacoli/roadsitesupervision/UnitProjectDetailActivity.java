package com.jacoli.roadsitesupervision;

import android.app.Activity;
import android.os.Bundle;

public class UnitProjectDetailActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_project_detail);

        createTitleBar();
        titleBar.setLeftText("返回");

//        Intent intent = getIntent();
//        String sub = intent.getStringExtra("ComponentID");
//        if (MainService.getInstance().sendProjectDetailQuery(projectId, handler)) {
//            Toast.makeText(getBaseContext(), "获取项目详情中", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Toast.makeText(getBaseContext(), "获取项目详情失败", Toast.LENGTH_SHORT).show();
//        }
    }
}
