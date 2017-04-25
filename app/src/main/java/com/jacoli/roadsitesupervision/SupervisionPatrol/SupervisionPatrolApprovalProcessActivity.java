package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.AssignedMatterCreateActivity;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.StaffsActivity;
import com.jacoli.roadsitesupervision.services.AssignedMatterDetailModel;
import com.jacoli.roadsitesupervision.services.ImageUrlModel;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.Utils;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class SupervisionPatrolApprovalProcessActivity extends CommonActivity {
    private DetailModel detailModel;
    private String modelId;
    private Button button1;
    private Button button2;
    private Button button3;
    private LinearLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_patrol_approval_process);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("待审批");

        Intent intent = getIntent();
        modelId = intent.getStringExtra("id");

        SupervisionPatrolService.getInstance().sendQueryDetail(modelId, new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                updateSubviews((DetailModel) responseModel);
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        button1 = (Button) findViewById(R.id.submit_btn_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit1();
            }
        });

        button2 = (Button) findViewById(R.id.submit_btn_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit2();
            }
        });

        button3 = (Button) findViewById(R.id.submit_btn_3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit3();
            }
        });

        contentLayout = (LinearLayout) findViewById(R.id.content_layout);

        button1.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.INVISIBLE);
        contentLayout.setVisibility(View.INVISIBLE);
    }


    private void submit1() {
        EditText editText = (EditText) findViewById(R.id.edit_text_content);
        String content = editText.getText().toString();
        if (Utils.isStringEmpty(content)) {
            Toast.makeText(getBaseContext(), "请输入处理意见", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, SelectPersonActivity.class);
        startActivityForResult(intent, SelectPersonActivity.RequestCode);
    }

    private void submit2() {
        EditText editText = (EditText) findViewById(R.id.edit_text_content);
        String content = editText.getText().toString();
        if (Utils.isStringEmpty(content)) {
            Toast.makeText(getBaseContext(), "请输入处理意见", Toast.LENGTH_SHORT).show();
            return;
        }

        SupervisionPatrolService.getInstance().sendSupervisionPatrolApproval(modelId, content, "2", "", new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submit3() {
        SupervisionPatrolService.getInstance().sendSupervisionPatrolApproval(modelId, "", "3", "", new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSubviews(DetailModel model) {
        TextView textView = (TextView) findViewById(R.id.text_view);
        String text = "工程构件：" + model.getProjectPart()
                + "\n检查大项："
                + "\n检查细目："
                + "\n上报人：" + model.getAddByName()
                + "\n上报时间：" + model.getAddTime()
                + "\n\n补充说明：" + model.getDescription();
        textView.setText(text);

        ArrayList<String> imageUrls = new ArrayList<>();
        for (ImageUrlModel imageUrlModel : model.getPhotoList()) {
            imageUrls.add(imageUrlModel.getWebPath());
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        setupPhotoViewer(recyclerView, imageUrls);

        if (model.getApprovalBy().equals(MainService.getInstance().getLoginModel().getName())) {
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.VISIBLE);
            contentLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SelectPersonActivity.RequestCode && resultCode == RESULT_OK) {
            final String personId = data.getStringExtra(SelectPersonActivity.PersonIdKey);
            String personName = data.getStringExtra(SelectPersonActivity.PersonNamekey);
            String message = "是否由承办人（" + personName + "）继续处理";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("需整改");
            builder.setMessage(message);
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    doApproval(personId);
                }
            });
            builder.setNegativeButton("否", null);
            builder.create().show();
        }
    }

    private void doApproval(String personId) {
        EditText editText = (EditText) findViewById(R.id.edit_text_content);
        String content = editText.getText().toString();

        SupervisionPatrolService.getInstance().sendSupervisionPatrolApproval(modelId, content, "1", personId, new Callbacks() {
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
}
