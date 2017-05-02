package com.jacoli.roadsitesupervision.ProgressCheck;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.UnitProjectDetailForInspectionActivity;
import com.jacoli.roadsitesupervision.services.ActiveUnitProjectResp;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.ProjectDetailModel;
import com.jacoli.roadsitesupervision.services.Utils;
import com.jacoli.roadsitesupervision.views.MyToast;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailActivity extends CommonActivity {

    private ProjectDetailModel model;
    private List<View> itemViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_check_project_detail);

        itemViews = new ArrayList<>();

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("进度巡查");

        TextView operatorTextView = (TextView) findViewById(R.id.operator_text);
        String operator = "检查人：" + MainService.getInstance().getLoginModel().getName();
        operatorTextView.setText(operator);

        TextView dateTextView = (TextView) findViewById(R.id.date_text);
        String date = "日期：" + Utils.getCurrentDateStr();
        dateTextView.setText(date);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // send requests to update subviews
        if (!MainService.getInstance().sendProjectDetailQuery(handler)) {
            MyToast.showMessage(getBaseContext(), "获取项目详情失败");
        }

        // get weather
        MainService.getInstance().sendQueryWeather(MainService.getInstance().getLoginModel().getProjectID(), handler);
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_QUERY_PROJECT_DETAIL_SUCCESS:
                model = (ProjectDetailModel) obj;
                updateWeatherView();
                updateUnitProjectItemViews();
                break;
            case MainService.MSG_QUERY_PROJECT_DETAIL_FAILED:
                Toast.makeText(getBaseContext(), "获取项目详情失败", Toast.LENGTH_SHORT).show();
                break;
            case MainService.MSG_QUERY_WEATHER_SUCCESS:
                updateWeatherView();
                break;
            case MainService.MSG_QUERY_WEATHER_FAILED:
                Toast.makeText(getBaseContext(), "获取天气失败", Toast.LENGTH_SHORT).show();
                break;
            case MainService.MSG_ACTIVE_UNIT_PROJECT_SUCCESS:
                MyToast.showMessage(getBaseContext(), "激活工程成功");
                ActiveUnitProjectResp resp = (ActiveUnitProjectResp) obj;
                updateWithActiveUnitProjectResp(resp);
                break;
            case MainService.MSG_ACTIVE_UNIT_PROJECT_FAILED:
                Toast.makeText(getBaseContext(), "激活工程失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void updateWeatherView() {
        TextView dateTextView = (TextView) findViewById(R.id.weather_text);
        String date = "天气：" + MainService.getInstance().getWeatherModel().getWeather()
                + "   气温："
                + MainService.getInstance().getWeatherModel().getAirTep()
                + "℃";
        dateTextView.setText(date);
    }

    public void updateUnitProjectItemViews() {
        if (model == null) {
            return;
        }

        TextView projectNameTextView = (TextView) findViewById(R.id.project_name_text);
        String projectName = model.getUnitProjectName();
        projectNameTextView.setText(projectName);

        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        for (View view : itemViews) {
            flowLayout.removeView(view);
        }
        itemViews.clear();
        for (ProjectDetailModel.UnitProjectModel unitProjectModel : model.getItems()) {
            Button button = new Button(this);

            int size = getResources().getDimensionPixelSize(R.dimen.project_detail_button_size);
            int margin_v = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_v);
            int margin_h = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_h);

            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size, size);
            layoutParams.setMargins(margin_h, margin_v, margin_h, margin_v);
            button.setLayoutParams(layoutParams);
            button.setPadding(5, 5, 5, 5);
            //button.setTextSize(12);

            button.setText(unitProjectModel.getName());

            updateButtonStatus(button, unitProjectModel.getProgress(), unitProjectModel.getPZStatus());
            updateButtonActions(button, unitProjectModel);

            button.setTag(unitProjectModel.getID());

            flowLayout.addView(button);
            itemViews.add(button);
        }

        addBottomPaddingViewToFlowLayout(flowLayout);
    }

    // fix : flowlayout底部无法滑倒底，增加一个padding
    private void addBottomPaddingViewToFlowLayout(FlowLayout layout) {
        View view = new View(this);
        int size = getResources().getDimensionPixelSize(R.dimen.project_detail_button_size);
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size, size);
        layoutParams.setNewLine(true);
        layout.addView(view, layoutParams);
        itemViews.add(view);
    }

    private void updateWithActiveUnitProjectResp(ActiveUnitProjectResp resp) {
        if (resp == null) {
            return;
        }

        // update model
        ProjectDetailModel.UnitProjectModel unitProjectModel = model.queryUnitProjectModelWithID(resp.getID());
        if (unitProjectModel != null) {
            unitProjectModel.setProgress(resp.getProgress());
            unitProjectModel.setPZStatus(resp.getPZStatus());
        }

        // update view
        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        Button button = (Button) flowLayout.findViewWithTag(resp.getID());
        updateButtonStatus(button, resp.getProgress(), resp.getPZStatus());
    }

    // 旁站模式、巡视模式，按钮状态
    private void updateButtonStatus(Button button, int Progress, int PZStatus) {
        if (button != null) {
            if (Progress == 2) {
                button.setBackgroundColor(Color.RED);
            }
            else if (Progress == 1) {
                button.setBackgroundColor(Color.GREEN);
            }
            else {
                button.setBackgroundColor(Color.GRAY);
            }
        }
    }

    // 旁站模式、巡视模式，按钮操作事件处理
    private void updateButtonActions(Button button, final ProjectDetailModel.UnitProjectModel unitProjectModel) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unitProjectModel.getProgress() == 2) {
                    showUnitProjectDetailActivity(unitProjectModel);
                }
                else if (unitProjectModel.getProgress() == 1) {
                    showUnitProjectDetailActivity(unitProjectModel);
                }
                else {
                    alertToActiveUnitProject(unitProjectModel);
                }
            }
        });
    }

    private void alertToActiveUnitProject(final ProjectDetailModel.UnitProjectModel unitProjectModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(unitProjectModel.getName() + "\n是否开始施工？");

        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                MainService.getInstance().sendActiveUnitProject(unitProjectModel.getID(), handler);
            }
        });

        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });

        builder.create().show();
    }

    private void showUnitProjectDetailActivity(ProjectDetailModel.UnitProjectModel unitProjectModel) {
        Intent intent = new Intent(this ,UnitProjectDetailActivity.class);
        intent.putExtra("id", unitProjectModel.getID());
        intent.putExtra("name", unitProjectModel.getName());
        intent.putExtra("title", getIntent().getStringExtra("title"));
        startActivity(intent);
    }
}
