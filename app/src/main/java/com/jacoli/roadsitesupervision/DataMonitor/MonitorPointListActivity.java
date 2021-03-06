package com.jacoli.roadsitesupervision.DataMonitor;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.R;

public class MonitorPointListActivity extends CommonActivity {

    private PointListModel model;
    private BaseAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_point_list);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle(getIntent().getStringExtra("title"));

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
        DataMonitorService.getInstance().GetPointListByUnitProjectID(getIntent().getStringExtra("id"), new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                model = (PointListModel) responseModel;
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailed(String error) {
                showToast(error);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
                return model == null ? 1 : model.getItems().size() + 1;
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
                if (position == 0) {
                    return getLayoutInflater().inflate(R.layout.list_item_point_list_header, null);
                } else {
                    PointListModel.Point point = model.getItems().get(position - 1);
                    View v = getLayoutInflater().inflate(R.layout.list_item_point_list_cell, null);
                    ((TextView)v.findViewById(R.id.text1)).setText(String.valueOf(position));
                    ((TextView)v.findViewById(R.id.text2)).setText(point.getPointName());
                    ((TextView)v.findViewById(R.id.text3)).setText(point.getPointCode());
                    ((TextView)v.findViewById(R.id.text4)).setText(point.getUpdateTime());
                    ((TextView)v.findViewById(R.id.text5)).setText(point.getValStr());

                    if (getIntent().getBooleanExtra("isAlarm", false)) {
                        int color = ContextCompat.getColor(MonitorPointListActivity.this, R.color.material_red_300);
                        ((TextView)v.findViewById(R.id.text1)).setTextColor(color);
                        ((TextView)v.findViewById(R.id.text2)).setTextColor(color);
                        ((TextView)v.findViewById(R.id.text3)).setTextColor(color);
                        ((TextView)v.findViewById(R.id.text4)).setTextColor(color);
                        ((TextView)v.findViewById(R.id.text5)).setTextColor(color);
                    }

                    return v;
                }
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO
            }
        });
    }
}
