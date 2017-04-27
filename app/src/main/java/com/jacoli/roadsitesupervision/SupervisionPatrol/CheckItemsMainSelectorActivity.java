package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.R;
import java.util.ArrayList;
import java.util.List;

public class CheckItemsMainSelectorActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_patrol_check_items_main_seletor);

        createTitleBar();
        titleBar.setLeftText("取消");
        titleBar.setTitle("选取巡查细目");

        List<CheckItemsModel.Item> subItems = new ArrayList<>();
        CheckItemsModel.Item item = (CheckItemsModel.Item) getIntent().getExtras().getSerializable("object");
        if (item.getItems() != null) {
            for (CheckItemsModel.Item subItem : item.getItems()) {
                subItems.add(subItem);
            }
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        setupListView(listView, subItems);
    }

    private void setupListView(ListView listView, final List<CheckItemsModel.Item> subItems) {
        BaseAdapter adapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return subItems.size();
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
                final CheckItemsModel.Item item = subItems.get(position);
                View v = getLayoutInflater().inflate(R.layout.list_item_supervision_patrol_check_items_level2, null);
                TextView textView = (TextView) v.findViewById(R.id.textView);
                textView.setText(item.getName());
                return v;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckItemsModel.Item item = subItems.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object", item);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
