package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.R;

// 创建监理巡查
public class SupervisionPatrolCreatingActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_patrol_creating);

        createTitleBar();
        titleBar.setLeftText("取消");
        titleBar.setTitle("创建监理巡查");

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.component_id);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupervisionPatrolCreatingActivity.this, SupervisionPatrolProjectDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
