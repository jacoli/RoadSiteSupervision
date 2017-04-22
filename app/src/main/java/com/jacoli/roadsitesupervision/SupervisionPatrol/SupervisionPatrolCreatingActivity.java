package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.AssignedMatterCreateActivity;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.PhotoAdapter;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.RecyclerItemClickListener;
import com.jacoli.roadsitesupervision.StaffsActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

// 创建监理巡查
public class SupervisionPatrolCreatingActivity extends CommonActivity {

    private CheckItemsModel checkItemsModel;
    private ApproverListModel approverListModel;
    private String selectedTypeId;
    private String selectedCheckItemIds;
    private String selectedApproverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_patrol_creating);

        createTitleBar();
        titleBar.setLeftText("取消");
        titleBar.setTitle("创建监理巡查");

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.component_id);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupervisionPatrolCreatingActivity.this, SupervisionPatrolProjectDetailActivity.class);
                startActivity(intent);
            }
        });

        SupervisionPatrolService.getInstance().sendQuerySupervisionPatrolCheckItemList(new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                checkItemsModel = (CheckItemsModel) responseModel;
                updateSpinerAfterFetchSuccess();
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(SupervisionPatrolCreatingActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        setupCheckItems();
        setupDefaultImagePicker();
    }

    private void setupCheckItems() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.check_items);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupervisionPatrolCreatingActivity.this, CheckItemsSelectionActivity.class);
                startActivityForResult(intent, CheckItemsSelectionActivity.RequestCode);
            }
        });
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CheckItemsSelectionActivity.RequestCode && resultCode == RESULT_OK) {
            TextView textView = (TextView) findViewById(R.id.text_view_check_items);
            textView.setText(data.getStringExtra("content"));
            selectedCheckItemIds = data.getStringExtra("checkItemIds");
        }
    }

    public void updateSpinerAfterFetchSuccess() {
        final List<CheckItemsModel.Item> items = checkItemsModel.getCheckTypes();
        List<String> typeNames = new ArrayList<>();
        for (CheckItemsModel.Item item : items) {
            typeNames.add(item.getName());
        }
        String[] strArr = new String[typeNames.size()];
        typeNames.toArray(strArr);

        Spinner spinner = (Spinner) findViewById(R.id.spinner0);

        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, strArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                CheckItemsModel.Item item = items.get(pos);
                selectedTypeId = item.getID();
                SupervisionPatrolService.getInstance().sendQueryApproverList(selectedTypeId, new Callbacks() {
                    @Override
                    public void onSuccess(ResponseBase responseModel) {
                        approverListModel = (ApproverListModel) responseModel;
                        updateApproverSpinerAfterFetchSuccess();
                    }

                    @Override
                    public void onFailed(String error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    public void updateApproverSpinerAfterFetchSuccess() {
        final List<ApproverListModel.Item> items = approverListModel.getItems();
        List<String> names = new ArrayList<>();
        for (ApproverListModel.Item item : items) {
            names.add(item.getName());
        }
        String[] strArr = new String[names.size()];
        names.toArray(strArr);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_approver);

        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, strArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                ApproverListModel.Item item = items.get(pos);
                selectedApproverId = item.getID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }
}
