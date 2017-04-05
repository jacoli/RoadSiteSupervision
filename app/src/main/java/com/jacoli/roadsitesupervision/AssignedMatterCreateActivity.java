package com.jacoli.roadsitesupervision;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        TextView textView = (TextView) findViewById(R.id.text_view_time);
        String time = Utils.getCurrentDateStr();
        textView.setText(time);

        final EditText receiverEditText = (EditText) findViewById(R.id.edit_text_receiver);
        receiverEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    receiverEditText.clearFocus();
                    Intent intent = new Intent(AssignedMatterCreateActivity.this, StaffsActivity.class);
                    intent.putStringArrayListExtra(StaffsActivity.KeyForStaffIds, selectedStaffIds);
                    startActivityForResult(intent, StaffsActivity.RequestCode);
                }
            }
        });
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
        String subject = editText.getText().toString();
        if (subject.isEmpty()) {
            Toast.makeText(this, "请输入主题", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText recvEditText = (EditText) findViewById(R.id.edit_text_receiver);
        String receiver = recvEditText.getText().toString();
        if (receiver.isEmpty()) {
            Toast.makeText(this, "请输入承办人", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText contentEditText = (EditText) findViewById(R.id.editText);
        String content = contentEditText.getText().toString();

        if (content.isEmpty() && selectedPhotos.isEmpty()) {
            Toast.makeText(this, "请输入内容或选择图片", Toast.LENGTH_SHORT).show();
            return;
        }

        String staffIds = "";
        for (int idx = 0; idx < selectedStaffIds.size(); ++idx) {
            String staffId = selectedStaffIds.get(idx);
            staffIds += staffId;
            if (idx != selectedStaffIds.size() - 1) {
                staffIds += ",";
            }
        }

        if (!MainService.getInstance().submitAssignedMatter(staffIds, subject, content, selectedPhotos, handler)) {
            Toast.makeText(this, "创建失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_SUBMIT_ASSIGNED_MATTER_SUCCESS:
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
}
