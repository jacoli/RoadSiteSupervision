package com.jacoli.roadsitesupervision.DataMonitor;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
//import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.R;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {

    @BindView(R.id.tab1)
    LinearLayout linearLayout1;

    @BindView(R.id.tab2)
    LinearLayout linearLayout2;

    @BindView(R.id.refresh)
    MaterialRefreshLayout refreshLayout;

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.refresh2)
    MaterialRefreshLayout refreshLayout2;

    @BindView(R.id.listView2)
    ListView listView2;

    private BaseAdapter adapter;
    private BaseAdapter adapter2;

    private List<GetPointAlarmHistroyModel> models;

    private GetPointAlarmHistroyModel model;
    private GetPointAlarmHistroyModel model2;

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_monitor_messages, container, false);

        ButterKnife.bind(this, view);

        models = new ArrayList<>();

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLayout.setVisibility(View.VISIBLE);
                refreshLayout2.setVisibility(View.INVISIBLE);
            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLayout2.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.INVISIBLE);
            }
        });

        setupListView(listView);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                loadData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                loadMore();
            }
        });

        setupListView2(listView2);
        refreshLayout2.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                loadData2();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                loadData2();
            }
        });

        return view;
    }

    public void loadData() {
        DataMonitorService.getInstance().GetPointAlarmHistroy(false, 1, new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                GetPointAlarmHistroyModel model = (GetPointAlarmHistroyModel)responseModel;
                models.clear();
                models.add(model);
                adapter.notifyDataSetChanged();
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();
                refreshLayout.setLoadMore(model.getPageCounts() > 1);
                //dismissHUD();
            }

            @Override
            public void onFailed(String error) {
                //showToast(error);
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();
                //dismissHUD();
            }
        });
        //showHUD();
    }

    public void loadMore() {
        DataMonitorService.getInstance().GetPointAlarmHistroy(false, 1, new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                GetPointAlarmHistroyModel model = (GetPointAlarmHistroyModel)responseModel;
                models.add(model);
                adapter.notifyDataSetChanged();
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();
                refreshLayout.setLoadMore(model.getPageCounts() > models.size());
                //dismissHUD();
            }

            @Override
            public void onFailed(String error) {
                //showToast(error);
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();
                //dismissHUD();
            }
        });
        //showHUD();
    }

    public void loadData2() {
        DataMonitorService.getInstance().GetPointAlarmHistroy(false, 1, new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                model = (GetPointAlarmHistroyModel) responseModel;
                adapter.notifyDataSetChanged();
                refreshLayout2.finishRefresh();
                refreshLayout2.finishRefreshLoadMore();
                //dismissHUD();
            }

            @Override
            public void onFailed(String error) {
                //showToast(error);
                refreshLayout2.finishRefresh();
                refreshLayout2.finishRefreshLoadMore();
                //dismissHUD();
            }
        });
        //showHUD();
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
                    return getActivity().getLayoutInflater().inflate(R.layout.list_item_point_list_header, null);
                } else {
                    GetPointAlarmHistroyModel.Item item = model.getItems().get(position - 1);
                    View v = getActivity().getLayoutInflater().inflate(R.layout.list_item_point_list_cell, null);
                    ((TextView)v.findViewById(R.id.text1)).setText(String.valueOf(position));
                    ((TextView)v.findViewById(R.id.text2)).setText(item.getProcessByName());
                    ((TextView)v.findViewById(R.id.text3)).setText(item.getPointCode());
                    ((TextView)v.findViewById(R.id.text4)).setText(item.getAddTime());
                    ((TextView)v.findViewById(R.id.text5)).setText(item.getMonitorTypeName());

                    return v;
                }
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                PointListModel.Point point = model.getItems().get(position - 1);
//                Intent intent = new Intent(MonitorPointListActivity.this, LineChartActivity1.class);
//                intent.putExtra("title", point.getPointName());
//                intent.putExtra("id", point.getID());
//                intent.putExtra("special", point.getMonitorTypeName().equals("变形"));
//                startActivity(intent);
            }
        });
    }

    private void setupListView2(ListView listView) {
        adapter2 = new BaseAdapter() {

            @Override
            public int getCount() {
                return model2 == null ? 1 : model2.getItems().size() + 1;
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
                    return getActivity().getLayoutInflater().inflate(R.layout.list_item_point_list_header, null);
                } else {
                    GetPointAlarmHistroyModel.Item item = model2.getItems().get(position - 1);
                    View v = getActivity().getLayoutInflater().inflate(R.layout.list_item_point_list_cell, null);
                    ((TextView)v.findViewById(R.id.text1)).setText(String.valueOf(position));
                    ((TextView)v.findViewById(R.id.text2)).setText(item.getProcessByName());
                    ((TextView)v.findViewById(R.id.text3)).setText(item.getPointCode());
                    ((TextView)v.findViewById(R.id.text4)).setText(item.getAddTime());
                    ((TextView)v.findViewById(R.id.text5)).setText(item.getMonitorTypeName());

                    return v;
                }
            }
        };

        listView.setAdapter(adapter2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                PointListModel.Point point = model.getItems().get(position - 1);
//                Intent intent = new Intent(MonitorPointListActivity.this, LineChartActivity1.class);
//                intent.putExtra("title", point.getPointName());
//                intent.putExtra("id", point.getID());
//                intent.putExtra("special", point.getMonitorTypeName().equals("变形"));
//                startActivity(intent);
            }
        });
    }
}


