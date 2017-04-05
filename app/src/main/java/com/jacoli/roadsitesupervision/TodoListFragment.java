package com.jacoli.roadsitesupervision;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.MyAssignedMattersModel;

public class TodoListFragment extends CommonFragment {

    private MyAssignedMattersModel model;
    private BaseAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public TodoListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View selfView = inflater.inflate(R.layout.fragment_todolist_list, container, false);
        ListView listView = (ListView)selfView.findViewById(R.id.listView);
        setupListView(listView);

        swipeRefreshLayout = (SwipeRefreshLayout) selfView.findViewById(R.id.id_swipe_ly);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MainService.getInstance().sendQueryAssignedMatters(handler);
            }
        });

        return selfView;
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
                int layoutId = model.getItems().get(position).getRead() ? R.layout.list_item_assigned_mater_list : R.layout.list_item_assigned_mater_unread_list;

                View v = getActivity().getLayoutInflater().inflate(layoutId, null);
                TextView textView = (TextView)v.findViewById(R.id.textView);
                textView.setText(model.getItems().get(position).getSubject());

                TextView textView2 = (TextView)v.findViewById(R.id.textView2);
                textView2.setText(model.getItems().get(position).getAddTime());
                return v;
            }
        };


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity() ,AssignedMatterDetailActivity.class);
                intent.putExtra("id", model.getItems().get(position).getID());
                startActivity(intent);
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
                Toast.makeText(getActivity(), "获取交办事项列表失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}
