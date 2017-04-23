package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.services.MainService;

public class SupervisionPatrolApprovalProcessActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_patrol_approval_process);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("待审批");

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        SupervisionPatrolService.getInstance().sendQueryDetail(id, new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {

            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
