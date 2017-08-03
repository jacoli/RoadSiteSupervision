package com.jacoli.roadsitesupervision.Commons;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import com.bm.library.PhotoView;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.services.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class ImagePreviewActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        createTitleBar();

        String title = getIntent().getStringExtra("title");
        if (Utils.isStringEmpty(title)) {
            title = "查看图片";
        }
        titleBar.setTitle(title);

        String url = getIntent().getStringExtra("url");
        ImageLoader imageLoader = ImageLoader.getInstance();
        PhotoView imageView = (PhotoView) findViewById(R.id.image_view);
        imageView.enable();
        imageLoader.displayImage(url, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                showHUD();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                dismissHUD();
                showToast("图片加载失败");
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                dismissHUD();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                dismissHUD();
            }
        });
    }
}
