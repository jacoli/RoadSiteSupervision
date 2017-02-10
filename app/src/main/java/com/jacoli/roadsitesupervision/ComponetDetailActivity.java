package com.jacoli.roadsitesupervision;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.services.MainService;

import org.apmem.tools.layouts.FlowLayout;

public class ComponetDetailActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_componet_detail);

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

        FlowLayout layout = (FlowLayout) findViewById(R.id.flow_layout);

//        TextView textView0 = new TextView(this);
//
//        textView0.setLayoutParams(new FlowLayout.LayoutParams(300, 128));
//        String text0 = "#0";
//        textView0.setText(text0);
//        textView0.setBackgroundColor(Color.GREEN);
//        textView0.setGravity(Gravity.CENTER);
//
//        layout.addView(textView0);
//
//        for (int i = 0; i < 200; ++i) {
//            TextView textView = new TextView(this);
//
//            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.layout_item_size), getResources().getDimensionPixelSize(R.dimen.layout_item_size));
//            layoutParams.setMargins(5, 10, 15, 20);
//
//            textView.setLayoutParams(layoutParams);
//            String text = "#" + i;
//            textView.setText(text);
//            textView.setBackgroundColor(Color.GREEN);
//            textView.setGravity(Gravity.CENTER);
//
//            layout.addView(textView);
//        }
    }

    @Override
    public void onResponse(int msgCode) {
        switch (msgCode) {
//            case MainService.MSG_QUERY_PROJECT_DETAIL_SUCCESS:
//                MyToast.showMessage(getBaseContext(), "获取项目详情成功");
//                updateMainInfoViews();
//                MainService.getInstance().sendGetSignCheck(projectId, handler);
//                break;
//            case MainService.MSG_QUERY_PROJECT_DETAIL_FAILED:
//                Toast.makeText(getBaseContext(), "获取项目详情失败", Toast.LENGTH_SHORT).show();
//                break;
//            case MainService.MSG_GET_SIGN_CHECK_SUCCESS:
//                MyToast.showMessage(getBaseContext(), "获取检查标志成功");
//                updateSignItemsViews();
//                updateSensorItemsViews();
//                curScanedSignCode = null;
//                break;
//            case MainService.MSG_GET_SIGN_CHECK_FAILED:
//                Toast.makeText(getBaseContext(), "获取检查标志失败", Toast.LENGTH_SHORT).show();
//                curScanedSignCode = null;
//                break;
//            default:
//                break;
        }
    }
}
