package com.jacoli.roadsitesupervision;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.MyAssignedMattersModel;
import com.jacoli.roadsitesupervision.services.StaffListModel;
import com.jacoli.roadsitesupervision.services.Utils;

import java.util.ArrayList;
import java.util.List;

public class StaffsActivity extends CommonActivity {

    private StaffListModel model;
    private BaseAdapter adapter;
    private List<String> selectedStaffIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staffs);

        createTitleBar();
        titleBar.setLeftText("取消");
        titleBar.setTitle("选择承办人");

        selectedStaffIds = getIntent().getStringArrayListExtra("staffs");
        if (selectedStaffIds == null) {
            selectedStaffIds = new ArrayList<>();
        }

        setupListView((ListView) findViewById(R.id.listView));

        MainService.getInstance().sendQueryStaffs(new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                model = (StaffListModel) responseModel;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(StaffsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setText("确定");
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
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

    boolean isStaffSelected(String staffId) {
        if (Utils.isStringEmpty(staffId)) {
            return false;
        }

        boolean isSelected = false;
        if (selectedStaffIds != null) {
            for (String selectedStaffId : selectedStaffIds) {
                if (selectedStaffId.equals(staffId)) {
                    isSelected = true;
                    break;
                }
            }
        }
        return isSelected;
    }

    void submit() {
        if (model == null) {
            return;
        }


//        String staffIds = "";
//        String staffNames = "";
//
//        for (int idx = 0; idx < selectedStaffIds.size(); ++idx) {
//            String staffId = selectedStaffIds.get(idx);
//            staffIds += staffId;
//            if (idx != selectedStaffIds.size() - 1) {
//                staffIds += ",";
//            }
//
//
//
//        }
//
//        for (String staffId : selectedStaffIds) {
//
//
//        }



        Intent intent = new Intent();
        //intent.putExtra("staffID", );
        setResult(RESULT_OK, intent);
        finish();
    }
}
