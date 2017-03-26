package com.jacoli.roadsitesupervision;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.services.ComponentDetailModel;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.MyAssginedMattersModel;
import com.jacoli.roadsitesupervision.views.MyToast;

import java.util.List;

public class TodoListFragment extends CommonFragment {

    private MyAssginedMattersModel model;
    private BaseAdapter adapter;

    public TodoListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View selfView = inflater.inflate(R.layout.fragment_todolist_list, container, false);
        ListView listView = (ListView)selfView.findViewById(R.id.listView);

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
                View v = getActivity().getLayoutInflater().inflate(R.layout.list_item_base_action_button, null);
                TextView textView = (TextView)v.findViewById(R.id.textView);
                //textView.setText(models[position]);
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

        MainService.getInstance().sendQueryAssignedMatters(handler);

        return selfView;
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_QUERY_ASSIGNED_MATTERS_SUCCESS:
                model = (MyAssginedMattersModel) obj;
                adapter.notifyDataSetChanged();
                break;
            case MainService.MSG_QUERY_ASSIGNED_MATTERS_FAILED:
                Toast.makeText(getActivity(), "获取交办事项列表失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}
