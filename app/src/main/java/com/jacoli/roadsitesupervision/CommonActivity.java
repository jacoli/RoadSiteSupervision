package com.jacoli.roadsitesupervision;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.os.Handler;
import android.os.Message;

import com.jacoli.roadsitesupervision.views.TitleBar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

public class CommonActivity extends Activity {

    static class MyHandler extends Handler {
        WeakReference<CommonActivity> weakActivity;

        public MyHandler(CommonActivity fragment){
            weakActivity = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakActivity != null) {
                weakActivity.get().onResponse(msg.what);
                weakActivity.get().onResponse(msg.what, msg.obj);
            }
        }
    }

    public MyHandler handler = new MyHandler(this);

    public void onResponse(int msgCode) {
    }

    public void onResponse(int msgCode, Object obj) {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("CommonActivity", "onCreate " + this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("CommonActivity", "onStart " + this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("CommonActivity", "onStop " + this);
    }


    public TitleBar titleBar;

    public void createTitleBar() {
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.primary_dark));

        //titleBar.setLeftImageResource(R.drawable.ic_back_arrow);
        titleBar.setLeftText("返回");
        titleBar.setLeftTextColor(Color.WHITE);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleBar.setTitle("文章详情");
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setSubTitleColor(Color.GREEN);
        titleBar.setDividerColor(Color.GRAY);
    }

    public static void compressPicture(String srcPath, String desPath) {
        FileOutputStream fos = null;
        BitmapFactory.Options op = new BitmapFactory.Options();

        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        op.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, op);
        op.inJustDecodeBounds = false;

        // 缩放图片的尺寸
        float w = op.outWidth;
        float h = op.outHeight;
        float hh = 1024f;//
        float ww = 1024f;//
        // 最长宽度或高度1024
        float be = 1.0f;
        if (w > h && w > ww) {
            be = (float) (w / ww);
        } else if (w < h && h > hh) {
            be = (float) (h / hh);
        }
        if (be <= 0) {
            be = 1.0f;
        }
        op.inSampleSize = (int) be;// 设置缩放比例,这个数字越大,图片大小越小.
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, op);
        int desWidth = (int) (w / be);
        int desHeight = (int) (h / be);
        bitmap = Bitmap.createScaledBitmap(bitmap, desWidth, desHeight, true);
        try {
            fos = new FileOutputStream(desPath);
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
