package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.AssignedMatterDetailActivity;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.MyAssignedMattersModel;
import com.jacoli.roadsitesupervision.services.Utils;

// 监理巡查
public class SupervisionPatrolListActivity extends CommonActivity {

    private MyAssignedMattersModel model;
    private BaseAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_patrol_list);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("监理巡查");

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        ListView listView = (ListView) findViewById(R.id.listView);
        setupListView(listView);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MainService.getInstance().sendQueryAssignedMatters(handler);
            }
        });
    }

    public void submit() {
        Intent intent = new Intent(this, SupervisionPatrolCreatingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        MainService.getInstance().sendQueryAssignedMatters(handler);
    }

    private void setupListView(ListView listView) {
        adapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return model == null ? 0 : model.getItems().size();
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
                // TODO

                MyAssignedMattersModel.Item item = model.getItems().get(position);

                View v = getLayoutInflater().inflate(R.layout.list_item_supervision_patral_list, null);
                TextView textView = (TextView)v.findViewById(R.id.textView);
                String text = "";
                if (Utils.isStringEmpty(item.getAMType())) {
                    text += item.getSubject();
                } else {
                    text += item.getAMType() + "：" + item.getSubject();
                }

                textView.setText(text);

                TextView textView2 = (TextView)v.findViewById(R.id.textView2);
                String text2 = "交办人：" + item.getSenderName() + " " + item.getAddTime();
                textView2.setText(text2);

                TextView textView3 = (TextView)v.findViewById(R.id.textView3);
                String text3 = "截止时间：" + item.getDeadLine();
                textView3.setText(text3);

                TextView textView4 = (TextView)v.findViewById(R.id.textView4);
                String text4 = "最后回复：";
                if (Utils.isStringEmpty(item.getReplyName())) {
                    text4 += "无";
                } else {
                    text4 += item.getReplyName() + " " + item.getReplyTime();
                }
                textView4.setText(text4);

                return v;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity() ,AssignedMatterDetailActivity.class);
//                intent.putExtra("id", model.getItems().get(position).getID());
//                startActivity(intent);
            }
        });
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_QUERY_ASSIGNED_MATTERS_SUCCESS:
                model = (MyAssignedMattersModel) obj;
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                break;
            case MainService.MSG_QUERY_ASSIGNED_MATTERS_FAILED:
                Toast.makeText(this, "获取交办事项列表失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
