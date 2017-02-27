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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jacoli.roadsitesupervision.services.ImageUrlModel;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.SamplingInspectionModel;
import com.jacoli.roadsitesupervision.services.Utils;
import com.jacoli.roadsitesupervision.views.MyToast;
import java.util.ArrayList;
import java.util.List;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class SamplingInspectionActivity extends CommonActivity {
    private String id;
    private int type;
    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private SamplingInspectionModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sampling_inspection);

        type = getIntent().getIntExtra("type", MainService.project_detail_type_pz);
        id = getIntent().getStringExtra("id");

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle(getIntent().getStringExtra("title"));
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView textView = (TextView) findViewById(R.id.name_text);
        String nameText = "工序部位：" + getIntent().getStringExtra("name");
        textView.setText(nameText);

        EditText editText = (EditText) findViewById(R.id.editText);
        editText.clearFocus();
        editText.setVisibility(View.INVISIBLE);

        setupImagePicker();

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        //
        if (MainService.getInstance().sendQueryComponentSamplingInspection(id, handler)) {
            MyToast.showMessage(this, "正在查询抽检情况...");
        }
    }

    private void setupImagePicker() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        photoAdapter = new PhotoAdapter(this, selectedPhotos);
        recyclerView.setVisibility(View.INVISIBLE);

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
                                    .start(SamplingInspectionActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(SamplingInspectionActivity.this);
                        }
                    }
                }));
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("抽检情况可能未保存，是否仍要返回上一页");

        builder.setPositiveButton("返回上一页", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                finish();
            }
        });

        builder.setNegativeButton("留在当前页面", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });

        builder.create().show();
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_QUERY_COMPONENT_SAMPLING_INSPECTION_SUCCESS:
                //MyToast.showMessage(getBaseContext(), "获取抽检情况成功");
                model = (SamplingInspectionModel) obj;
                onQuerySuccess();
                break;
            case MainService.MSG_QUERY_COMPONENT_SAMPLING_INSPECTION_FAILED:
                MyToast.showMessage(getBaseContext(), "获取抽检情况失败");
                break;
            case MainService.MSG_SUBMIT_COMPONENT_SAMPLING_INSPECTION_SUCCESS:
                onSubmitSuccess();
                break;
            case MainService.MSG_SUBMIT_COMPONENT_SAMPLING_INSPECTION_FAILED:
                MyToast.showMessage(getBaseContext(), "提交抽检情况失败");
                break;
            default:
                break;
        }
    }

    public void onQuerySuccess() {
        if (model.getIsExist().equalsIgnoreCase("true") || model.getIsExist().equalsIgnoreCase("y")) {
            EditText editText = (EditText) findViewById(R.id.editText);
            editText.setText(model.getSituation());

            for (ImageUrlModel imageUrlModel : model.getPhotoList()) {
                if (imageUrlModel.getWebPath().length() > 0) {
                    selectedPhotos.add(imageUrlModel.getWebPath());
                }
            }

            photoAdapter.notifyDataSetChanged();
        }

        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void onSubmitSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("提交抽检情况成功，是否留在当前页面");

        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });

        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                finish();
            }
        });

        builder.create().show();
    }

    public void submit() {
        if (id == null || id.length() < 0) {
            return;
        }

        try {
            EditText editText = (EditText) findViewById(R.id.editText);

            List<String> imgUrls = new ArrayList<>();
            for (String imgUrl : selectedPhotos) {
                if (imgUrl.startsWith("https") || imgUrl.startsWith("http")) {
                }
                else {
                    String imgUrlSmall = getCacheDir() + "/tmp" + selectedPhotos.indexOf(imgUrl) + ".jpg";
                    Log.d("ImageUrl", imgUrlSmall);
                    CommonActivity.compressPicture(imgUrl, imgUrlSmall);
                    imgUrls.add(imgUrlSmall);
                }
            }

            String delFiles = "";

            if (model != null && (model.getIsExist().equalsIgnoreCase("true") || model.getIsExist().equalsIgnoreCase("y"))) {
                for (ImageUrlModel imageUrlModel : model.getPhotoList()) {
                    if (imageUrlModel.getWebPath().length() > 0) {

                        boolean isImgUrlDel = true;

                        for (String imgUrl : selectedPhotos) {

                            if (imgUrl.startsWith("https") || imgUrl.startsWith("http")) {
                                if (imgUrl.equals(imageUrlModel.getWebPath())) {
                                    isImgUrlDel = false;
                                    break;
                                }
                            }
                        }

                        if (isImgUrlDel) {
                            delFiles += imageUrlModel.getOrdinal() + Utils.MultipartSeparator;
                        }
                    }
                }
            }

            MainService.getInstance().sendSubmitComponentSamplingInspection(id,
                    editText.getText().toString(), delFiles, imgUrls, handler);
        }
        catch (Exception ex) {
            Log.e("InspectionDetail", ex.toString());
        }
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
}
