package com.jacoli.roadsitesupervision.UserSystem;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.CommonFragment;
import com.jacoli.roadsitesupervision.Commons.WebViewActivity;
import com.jacoli.roadsitesupervision.MonitorMain.MonitorMainActivity;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.services.Utils;
import com.lichuange.bridges.scan.scan.qrmodule.CaptureActivity;

import java.lang.ref.WeakReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComponentDetailFragment extends CommonFragment {

    private View selfView;
    public WeakReference<MainTabActivity> mainTabActivityWeakReference;

    public ComponentDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        selfView = getActivity().getLayoutInflater().inflate(R.layout.fragment_user_system_component_detail, container, false);

        // 跳转到扫码界面
        Button scanBtn = (Button) selfView.findViewById(R.id.submit_btn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) selfView.findViewById(R.id.text_view_content);
                textView.setText("");

                if (mainTabActivityWeakReference != null) {
                    mainTabActivityWeakReference.get().scan();
                }
            }
        });

        return selfView;
    }

    public void onScanedSuccess(String text) {
        if (!Utils.isStringEmpty(text)
                && (text.startsWith("http://")
                || text.startsWith("https://"))) {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra(WebViewActivity.WEBVIEW_EXTRA_URL, text);
            //intent.putExtra(WebViewActivity.WEBVIEW_EXTRA_TITLE, models[position]);
            startActivity(intent);
        } else {
            TextView textView = (TextView) selfView.findViewById(R.id.text_view_content);
            textView.setText(text);
        }
    }
}
