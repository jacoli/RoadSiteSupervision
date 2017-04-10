package com.jacoli.roadsitesupervision;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.Utils;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class AssignedMatterCreateActivity extends CommonActivity {

    public static int RequestCode = 2001;

    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private ArrayList<String> selectedStaffIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_matter_create);

        createTitleBar();
        titleBar.setLeftText("取消");
        titleBar.setTitle("创建交办事项");

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        setupImagePicker();

        EditText senderEditText = (EditText) findViewById(R.id.edit_text_sender);
        senderEditText.setText(MainService.getInstance().getLoginModel().getName());
        senderEditText.setEnabled(false);

        TextView textView = (TextView) findViewById(R.id.text_view_time);
        String time = Utils.getCurrentDateStr();
        textView.setText(time);

        final EditText receiverEditText = (EditText) findViewById(R.id.edit_text_receiver);
        receiverEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssignedMatterCreateActivity.this, StaffsActivity.class);
                intent.putStringArrayListExtra(StaffsActivity.KeyForStaffIds, selectedStaffIds);
                startActivityForResult(intent, StaffsActivity.RequestCode);
            }
        });
        receiverEditText.setFocusable(false);

        initSpinerAfterFetchOperatorListSuccess();
    }

    private void setupImagePicker() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        photoAdapter = new PhotoAdapter(this, selectedPhotos);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
                            PhotoPicker.builder()
                                    .setPhotoCount(PhotoAdapter.MAX)
                                    .setShowCamera(true)
                                    .setPreviewEnabled(false)
                                    .setSelected(selectedPhotos)
                                    .start(AssignedMatterCreateActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(AssignedMatterCreateActivity.this);
                        }
                    }
                }));
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();

            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }

        if (requestCode == StaffsActivity.RequestCode && resultCode == RESULT_OK) {
            try {
                selectedStaffIds = data.getStringArrayListExtra(StaffsActivity.KeyForStaffIds);
                EditText receiverEditText = (EditText) findViewById(R.id.edit_text_receiver);
                receiverEditText.setText(data.getStringExtra(StaffsActivity.KeyForStaffNames));
            } catch (Exception ex) {
                Log.e("", ex.toString());
            }
        }
    }

    public void submit() {
        EditText editText = (EditText) findViewById(R.id.edit_text_subject);
        final String subject = editText.getText().toString();
        if (subject.isEmpty()) {
            Toast.makeText(this, "请输入主题", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText recvEditText = (EditText) findViewById(R.id.edit_text_receiver);
        final String receiver = recvEditText.getText().toString();
        if (receiver.isEmpty()) {
            Toast.makeText(this, "请输入承办人", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText contentEditText = (EditText) findViewById(R.id.editText);
        final String content = contentEditText.getText().toString();

        if (content.isEmpty() && selectedPhotos.isEmpty()) {
            Toast.makeText(this, "请输入内容或选择图片", Toast.LENGTH_SHORT).show();
            return;
        }

        String tmp = "";
        for (int idx = 0; idx < selectedStaffIds.size(); ++idx) {
            String staffId = selectedStaffIds.get(idx);
            tmp += staffId;
            if (idx != selectedStaffIds.size() - 1) {
                tmp += ",";
            }
        }
        final String staffIds = tmp;

        Spinner spinner = (Spinner) findViewById(R.id.spinner0);
        final String type = (String) spinner.getSelectedItem();

        AlertDialog.Builder builder = new AlertDialog.Builder(AssignedMatterCreateActivity.this);
        builder.setTitle("提示");
        builder.setMessage("是否发送");

        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (!MainService.getInstance().submitAssignedMatter(type, staffIds, subject, content, selectedPhotos, handler)) {
                    Toast.makeText(AssignedMatterCreateActivity.this, "创建失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });

        builder.create().show();
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_SUBMIT_ASSIGNED_MATTER_SUCCESS:
                Toast.makeText(this, "创建交办事项成功", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
                break;
            case MainService.MSG_SUBMIT_ASSIGNED_MATTER_FAILED:
                Toast.makeText(this, "创建失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void initSpinerAfterFetchOperatorListSuccess() {
        String[] types = {"进度", "安全", "质量", "文明施工"};

        Spinner spinner = (Spinner) findViewById(R.id.spinner0);

        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner .setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }
}
