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
import com.jacoli.roadsitesupervision.services.Utils;

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
                MyAssignedMattersModel.Item item = model.getItems().get(position);

                int layoutId = item.getRead() ? R.layout.list_item_assigned_mater_list : R.layout.list_item_assigned_mater_unread_list;

                boolean isExpireDeadLine = !Utils.isStringEmpty(item.getDeadLine()) && Utils.isCurrentTimeExpired(item.getDeadLine());
                if (isExpireDeadLine) {
                    layoutId = R.layout.list_item_assigned_mater_list_deadline;
                }

                View v = getActivity().getLayoutInflater().inflate(layoutId, null);
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
                String text3 = "最后回复：";
                if (Utils.isStringEmpty(item.getReplyName())) {
                    text3 += "无";
                } else {
                    text3 += item.getReplyName() + " " + item.getReplyTime();
                }

                if (isExpireDeadLine) {
                    text3 = "截止时间：" + item.getDeadLine();
                }

                textView3.setText(text3);
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
