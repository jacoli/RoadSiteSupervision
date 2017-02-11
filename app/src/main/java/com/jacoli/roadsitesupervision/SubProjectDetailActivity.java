package com.jacoli.roadsitesupervision;

import android.app.Activity;
import android.os.Bundle;

import com.jacoli.roadsitesupervision.views.MyToast;

public class SubProjectDetailActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_project_detail);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("施工旁站");

//        Intent intent = getIntent();
//        String sub = intent.getStringExtra("ComponentID");
//        if (MainService.getInstance().sendProjectDetailQuery(projectId, handler)) {
//            MyToast.showMessage(getBaseContext(), "获取项目详情中");
//        }
//        else {
//            MyToast.showMessage(getBaseContext(), "获取项目详情失败");
//        }
    }
}
