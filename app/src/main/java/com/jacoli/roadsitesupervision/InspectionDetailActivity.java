package com.jacoli.roadsitesupervision;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.views.MyToast;

import java.util.HashMap;
import java.util.Map;

public class InspectionDetailActivity extends CommonActivity {

    private String id;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_detail);

        type = getIntent().getIntExtra("type", MainService.project_detail_type_pz);
        id = getIntent().getStringExtra("id");

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle(getIntent().getStringExtra("title"));
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        EditText editText = (EditText) findViewById(R.id.editText);
        editText.clearFocus();

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("旁站数据可能未保存，是否仍要返回上一页");

        builder.setPositiveButton("返回上一页", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                finish();
            }
        });

        builder.setNegativeButton("留在当前页面", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });

        builder.create().show();
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_SUBMIT_INSPECTION_DETAIL_SUCCESS:
                MyToast.showMessage(getBaseContext(), "提交旁站详情成功");
                onSubmitSuccess();
                break;
            case MainService.MSG_SUBMIT_INSPECTION_DETAIL_FAILED:
                Toast.makeText(getBaseContext(), "提交旁站详情失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void onSubmitSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("提交旁站数据成功，是否留在当前页面");

        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });

        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                finish();
            }
        });

        builder.create().show();
    }

    public void submit() {
        if (id == null || id.length() < 0) {
            return;
        }

        try {
            Map<String, String> params = new HashMap<>();
            // TODO

            EditText editText = (EditText) findViewById(R.id.editText);
            params.put("Situation", editText.getText().toString());

            if (type == MainService.project_detail_type_quality_inspection) {
                params.put("PatrolType", "0");
            }
            else if (type == MainService.project_detail_type_safety_inspection) {
                params.put("PatrolType", "1");
            }
            else if (type == MainService.project_detail_type_environmental_inspection) {
                params.put("PatrolType", "2");
            }

            MainService.getInstance().sendSubmitInspectionDetail(id, params, handler);
        }
        catch (Exception ex) {
            Log.e("InspectionDetail", ex.toString());
        }
    }
}
