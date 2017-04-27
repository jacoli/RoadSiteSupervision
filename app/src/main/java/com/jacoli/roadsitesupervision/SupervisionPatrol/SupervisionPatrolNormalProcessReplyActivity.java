package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.services.Utils;

// 监理巡查流程回复
public class SupervisionPatrolNormalProcessReplyActivity extends CommonActivity {

    public static int RequestCode = 2001;

    private String modelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_patrol_normal_process_reply);

        createTitleBar();
        titleBar.setLeftText("取消");
        titleBar.setTitle("回复");

        Intent intent = getIntent();
        modelId = intent.getStringExtra("id");

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        setupDefaultImagePicker();
    }

    public void submit() {
        EditText editText = (EditText) findViewById(R.id.editText);
        String content = editText.getText().toString();
        if (Utils.isStringEmpty(content)) {
            Toast.makeText(this, "回复内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        SupervisionPatrolService.getInstance().sendSupervisionPatrolReply(modelId,
                content,
                getCommonSelectedPhotoUrls(),
                new Callbacks() {
                    @Override
                    public void onSuccess(ResponseBase responseModel) {
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailed(String error) {
                        Toast.makeText(SupervisionPatrolNormalProcessReplyActivity.this, error, Toast.LENGTH_SHORT).show();
                        Button submitBtn = (Button) findViewById(R.id.submit_btn);
                        submitBtn.setEnabled(true);
                    }
                });

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setEnabled(false);
    }
}
