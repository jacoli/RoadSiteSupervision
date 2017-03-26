package com.jacoli.roadsitesupervision;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.services.AssignedMatterDetailModel;
import com.jacoli.roadsitesupervision.services.ComponentDetailModel;
import com.jacoli.roadsitesupervision.services.ImageUrlModel;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.views.MyToast;

import java.util.List;

public class AssignedMatterDetailActivity extends CommonActivity {

    private AssignedMatterDetailModel model;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_matter_detail);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("交办事项详情");

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if (!MainService.getInstance().sendQueryAssignedMatterDetail(id, handler)) {
            Toast.makeText(getBaseContext(), "获取交办事项详情失败", Toast.LENGTH_SHORT).show();
        }

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        ListView listView = (ListView) findViewById(R.id.listView);

        adapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return model == null ? 0 : (1 + model.getReply().size());
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
                View v = getLayoutInflater().inflate(R.layout.list_item_assigned_mater_detail, null);
                TextView textView = (TextView)v.findViewById(R.id.textView);

                if (position == 0) {
                    String text = "主题：" + model.getSubject() + "\n"
                            + "来自于：" + model.getSenderName() + "\n"
                            + "时间：" + model.getAddTime() + "\n"
                            + "内容：" + model.getAssignContent();
                    textView.setText(text);
                }
                else {
                    AssignedMatterDetailModel.Reply reply = model.getReply().get(position - 1);
                    String text = "来自于：" + reply.getReplyName() + "\n"
                            + "时间：" + reply.getAddTime() + "\n"
                            + "内容：" + reply.getAssignContent();
                    textView.setText(text);
                }

                return v;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_QUERY_ASSIGNED_MATTER_DETAIL_SUCCESS:
                model = (AssignedMatterDetailModel) obj;
                adapter.notifyDataSetChanged();
                break;
            case MainService.MSG_QUERY_ASSIGNED_MATTER_DETAIL_FAILED:
                Toast.makeText(getBaseContext(), "获取交办事项详情失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
