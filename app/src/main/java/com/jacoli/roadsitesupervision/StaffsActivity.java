package com.jacoli.roadsitesupervision;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.StaffListModel;
import com.jacoli.roadsitesupervision.services.Utils;
import java.util.ArrayList;

public class StaffsActivity extends CommonActivity {

    static int RequestCode = 1000;
    static String KeyForStaffIds = "StaffIds";
    static String KeyForStaffNames = "StaffNames";

    private StaffListModel model;
    private BaseAdapter adapter;
    private ArrayList<String> selectedStaffIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staffs);

        createTitleBar();
        titleBar.setLeftText("取消");
        titleBar.setTitle("选择承办人");

        selectedStaffIds = getIntent().getStringArrayListExtra(StaffsActivity.KeyForStaffIds);
        if (selectedStaffIds == null) {
            selectedStaffIds = new ArrayList<>();
        }

        setupListView((ListView) findViewById(R.id.listView));

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setText("确定");
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        MainService.getInstance().sendQueryStaffs(new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                model = (StaffListModel) responseModel;
                adapter.notifyDataSetChanged();

                ArrayList<String> staffIds = new ArrayList<>();
                for (String staffId: selectedStaffIds) {
                    if (model.getStaffName(staffId) != null) {
                        staffIds.add(staffId);
                    }
                }
                selectedStaffIds = staffIds;
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(StaffsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListView(ListView listView) {
        if (listView == null) {
            return;
        }

        adapter = new BaseAdapter() {

            @Override
            public int getCount() {
                if (model != null && model.getItems() != null) {
                    return model.getItems().size();
                } else {
                    return 0;
                }
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
                StaffListModel.Staff staff = model.getItems().get(position);
                int id = R.layout.list_item_staffs_unselected;
                if (staff != null && !Utils.isStringEmpty(staff.getID()) && selectedStaffIds.contains(staff.getID())) {
                    id = R.layout.list_item_staffs_selected;
                }
                View v = getLayoutInflater().inflate(id, null);
                TextView textView = (TextView)v.findViewById(R.id.textView);
                textView.setText(model.getItems().get(position).getName());
                return v;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StaffListModel.Staff staff = model.getItems().get(position);
                if (staff != null && !Utils.isStringEmpty(staff.getID())) {
                    if (selectedStaffIds.contains(staff.getID())) {
                        selectedStaffIds.remove(staff.getID());
                    } else {
                        selectedStaffIds.add(staff.getID());
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    void submit() {
        if (model == null) {
            return;
        }

        String staffNames = "";

        for (int idx = 0; idx < selectedStaffIds.size(); ++idx) {
            String name = model.getStaffName(selectedStaffIds.get(idx));
            if (name != null) {
                staffNames += name;
                if (idx != selectedStaffIds.size() - 1) {
                    staffNames += ",";
                }
            }
        }

        Intent intent = new Intent();
        intent.putStringArrayListExtra(StaffsActivity.KeyForStaffIds, selectedStaffIds);
        intent.putExtra(StaffsActivity.KeyForStaffNames, staffNames);
        setResult(RESULT_OK, intent);
        finish();
    }
}
