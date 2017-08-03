package com.jacoli.roadsitesupervision;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.jacoli.roadsitesupervision.services.MainService;
import java.util.ArrayList;
import java.util.List;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class AssignedMatterReplyActivity extends CommonActivity {

    public static int RequestCode = 2001;

    private String matterId;
    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_matter_reply);

        createTitleBar();

        titleBar.setTitle("回复");

        Intent intent = getIntent();
        matterId = intent.getStringExtra("id");

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        setupImagePicker();
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
                                    .start(AssignedMatterReplyActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(AssignedMatterReplyActivity.this);
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
    }

    public void submit() {
        EditText editText = (EditText) findViewById(R.id.editText);
        String content = editText.getText().toString();

        if (content.isEmpty() && selectedPhotos.isEmpty()) {
            Toast.makeText(this, "请输入内容或选择图片", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!MainService.getInstance().replyAssignedMatter(matterId, content, selectedPhotos, handler)) {
            Toast.makeText(this, "回复失败", Toast.LENGTH_SHORT).show();
        } else {
            Button submitBtn = (Button) findViewById(R.id.submit_btn);
            submitBtn.setEnabled(false);
        }
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_REPLY_ASSIGNED_MATTER_SUCCESS:
                setResult(RESULT_OK);
                finish();
                break;
            case MainService.MSG_REPLY_ASSIGNED_MATTER_FAILED:
                Toast.makeText(this, "回复失败", Toast.LENGTH_SHORT).show();
                Button submitBtn = (Button) findViewById(R.id.submit_btn);
                submitBtn.setEnabled(true);
                break;
            default:
                break;
        }
    }
}
