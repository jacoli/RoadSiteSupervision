package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.StaffListModel;

public class SelectPersonActivity extends CommonActivity {
    static int RequestCode = 1000;
    static String PersonIdKey = "PersonIdKey";
    static String PersonNamekey = "PersonNamekey";
    private StaffListModel model;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_patrol_select_person);

        createTitleBar();
        titleBar.setLeftText("取消");
        titleBar.setTitle("选择承办人");

        setupListView((ListView) findViewById(R.id.listView));

        MainService.getInstance().sendQueryStaffs(new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                model = (StaffListModel) responseModel;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
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
                View v = getLayoutInflater().inflate(R.layout.list_item_staffs_unselected, null);
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
                Intent intent = new Intent();
                intent.putExtra(SelectPersonActivity.PersonIdKey, staff.getID());
                intent.putExtra(SelectPersonActivity.PersonNamekey, staff.getName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
