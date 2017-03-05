package com.jacoli.roadsitesupervision;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.services.ActiveComponetResp;
import com.jacoli.roadsitesupervision.services.ActiveUnitProjectResp;
import com.jacoli.roadsitesupervision.services.FinishComponentResp;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.ProjectDetailModel;
import com.jacoli.roadsitesupervision.services.UnitProjectModel;
import com.jacoli.roadsitesupervision.views.MyToast;

import org.apmem.tools.layouts.FlowLayout;

public class UnitProjectDetailForInspectionActivity extends CommonActivity {

    private UnitProjectModel model;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_project_detail_for_inspection);

        type = getIntent().getIntExtra("type", MainService.project_detail_type_pz);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle(getIntent().getStringExtra("title"));

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if (MainService.getInstance().sendUnitProjectDetailQuery(id, handler)) {
            Toast.makeText(getBaseContext(), "获取单位工程详情中", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getBaseContext(), "获取单位工程详情失败", Toast.LENGTH_SHORT).show();
        }

        String name = intent.getStringExtra("name") + "\n巡视情况:";
        TextView textView = (TextView) findViewById(R.id.project_name_text);
        textView.setText(name);

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showComponentDetailActivity();
            }
        });
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_QUERY_UNIT_PROJECT_DETAIL_SUCCESS:
                MyToast.showMessage(getBaseContext(), "获取单位工程详情成功");
                model = (UnitProjectModel) obj;
                initSubviewsAfterFetchSuccess();
                break;
            case MainService.MSG_QUERY_UNIT_PROJECT_DETAIL_FAILED:
                Toast.makeText(getBaseContext(), "获取单位工程详情失败", Toast.LENGTH_SHORT).show();
                break;
            case MainService.MSG_ACTIVE_COMPONENT_SUCCESS:
                //MyToast.showMessage(getBaseContext(), "激活构件成功");
                ActiveComponetResp resp = (ActiveComponetResp) obj;
                updateWithActiveComponentResp(resp);
                break;
            case MainService.MSG_ACTIVE_COMPONENT_FAILED:
                Toast.makeText(getBaseContext(), "激活构件失败", Toast.LENGTH_SHORT).show();
                break;
            case MainService.MSG_FINISH_COMPONENT_SUCCESS:
                //MyToast.showMessage(getBaseContext(), "激活构件成功");
                FinishComponentResp resp2 = (FinishComponentResp) obj;
                updateWithFinishComponentResp(resp2);
                break;
            case MainService.MSG_FINISH_COMPONENT_FAILED:
                Toast.makeText(getBaseContext(), "标记构件完成失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void initSubviewsAfterFetchSuccess() {
        if (model == null) {
            return;
        }

        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flow_layout);

        for (final UnitProjectModel.SubProjectModel subProjectModel : model.getSubProjects()) {
            // header
            TextView textView = new TextView(this);
            textView.setText(subProjectModel.getName());
            int textViewSize = getResources().getDimensionPixelSize(R.dimen.unit_project_detail_button_size);
            FlowLayout.LayoutParams textLayoutParams = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textLayoutParams.setNewLine(true);
            int textView_margin_v = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_v);
            int textView_margin_h = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_h);
            textLayoutParams.setMargins(0, textView_margin_v, 0, 0);
            flowLayout.addView(textView, textLayoutParams);

            // rows
            for (final UnitProjectModel.ComponentModel componentModel : subProjectModel.getComponents()) {
                Button button = new Button(this);

                int size = getResources().getDimensionPixelSize(R.dimen.unit_project_detail_button_size);
                int margin_v = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_v);
                int margin_h = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_h);

                FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size, size);
                layoutParams.setMargins(margin_h, margin_v, margin_h, margin_v);
                button.setLayoutParams(layoutParams);

                button.setText(componentModel.getName());

                updateButtonWithStatus(button, componentModel.getProgress(), componentModel.getPZStatus());
                updateButtonActionsForInspections(button, subProjectModel, componentModel);

                button.setTag(componentModel.getID());

                flowLayout.addView(button);
            }
        }

        addBottomPaddingViewToFlowLayout(flowLayout);
    }

    // fix : flowlayout底部无法滑倒底，增加一个padding
    private void addBottomPaddingViewToFlowLayout(FlowLayout layout) {
        View view = new View(this);
        int size = getResources().getDimensionPixelSize(R.dimen.unit_project_detail_button_size);
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size, size);
        layoutParams.setNewLine(true);
        layout.addView(view, layoutParams);
    }

    private void updateWithActiveComponentResp(ActiveComponetResp resp) {
        if (resp == null) {
            return;
        }

        // update model
        UnitProjectModel.ComponentModel componentModel = model.queryComponentModelWithID(resp.getID());
        if (componentModel != null) {
            componentModel.setProgress(resp.getProgress());
            componentModel.setPZStatus(resp.getPZStatus());
        }

        // update view
        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        Button button = (Button) flowLayout.findViewWithTag(resp.getID());
        updateButtonWithStatus(button, resp.getProgress(), resp.getPZStatus());
    }

    private void updateWithFinishComponentResp(FinishComponentResp resp) {
        if (resp == null) {
            return;
        }

        // update model
        UnitProjectModel.ComponentModel componentModel = model.queryComponentModelWithID(resp.getID());
        if (componentModel != null) {
            componentModel.setProgress(resp.getProgress());
            componentModel.setPZStatus(resp.getPZStatus());
        }

        // update view
        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        Button button = (Button) flowLayout.findViewWithTag(resp.getID());
        updateButtonWithStatus(button, resp.getProgress(), resp.getPZStatus());
    }

    // 旁站模式、巡视模式，按钮状态
    private void updateButtonWithStatus(Button button, int Progress, int PZStatus) {
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

    private void alertToActiveComponent(final UnitProjectModel.SubProjectModel subProjectModel, final UnitProjectModel.ComponentModel componentModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        String text = getIntent().getStringExtra("name") + "-" + subProjectModel.getName() + "-" + componentModel.getName();
        builder.setMessage(text + "\n是否开始施工?");

        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                MainService.getInstance().sendActiveComponent(componentModel.getID(), handler);
            }
        });

        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });

        builder.create().show();
    }

    private void alertToFinishComponent(final UnitProjectModel.SubProjectModel subProjectModel, final UnitProjectModel.ComponentModel componentModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        String text = getIntent().getStringExtra("name") + "-" + subProjectModel.getName() + "-" + componentModel.getName();
        builder.setMessage(text + "\n是否已完成?");

        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                MainService.getInstance().sendFinishComponent(componentModel.getID(), handler);
            }
        });

        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });

        builder.create().show();
    }

    // 巡视模式，按钮操作事件处理
    private void updateButtonActionsForInspections(Button button, final UnitProjectModel.SubProjectModel subProjectModel, final UnitProjectModel.ComponentModel componentModel) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (componentModel.getProgress() == 2) {

                }
                else if (componentModel.getProgress() == 1) {
                    alertToFinishComponent(subProjectModel, componentModel);
                }
                else {
                    alertToActiveComponent(subProjectModel, componentModel);
                }
            }
        });
    }

    private void showComponentDetailActivity() {
        Intent intent = new Intent(this ,InspectionDetailActivity.class);
        intent.putExtra("title", getIntent().getStringExtra("title"));
        intent.putExtra("type", type);
        startActivity(intent);
    }
}

