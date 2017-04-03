package com.jacoli.roadsitesupervision;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
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
    public static int RequestCodeToContacts = 2002;

    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();

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
        receiverEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().endsWith("@")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                    startActivityForResult(intent, RequestCodeToContacts);
                }
                else if (s.toString().endsWith("，")
                        || s.toString().endsWith(" ")
                        || s.toString().endsWith(".")
                        || s.toString().endsWith("。")) {
                    // 如果空格或中文逗号，则强制转换成英文逗号
                    String text = s.toString().replace('，', ',');
                    text = text.replace(' ', ',');
                    text = text.replace('.', ',');
                    text = text.replace('。', ',');
                    receiverEditText.setText(text);
                    receiverEditText.setSelection(text.length());
                    receiverEditText.requestFocus();
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

        if (requestCode == RequestCodeToContacts) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        Cursor cursor = getContentResolver()
                                .query(uri,
                                        new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME },
                                        null, null, null);
                        while (cursor.moveToNext()) {
                            String number = cursor.getString(0);
                            String name = cursor.getString(1);

                            EditText receiverEditText = (EditText) findViewById(R.id.edit_text_receiver);
                            String text = receiverEditText.getText().toString() + name;
                            text = text.replace('@', ',');
                            receiverEditText.setText(text);
                            receiverEditText.setSelection(text.length());
                            receiverEditText.requestFocus();
                        }
                    }
                }
            }

            // 清除字符串头部或尾部的"@"
            EditText receiverEditText = (EditText) findViewById(R.id.edit_text_receiver);
            String text = receiverEditText.getText().toString();
            if (text.startsWith("@") || text.startsWith(",")) {
                text = text.substring(1);
            }
            if (text.endsWith("@")) {
                text = text.substring(0, text.length() - 1);
            }

            receiverEditText.setText(text);
            receiverEditText.setSelection(text.length());
            receiverEditText.requestFocus();
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
        if (subject.isEmpty()) {
            Toast.makeText(this, "请输入承办人", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText contentEditText = (EditText) findViewById(R.id.editText);
        String content = contentEditText.getText().toString();

        if (content.isEmpty() && selectedPhotos.isEmpty()) {
            Toast.makeText(this, "请输入内容或选择图片", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!MainService.getInstance().submitAssignedMatter(receiver, subject, content, selectedPhotos, handler)) {
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
