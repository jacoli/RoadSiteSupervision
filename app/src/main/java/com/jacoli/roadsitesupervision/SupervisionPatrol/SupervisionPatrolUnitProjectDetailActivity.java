package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.ComponentDetailActivity;
import com.jacoli.roadsitesupervision.InspectionDetailActivity;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.SamplingInspectionActivity;
import com.jacoli.roadsitesupervision.services.ActiveComponetResp;
import com.jacoli.roadsitesupervision.services.FinishComponentResp;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.UnitProjectModel;
import com.jacoli.roadsitesupervision.views.MyToast;

import org.apmem.tools.layouts.FlowLayout;

public class SupervisionPatrolUnitProjectDetailActivity extends CommonActivity {

    private UnitProjectModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_patrol_unit_project_detail);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("选择工程构件");

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if (MainService.getInstance().sendUnitProjectDetailQuery(id, handler)) {
            Toast.makeText(getBaseContext(), "获取单位工程详情中", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getBaseContext(), "获取单位工程详情失败", Toast.LENGTH_SHORT).show();
        }

        String name = intent.getStringExtra("title");
        TextView textView = (TextView) findViewById(R.id.project_name_text);
        textView.setText(name);
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
                button.setBackgroundColor(Color.GRAY);
                button.setText(componentModel.getName());

                updateButtonActionsForPZ(button, subProjectModel, componentModel);

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

    // 旁站模式，按钮操作事件处理
    private void updateButtonActionsForPZ(Button button, final UnitProjectModel.SubProjectModel subProjectModel, final UnitProjectModel.ComponentModel componentModel) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = getIntent().getStringExtra("title") + "-" + subProjectModel.getName() + "-" + componentModel.getName();
                Intent intent = new Intent();
                intent.putExtra("title", title);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
