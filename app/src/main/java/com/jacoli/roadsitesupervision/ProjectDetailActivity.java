package com.jacoli.roadsitesupervision;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.ProjectDetailModel;
import com.jacoli.roadsitesupervision.services.Utils;
import com.jacoli.roadsitesupervision.views.MyToast;

import org.apmem.tools.layouts.FlowLayout;

public class ProjectDetailActivity extends CommonActivity {

    private ProjectDetailModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("施工旁站");

        TextView operatorTextView = (TextView) findViewById(R.id.operator_text);
        String operator = "检查人：" + MainService.getInstance().getLoginModel().getName();
        operatorTextView.setText(operator);

        TextView dateTextView = (TextView) findViewById(R.id.date_text);
        String date = Utils.getCurrentDateStr();
        dateTextView.setText(date);

        TextView weatherTextView = (TextView) findViewById(R.id.weather_text);
        String weather = "天气：" + "晴" +  "气温：" + "30";
        weatherTextView.setText(weather);

        if (!MainService.getInstance().sendProjectDetailQuery(handler)) {
            MyToast.showMessage(getBaseContext(), "获取项目详情成功");
        }
    }

    public void initSubviewsAfterFetchSuccess() {
        if (model == null) {
            return;
        }

        TextView projectNameTextView = (TextView) findViewById(R.id.project_name_text);
        String projectName = "单位工程：" + model.getUnitProjectName();
        projectNameTextView.setText(projectName);

        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        for (ProjectDetailModel.SubProjectModel subProjectModel : model.getItems()) {
            Button button = new Button(this);

            int size = getResources().getDimensionPixelSize(R.dimen.project_detail_button_size);
            int margin_v = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_v);
            int margin_h = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_h);

            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size, size);
            layoutParams.setMargins(margin_h, margin_v, margin_h, margin_v);
            button.setLayoutParams(layoutParams);

            String text = "" + subProjectModel.getOrdinal();
            button.setText(text);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO
                }
            });

            if (subProjectModel.getProgress() == 1 || subProjectModel.getProgress() == 2) {
                button.setBackgroundColor(Color.GREEN);
            }
            else {
                if (subProjectModel.getPZStatus() == 2) {
                    button.setBackgroundColor(Color.RED);
                }
                else {
                    button.setBackgroundColor(Color.GRAY);
                }
            }

            flowLayout.addView(button);
        }
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_QUERY_PROJECT_DETAIL_SUCCESS:
                MyToast.showMessage(getBaseContext(), "获取项目详情成功");
                model = (ProjectDetailModel) obj;
                initSubviewsAfterFetchSuccess();
                break;
            case MainService.MSG_QUERY_PROJECT_DETAIL_FAILED:
                Toast.makeText(getBaseContext(), "获取项目详情失败", Toast.LENGTH_SHORT).show();
                break;
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
