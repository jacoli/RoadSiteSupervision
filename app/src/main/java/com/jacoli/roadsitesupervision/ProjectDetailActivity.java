package com.jacoli.roadsitesupervision;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.views.MyToast;

import cn.lankton.flowlayout.FlowLayout;

public class ProjectDetailActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("施工旁站");

final ProjectDetailActivity projectDetailActivity = this;

        GridView gridView = (GridView) findViewById(R.id.gridview);

        BaseAdapter baseAdapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return 30;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = projectDetailActivity.getLayoutInflater().inflate(R.layout.text_item_layout, null);
                TextView textView = (TextView)v.findViewById(R.id.textView);
                String text = "" + position;
                textView.setText(text);
                return v;
            }
        };

        gridView.setAdapter(baseAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(projectDetailActivity, ComponetDetailActivity.class);
                startActivity(intent);
            }
        });
    }

}
