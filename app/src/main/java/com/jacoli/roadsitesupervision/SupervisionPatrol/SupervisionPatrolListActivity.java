package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.R;

// 监理巡查列表页
public class SupervisionPatrolListActivity extends CommonActivity {

    private SupervisionPatrolListModel model;
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
                loadData();
            }
        });
    }

    public void loadData() {
        SupervisionPatrolService.getInstance().sendQuerySupervisionPatrolList(new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                model = (SupervisionPatrolListModel) responseModel;
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(SupervisionPatrolListActivity.this, error, Toast.LENGTH_SHORT).show();
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

        loadData();
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
                SupervisionPatrolListModel.Item item = model.getItems().get(position);

                View v = getLayoutInflater().inflate(R.layout.list_item_supervision_patral_list, null);
                TextView textView = (TextView)v.findViewById(R.id.textView);
                String text = "项目：" + item.getProjectName();
                textView.setText(text);

                TextView textView2 = (TextView)v.findViewById(R.id.textView2);
                String text2 = "工程地点部位：" + item.getProjectPart();
                textView2.setText(text2);

                TextView textView3 = (TextView)v.findViewById(R.id.textView3);
                String text3 = "上报人：" + item.getAddBy() + item.getAddTime();
                textView3.setText(text3);

                TextView textView4 = (TextView)v.findViewById(R.id.textView4);
                String text4 = "最后回复：" + item.getLastUpdateBy() + item.getLastUpdateTime();
                textView4.setText(text4);

                TextView statusTextView = (TextView) v.findViewById(R.id.text_view_status);
                statusTextView.setText(item.getStatus());

                return v;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SupervisionPatrolListActivity.this, SupervisionPatrolNormalProcessActivity.class);
                intent.putExtra("id", model.getItems().get(position).getID());
                startActivity(intent);
            }
        });
    }
}
