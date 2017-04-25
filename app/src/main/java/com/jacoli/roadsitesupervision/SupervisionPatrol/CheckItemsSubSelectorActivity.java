package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckItemsSubSelectorActivity extends CommonActivity {

    static int RequestCode = 10001;

    private BaseAdapter adapter;
    private List<CheckItemsModel.Item> flatItems;
    private HashMap<String, CheckItemsModel.Item> selectedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_patrol_check_items_sub_selector);

        flatItems = new ArrayList<>();
        selectedItems = new HashMap<>();

        createTitleBar();
        titleBar.setLeftText("取消");
        titleBar.setTitle("选取巡查明细（可多选）");

        CheckItemsModel.Item item = (CheckItemsModel.Item) getIntent().getExtras().getSerializable("object");
        flatItems = item.getFlatItems(false);

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

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
                View v = null;
                if (item.getLevel() == 1) {
                    v = getLayoutInflater().inflate(R.layout.list_item_supervision_patrol_check_items_level1, null);

                    CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                            if (isChecked) {
                                selectedItems.put(item.getID(), item);
                            }
                            else {
                                selectedItems.remove(item.getID());
                            }
                        }
                    });
                } else if (item.getLevel() == 2) {
                    v = getLayoutInflater().inflate(R.layout.list_item_supervision_patrol_check_items_level2, null);
                } else {
                    v = getLayoutInflater().inflate(R.layout.list_item_supervision_patrol_check_items_level2, null);
                }

                TextView textView = (TextView) v.findViewById(R.id.textView);
                textView.setText(item.getName());
                return v;
            }
        };

        listView.setAdapter(adapter);
    }

    public void submit() {
        if (selectedItems.isEmpty()) {
            Toast.makeText(CheckItemsSubSelectorActivity.this, "请选择巡查明细", Toast.LENGTH_SHORT).show();
            return;
        }

        String itemIds = "";
        String text = "";

        for (CheckItemsModel.Item item : selectedItems.values()) {
            itemIds += item.getID() + ",";
            text += item.getName() + "(否)\n";
        }

        if (itemIds.endsWith(",")) {
            itemIds = itemIds.substring(0, itemIds.length() - 1);
        }

        Intent intent = new Intent();
        intent.putExtra("checkItemIds", itemIds);
        intent.putExtra("content", text);
        setResult(RESULT_OK, intent);
        finish();
    }
}
