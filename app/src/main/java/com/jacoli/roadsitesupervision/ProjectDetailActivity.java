package com.jacoli.roadsitesupervision;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.services.ActiveUnitProjectResp;
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
        String projectName = model.getUnitProjectName();
        projectNameTextView.setText(projectName);

        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        for (final ProjectDetailModel.UnitProjectModel unitProjectModel : model.getItems()) {
            Button button = new Button(this);

            int size = getResources().getDimensionPixelSize(R.dimen.project_detail_button_size);
            int margin_v = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_v);
            int margin_h = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_h);

            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size, size);
            layoutParams.setMargins(margin_h, margin_v, margin_h, margin_v);
            button.setLayoutParams(layoutParams);

            button.setText(unitProjectModel.getName());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (unitProjectModel.getPZStatus() == 2) {
                        showUnitProjectDetailActivity(unitProjectModel);
                    }
                    else {
                        if (unitProjectModel.getProgress() == 1 || unitProjectModel.getProgress() == 2) {
                            showUnitProjectDetailActivity(unitProjectModel);
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProjectDetailActivity.this);
                            //builder.setTitle("是否开始施工");
                            builder.setMessage("是否开始施工，工程名称：" + unitProjectModel.getName());

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
                    }
                }
            });

            updateButtonWithStatus(button, unitProjectModel.getProgress(), unitProjectModel.getPZStatus());

            button.setTag(unitProjectModel.getID());

            flowLayout.addView(button);
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
        updateButtonWithStatus(button, resp.getProgress(), resp.getPZStatus());
    }

    private void updateButtonWithStatus(Button button, int Progress, int PZStatus) {
        if (button != null) {
            if (PZStatus == 2) {
                button.setBackgroundColor(Color.RED);
            }
            else {
                if (Progress == 1 || Progress == 2) {
                    button.setBackgroundColor(Color.GREEN);
                }
                else {
                    button.setBackgroundColor(Color.GRAY);
                }
            }
        }
    }

    private void showUnitProjectDetailActivity(ProjectDetailModel.UnitProjectModel unitProjectModel) {
        Intent intent = new Intent(this ,UnitProjectDetailActivity.class);
        intent.putExtra("id", unitProjectModel.getID());
        startActivity(intent);
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
}
