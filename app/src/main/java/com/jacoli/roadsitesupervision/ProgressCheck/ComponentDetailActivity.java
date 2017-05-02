package com.jacoli.roadsitesupervision.ProgressCheck;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.SupervisionPatrol.SelectPersonActivity;
import com.jacoli.roadsitesupervision.services.Utils;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

public class ComponentDetailActivity extends CommonActivity {

    private ProgressCheckItemsModel checkItemsModel;
    private ProgressItemsModel.Item selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_check_component_detail);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("进度巡查");

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        TextView textView = (TextView) findViewById(R.id.project_name_text);
        textView.setText(name);


        Service.getInstance().sendQueryProgressItems(new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                ProgressItemsModel model = (ProgressItemsModel) responseModel;
                updateItemsView(model);
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        Service.getInstance().sendQueryProgressCheckItems(getIntent().getStringExtra("id"), new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                checkItemsModel = (ProgressCheckItemsModel) responseModel;
                updateCheckItemsView();
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        Button button1 = (Button) findViewById(R.id.submit_btn_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit1();
            }
        });

        Button button2 = (Button) findViewById(R.id.submit_btn_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit2();
            }
        });
    }

    private void submit1() {
        if (selectedItem == null) {
            Toast.makeText(getBaseContext(), "请选择工序", Toast.LENGTH_SHORT).show();
            return;
        }

        Service.getInstance().sendSubmitProgressCheck(getIntent().getStringExtra("id"), selectedItem.getID(), new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                Toast.makeText(getBaseContext(), "提交成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void submit2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定归档？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                doDocument();
            }
        });
        builder.setNegativeButton("否", null);
        builder.create().show();
    }

    private void doDocument() {
        Service.getInstance().sendFileProgressCheck(getIntent().getStringExtra("id"), new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                Toast.makeText(getBaseContext(), "归档成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateCheckItemsView() {
        String text = "历史状态：";
        if (checkItemsModel != null) {
            for (ProgressCheckItemsModel.Item item : checkItemsModel.getItems()) {
                text += item.getProcessName() + " " + item.getAddTime() + "\n";
            }
        }
        text += "最新状态：";
        if (selectedItem != null) {
            text += selectedItem.getName();
        }

        TextView textView = (TextView) findViewById(R.id.progress_check_status);
        textView.setText(text);
    }

    public void updateItemsView(ProgressItemsModel model) {
        if (model == null) {
            return;
        }

        List<ProgressItemsModel.Item> items = model.getFlatSubItems();

        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flow_layout);

        for (final ProgressItemsModel.Item item : items) {

            if (item.getLevel() == 1) {
                // rows
                Button button = new Button(this);

                int size = getResources().getDimensionPixelSize(R.dimen.unit_project_detail_button_size);
                int margin_v = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_v);
                int margin_h = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_h);

                FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size, size);
                layoutParams.setMargins(margin_h, margin_v, margin_h, margin_v);
                button.setLayoutParams(layoutParams);
                button.setBackgroundColor(Color.GRAY);
                button.setText(item.getName());

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedItem = item;
                        updateCheckItemsView();
                    }
                });

                flowLayout.addView(button);
            } else {
                // header
                TextView textView = new TextView(this);
                textView.setText(item.getName());
                int textViewSize = getResources().getDimensionPixelSize(R.dimen.unit_project_detail_button_size);
                FlowLayout.LayoutParams textLayoutParams = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textLayoutParams.setNewLine(true);
                int textView_margin_v = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_v);
                int textView_margin_h = getResources().getDimensionPixelSize(R.dimen.project_detail_button_margin_h);
                textLayoutParams.setMargins(0, textView_margin_v, 0, 0);
                flowLayout.addView(textView, textLayoutParams);
            }
        }
    }
}
