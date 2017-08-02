package com.jacoli.roadsitesupervision.DataMonitor;

import android.content.Intent;
import android.os.Bundle;
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

public class MonitorSensorListActivity extends CommonActivity {

    private MonitorSensorListModel model;
    private BaseAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_sensor_list);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("传感器列表");

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
        DataMonitorService.getInstance().GetSensorListByMonitorTypeID(getIntent().getStringExtra("id"), new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                model = (MonitorSensorListModel) responseModel;
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                findViewById(R.id.loadView).setVisibility(View.GONE);
            }

            @Override
            public void onFailed(String error) {
                showToast(error);
                swipeRefreshLayout.setRefreshing(false);
                findViewById(R.id.loadView).setVisibility(View.GONE);
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
                    return getLayoutInflater().inflate(R.layout.list_item_sensor_list_header, null);
                } else {
                    MonitorSensorListModel.Sensor sensor = model.getItems().get(position - 1);
                    View v = getLayoutInflater().inflate(R.layout.list_item_sensor_list_cell, null);
                    ((TextView)v.findViewById(R.id.text1)).setText(String.valueOf(position));
                    ((TextView)v.findViewById(R.id.text2)).setText(sensor.getSensorCode());
                    ((TextView)v.findViewById(R.id.text3)).setText(sensor.getPowerStr());
                    ((TextView)v.findViewById(R.id.text4)).setText(sensor.getValStr());
                    ((TextView)v.findViewById(R.id.text5)).setText(sensor.getAddTime());
                    return v;
                }
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                } else {
                    MonitorSensorListModel.Sensor sensor = model.getItems().get(position - 1);
                    Intent intent = new Intent(MonitorSensorListActivity.this, MonitorAddSensorDataActivity.class);
                    intent.putExtra("id", getIntent().getStringExtra("id"));
                    intent.putExtra("type_name", getIntent().getStringExtra("type_name"));
                    intent.putExtra("code", sensor.getSensorCode());
                    startActivity(intent);
                }
            }
        });
    }
}
