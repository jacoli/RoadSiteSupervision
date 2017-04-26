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

    static int RequestCode = 10099;

    private BaseAdapter adapter;
    private List<CheckItemsModel.Item> flatItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_patrol_check_items_main_seletor);

        flatItems = new ArrayList<>();

        createTitleBar();
        titleBar.setLeftText("取消");
        titleBar.setTitle("选取巡查明细");

        CheckItemsModel.Item item = (CheckItemsModel.Item) getIntent().getExtras().getSerializable("object");
        if (item.getItems() != null) {
            for (CheckItemsModel.Item subitem : item.getItems()) {
                flatItems.add(subitem);
            }
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        setupListView(listView);
    }

    private void setupListView(ListView listView) {
        adapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return flatItems.size();
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
                final CheckItemsModel.Item item = flatItems.get(position);
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
                CheckItemsModel.Item item = flatItems.get(position);
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
