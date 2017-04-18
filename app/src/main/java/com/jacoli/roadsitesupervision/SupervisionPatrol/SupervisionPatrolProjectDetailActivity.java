package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.UnitProjectDetailActivity;
import com.jacoli.roadsitesupervision.UnitProjectDetailForInspectionActivity;
import com.jacoli.roadsitesupervision.services.ActiveUnitProjectResp;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.ProjectDetailModel;
import com.jacoli.roadsitesupervision.services.Utils;
import com.jacoli.roadsitesupervision.views.MyToast;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class SupervisionPatrolProjectDetailActivity extends CommonActivity {

    private ProjectDetailModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_patrol_project_detail);

        createTitleBar();
        titleBar.setLeftText("取消");
        titleBar.setTitle(SupervisionPatrolUtils.title);

        MainService.getInstance().sendProjectDetailQuery(handler);
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_QUERY_PROJECT_DETAIL_SUCCESS:
                model = (ProjectDetailModel) obj;
                updateUnitProjectItemViews();
                break;
            case MainService.MSG_QUERY_PROJECT_DETAIL_FAILED:
                Toast.makeText(getBaseContext(), "获取项目详情失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void updateUnitProjectItemViews() {
        if (model == null) {
            return;
        }

        TextView projectNameTextView = (TextView) findViewById(R.id.project_name_text);
        String projectName = model.getUnitProjectName();
        projectNameTextView.setText(projectName);

        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        for (ProjectDetailModel.UnitProjectModel unitProjectModel : model.getItems()) {
            Button button = new Button(this);

            int size = getResources().getDimensionPixelSize(R.dimen.project_detail_button_size);
            int margin_v = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_v);
            int margin_h = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_h);

            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size, size);
            layoutParams.setMargins(margin_h, margin_v, margin_h, margin_v);
            button.setLayoutParams(layoutParams);
            button.setPadding(5, 5, 5, 5);
            button.setBackgroundColor(Color.GRAY);

            button.setText(unitProjectModel.getName());
            updateButtonActions(button, unitProjectModel);
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

    // 旁站模式、巡视模式，按钮操作事件处理
    private void updateButtonActions(Button button, final ProjectDetailModel.UnitProjectModel unitProjectModel) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUnitProjectDetailActivity(unitProjectModel);
            }
        });
    }

    private void showUnitProjectDetailActivity(ProjectDetailModel.UnitProjectModel unitProjectModel) {
        Intent intent = new Intent(this ,SupervisionPatrolUnitProjectDetailActivity.class);
        intent.putExtra("id", unitProjectModel.getID());
        intent.putExtra("name", unitProjectModel.getName());
        intent.putExtra("title", getIntent().getStringExtra("title"));
        startActivity(intent);
    }
}
