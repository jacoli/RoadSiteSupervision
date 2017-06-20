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

public class MonitorSensorTypesActivity extends CommonActivity {
    private MonitorSensorTypesModel model;
    private BaseAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_sensor_types);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("传感器数据");

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
        DataMonitorService.getInstance().GetMonitorTypeList(new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                model = (MonitorSensorTypesModel) responseModel;
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
                int count = model == null ? 0 : model.getItems().size();
                return count;
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
                MonitorSensorTypesModel.SensorType type = model.getItems().get(position);
                View v = getLayoutInflater().inflate(R.layout.list_item_base_action_button, null);
                ((TextView)v.findViewById(R.id.textView)).setText(type.getName() + "传感器");
                return v;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MonitorSensorTypesModel.SensorType type = model.getItems().get(position);
                showDetail(type);
            }
        });
    }

    private void showDetail(MonitorSensorTypesModel.SensorType sensorType) {
    }
}
