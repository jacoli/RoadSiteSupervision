package com.jacoli.roadsitesupervision.DataMonitor;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.R;
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

    @BindView(R.id.title1)
    TextView textView1;

    @BindView(R.id.title2)
    TextView textView2;

    @BindView(R.id.seperator1)
    View seperator1;

    @BindView(R.id.seperator2)
    View seperator2;

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
    private List<GetPointAlarmHistroyModel.Item> items;

    private List<GetPointAlarmHistroyModel> models2;
    private List<GetPointAlarmHistroyModel.Item> items2;

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_monitor_messages, container, false);

        ButterKnife.bind(this, view);

        models = new ArrayList<>();
        items = new ArrayList<>();
        models2 = new ArrayList<>();
        items2 = new ArrayList<>();

        final int selectedColor = ContextCompat.getColor(getActivity(), R.color.material_blue_400);
        final int unselectedColor = ContextCompat.getColor(getActivity(), R.color.material_blueGrey_200);

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLayout.setVisibility(View.VISIBLE);
                textView1.setTextColor(selectedColor);
                seperator1.setBackgroundColor(selectedColor);
                refreshLayout2.setVisibility(View.INVISIBLE);
                textView2.setTextColor(unselectedColor);
                seperator2.setBackgroundColor(unselectedColor);
            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLayout.setVisibility(View.INVISIBLE);
                textView1.setTextColor(unselectedColor);
                seperator1.setBackgroundColor(unselectedColor);
                refreshLayout2.setVisibility(View.VISIBLE);
                textView2.setTextColor(selectedColor);
                seperator2.setBackgroundColor(selectedColor);
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
                loadMore2();
            }
        });

        loadData();
        loadData2();

        return view;
    }

    public void loadData() {
        DataMonitorService.getInstance().GetPointAlarmHistroy(false, 1, new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                GetPointAlarmHistroyModel model = (GetPointAlarmHistroyModel)responseModel;
                models.clear();
                items.clear();
                models.add(model);
                items.addAll(model.getItems());
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
        DataMonitorService.getInstance().GetPointAlarmHistroy(false, models.size() + 1, new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                GetPointAlarmHistroyModel model = (GetPointAlarmHistroyModel)responseModel;
                models.add(model);
                items.addAll(model.getItems());
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
        DataMonitorService.getInstance().GetPointAlarmHistroy(true, 1, new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                GetPointAlarmHistroyModel model = (GetPointAlarmHistroyModel)responseModel;
                models2.clear();
                items2.clear();
                models2.add(model);
                items2.addAll(model.getItems());
                adapter2.notifyDataSetChanged();
                refreshLayout2.finishRefresh();
                refreshLayout2.finishRefreshLoadMore();
                refreshLayout2.setLoadMore(model.getPageCounts() > 1);
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

    public void loadMore2() {
        DataMonitorService.getInstance().GetPointAlarmHistroy(true, models2.size() + 1, new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                GetPointAlarmHistroyModel model = (GetPointAlarmHistroyModel)responseModel;
                models2.add(model);
                items2.addAll(model.getItems());
                adapter2.notifyDataSetChanged();
                refreshLayout2.finishRefresh();
                refreshLayout2.finishRefreshLoadMore();
                refreshLayout2.setLoadMore(model.getPageCounts() > models2.size());
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
                return items.size() + 1;
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
                    return getActivity().getLayoutInflater().inflate(R.layout.list_item_message_list_header, null);
                } else {
                    GetPointAlarmHistroyModel.Item item = items.get(position - 1);
                    View v = getActivity().getLayoutInflater().inflate(R.layout.list_item_message_list_cell, null);
                    ((TextView)v.findViewById(R.id.text1)).setText(String.valueOf(position));
                    ((TextView)v.findViewById(R.id.text2)).setText(item.getUnitProjectName());
                    ((TextView)v.findViewById(R.id.text3)).setText(item.getPointCode());
                    ((TextView)v.findViewById(R.id.text4)).setText(item.getMonitorTypeName());
                    ((TextView)v.findViewById(R.id.text5)).setText(item.getProcessStatusStr());
                    ((TextView)v.findViewById(R.id.text6)).setText(item.getAddTime());
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
                return items2.size() + 1;
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
                    return getActivity().getLayoutInflater().inflate(R.layout.list_item_message_list_header, null);
                } else {
                    GetPointAlarmHistroyModel.Item item = items2.get(position - 1);
                    View v = getActivity().getLayoutInflater().inflate(R.layout.list_item_message_list_cell, null);
                    ((TextView)v.findViewById(R.id.text1)).setText(String.valueOf(position));
                    ((TextView)v.findViewById(R.id.text2)).setText(item.getUnitProjectName());
                    ((TextView)v.findViewById(R.id.text3)).setText(item.getPointCode());
                    ((TextView)v.findViewById(R.id.text4)).setText(item.getMonitorTypeName());
                    ((TextView)v.findViewById(R.id.text5)).setText(item.getProcessStatusStr());
                    ((TextView)v.findViewById(R.id.text6)).setText(item.getAddTime());
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


