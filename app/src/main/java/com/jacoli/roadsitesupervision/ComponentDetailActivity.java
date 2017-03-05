package com.jacoli.roadsitesupervision;

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

import com.jacoli.roadsitesupervision.services.ActiveComponetResp;
import com.jacoli.roadsitesupervision.services.ComponentDetailModel;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.UnitProjectModel;
import com.jacoli.roadsitesupervision.views.MyToast;

import org.apmem.tools.layouts.FlowLayout;

public class ComponentDetailActivity extends CommonActivity {

    private ComponentDetailModel model;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_detail);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("施工旁站");

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if (MainService.getInstance().sendComponentDetailQuery(id, handler)) {
            Toast.makeText(getBaseContext(), "获取构件详情中", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getBaseContext(), "获取构件详情失败", Toast.LENGTH_SHORT).show();
        }

        name = intent.getStringExtra("name");
        String text = name + " 旁站情况:";
        TextView textView = (TextView) findViewById(R.id.project_name_text);
        textView.setText(text);
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_QUERY_COMPONENT_DETAIL_SUCCESS:
                MyToast.showMessage(getBaseContext(), "获取构件详情成功");
                model = (ComponentDetailModel) obj;
                initSubviewsAfterFetchSuccess();
                break;
            case MainService.MSG_QUERY_COMPONENT_DETAIL_FAILED:
                Toast.makeText(getBaseContext(), "获取构件详情失败", Toast.LENGTH_SHORT).show();
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

        for (final ComponentDetailModel.PZModel pzModel : model.getItems()) {
            Button button = new Button(this);

            int size = getResources().getDimensionPixelSize(R.dimen.pz_button_size);
            int margin_v = getResources().getDimensionPixelSize(R.dimen.pz_button_vertical_margin);
            int margin_h = getResources().getDimensionPixelSize(R.dimen.pz_button_horizontal_margin);

            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size, size);
            layoutParams.setMargins(margin_h, margin_v, margin_h, margin_v);
            button.setLayoutParams(layoutParams);

            button.setPadding(5,5,5,5);
            button.setText(pzModel.getPZName());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pzModel.getStatus() == 0) {
                        showPZDetailActivity(pzModel);
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ComponentDetailActivity.this);
                        builder.setTitle("提示");
                        builder.setMessage("旁站表已经归档，不支持修改，归档操作只能在Web里面人工操作");
                        builder.setPositiveButton("确定", null);
                        builder.create().show();
                    }
                }
            });

            if (pzModel.getStatus() == 0) {
                button.setBackgroundColor(Color.GRAY);
            }
            else {
                button.setBackgroundColor(Color.GREEN);
            }

            flowLayout.addView(button);
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

    private void showPZDetailActivity(ComponentDetailModel.PZModel pzModel) {
        Intent intent = new Intent(this ,PZDetailActivity.class);
        intent.putExtra("id", pzModel.getID());
        intent.putExtra("name", pzModel.getPZName());
        intent.putExtra("project_name", name);
        startActivity(intent);
    }
}
