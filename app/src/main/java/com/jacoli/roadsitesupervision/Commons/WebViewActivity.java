package com.jacoli.roadsitesupervision.Commons;


import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.services.Utils;

public class WebViewActivity extends CommonActivity {

    final public static String WEBVIEW_EXTRA_TITLE = "title";
    final public static String WEBVIEW_EXTRA_URL = "url";

    private String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        createTitleBar();
        titleBar.setLeftText("返回");

        Intent intent = getIntent();
        webUrl = intent.getStringExtra(WEBVIEW_EXTRA_URL);
        String title = intent.getStringExtra(WEBVIEW_EXTRA_TITLE);
        titleBar.setTitle(Utils.notNullString(title));

        WebView webView = (WebView)findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setLoadWithOverviewMode(true);

        webView.setWebViewClient(new MyWebViewClient());
        // 使用WebChormClient的特性处理html页面
        webView.setWebChromeClient(new MyWebChormClient());

        if (webUrl != null && webUrl.length() > 0) {
            webView.loadUrl(webUrl);
        }
    }
}
