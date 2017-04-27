package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.services.Utils;
import java.util.ArrayList;
import java.util.List;

// 创建监理巡查
public class SupervisionPatrolCreatingActivity extends CommonActivity {

    static int ProjectDetailSelectorRequestCode = 1000;
    static int UnitProjectSelectorRequestCode = 1001;
    static int CheckItemsMainSelectorRequestCode = 1002;
    static int CheckItemsSubSelectorRequestCode = 1003;

    private CheckItemsModel checkItemsModel;
    private String selectedCheckItemIds;
    private ApproverListModel approverListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_patrol_creating);

        createTitleBar();
        titleBar.setLeftText("取消");
        titleBar.setTitle("发现问题");

        SupervisionPatrolService.getInstance().sendQuerySupervisionPatrolCheckItemList(new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                checkItemsModel = (CheckItemsModel) responseModel;
                updateCheckTypeSelectorAfterFetchSuccess();
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(SupervisionPatrolCreatingActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        setupCheckItems();
        setupDefaultImagePicker();

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        setupComponentSelector();
    }

    public void updateCheckTypeSelectorAfterFetchSuccess() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                checkItemsModel.getCheckTypeDescriptions());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_check_type);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                onCheckTypeSelected(checkItemsModel.getCheckTypes().get(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupComponentSelector() {
        Button submitBtn = (Button) findViewById(R.id.btn_component);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SupervisionPatrolProjectDetailActivity.class);
                startActivityForResult(intent, ProjectDetailSelectorRequestCode);
            }
        });
    }

    private CheckItemsModel.Item getSelectedCheckTypeItem() {
        if (checkItemsModel != null) {
            final List<CheckItemsModel.Item> items = checkItemsModel.getCheckTypes();
            Spinner approvalSpinner = (Spinner) findViewById(R.id.spinner_check_type);
            int position = approvalSpinner.getSelectedItemPosition();
            if (items != null && position >= 0 && items.size() > position) {
                return items.get(position);
            }
        }
        return null;
    }

    private void setupCheckItems() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.check_items);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckItemsModel.Item item = getSelectedCheckTypeItem();
                if (item != null) {
                    if (item.shouldMultiPageSelection()) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("object", item);
                        Intent intent = new Intent(SupervisionPatrolCreatingActivity.this, CheckItemsMainSelectorActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, CheckItemsMainSelectorRequestCode);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("object", item);
                        Intent intent = new Intent(SupervisionPatrolCreatingActivity.this, CheckItemsSubSelectorActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, CheckItemsSubSelectorRequestCode);
                    }
                }
            }
        });
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CheckItemsSubSelectorRequestCode) {
                TextView textView = (TextView) findViewById(R.id.text_view_check_items);
                textView.setText(data.getStringExtra("content"));
                selectedCheckItemIds = data.getStringExtra("checkItemIds");
                TextView actionsTextView = (TextView) findViewById(R.id.text_view_check_items_actions);
                actionsTextView.setText("重新选择");
            } else if (requestCode == CheckItemsMainSelectorRequestCode) {
                CheckItemsModel.Item item = (CheckItemsModel.Item) data.getExtras().getSerializable("object");
                if (item != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("object", item);
                    Intent intent = new Intent(SupervisionPatrolCreatingActivity.this, CheckItemsSubSelectorActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, CheckItemsSubSelectorRequestCode);
                }
            } else if (requestCode == ProjectDetailSelectorRequestCode) {
                Intent intent = new Intent(getBaseContext(), SupervisionPatrolUnitProjectDetailActivity.class);
                intent.putExtra("title", data.getStringExtra("title"));
                intent.putExtra("id", data.getStringExtra("id"));
                startActivityForResult(intent, UnitProjectSelectorRequestCode);
            } else if (requestCode == UnitProjectSelectorRequestCode) {
                EditText editText = (EditText) findViewById(R.id.edit_text_component);
                editText.setText(data.getStringExtra("title"));
            }
        }
    }

    private void onCheckTypeSelected(CheckItemsModel.Item item) {
        approverListModel = null;
        updateApproverSpiner();

        SupervisionPatrolService.getInstance().sendQueryApproverList(getSelectedCheckTypeId(), new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                approverListModel = (ApproverListModel) responseModel;
                updateApproverSpiner();
            }

            @Override
            public void onFailed(String error) {
            }
        });

        TextView textView = (TextView) findViewById(R.id.text_view_check_items);
        textView.setText("");
        selectedCheckItemIds = null;
        TextView actionsTextView = (TextView) findViewById(R.id.text_view_check_items_actions);
        actionsTextView.setText("点击选择");
    }

    public String[] getApproverNames() {
        String[] strArr = {};
        if (approverListModel != null) {
            final List<ApproverListModel.Item> items = approverListModel.getItems();
            List<String> names = new ArrayList<>();
            for (ApproverListModel.Item item : items) {
                names.add(item.getName());
            }
            strArr = new String[names.size()];
            names.toArray(strArr);
        }

        return strArr;
    }

    public void updateApproverSpiner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_approver);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, getApproverNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private String getSelectedCheckTypeId() {
        if (checkItemsModel != null) {
            final List<CheckItemsModel.Item> items = checkItemsModel.getCheckTypes();
            Spinner approvalSpinner = (Spinner) findViewById(R.id.spinner_check_type);
            int position = approvalSpinner.getSelectedItemPosition();
            if (items != null && position >= 0 && items.size() > position) {
                return items.get(position).getID();
            }
        }
        return null;
    }

    private String getSelectedApproverId() {
        String selectedApproverId = null;
        if (approverListModel != null) {
            Spinner approvalSpinner = (Spinner) findViewById(R.id.spinner_approver);
            selectedApproverId = approverListModel.getIDAtPosition(approvalSpinner.getSelectedItemPosition());
        }
        return selectedApproverId;
    }

    public void submit() {
        EditText editText = (EditText) findViewById(R.id.edit_text_component);
        String componentName = editText.getText().toString();
        if (Utils.isStringEmpty(componentName)) {
            Toast.makeText(this, "工程构件名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedCheckTypeId = getSelectedCheckTypeId();
        if (Utils.isStringEmpty(selectedCheckTypeId)) {
            Toast.makeText(this, "检查大项不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedApproverId = getSelectedApproverId();
        if (Utils.isStringEmpty(selectedApproverId)) {
            Toast.makeText(this, "审批人不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Utils.isStringEmpty(selectedCheckItemIds)) {
            Toast.makeText(this, "检查细目不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText contentEditText = (EditText) findViewById(R.id.edit_text_content);
        String content = contentEditText.getText().toString();
        if (Utils.isStringEmpty(content)) {
            Toast.makeText(this, "巡查内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        SupervisionPatrolService.getInstance().sendCreateSupervisionPatrol(componentName,
                selectedCheckTypeId,
                selectedCheckItemIds,
                content,
                selectedApproverId,
                getCommonSelectedPhotoUrls(),
                new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                Toast.makeText(SupervisionPatrolCreatingActivity.this, "提交成功", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailed(String error) {
                Button submitBtn = (Button) findViewById(R.id.submit_btn);
                submitBtn.setEnabled(true);
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setEnabled(false);
    }
}
