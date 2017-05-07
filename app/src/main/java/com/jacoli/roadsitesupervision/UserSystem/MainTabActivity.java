package com.jacoli.roadsitesupervision.UserSystem;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.jacoli.roadsitesupervision.AssignedMatterCreateActivity;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.InformationFragment;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.SettingsFragment;
import com.jacoli.roadsitesupervision.TodoListFragment;
import com.jacoli.roadsitesupervision.views.TitleBar;
import com.lichuange.bridges.scan.scan.qrmodule.CaptureActivity;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.lang.ref.WeakReference;

public class MainTabActivity extends CommonActivity {

    static public final int request_camera_auth_code = 1001;
    static public final int requset_scanner_code = 1002;

    private TodoListFragment todoListFragment;
    private SitesFragment sitesFragment;
    private InformationFragment informationFragment;
    private ComponentDetailFragment componentInfoFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_system_main_tab);

        createTitleBar();
        titleBar.setLeftVisible(false);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                setTabSelection(tabId);
            }
        });

        bottomBar.selectTabAtPosition(0);
        setTabSelection(R.id.tab_todo_list);
    }

    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);

        switch (index) {
            case R.id.tab_todo_list:
                titleBar.setTitle("交办事项");

                if (todoListFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    todoListFragment = new TodoListFragment();
                    transaction.add(R.id.contentContainer, todoListFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(todoListFragment);
                }

                titleBar.addAction(new TitleBar.TextAction("创建") {
                    @Override
                    public void performAction(View view) {
                        Intent intent = new Intent(MainTabActivity.this, AssignedMatterCreateActivity.class);
                        startActivity(intent);
                    }
                });

                break;
            case R.id.tab_sites:
                titleBar.setTitle("现场工作");

                if (sitesFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    sitesFragment = new SitesFragment();
                    transaction.add(R.id.contentContainer, sitesFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(sitesFragment);
                }
                break;
            case R.id.tab_informations:
                titleBar.setTitle("信息查询");

                if (informationFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    informationFragment = new InformationFragment();
                    transaction.add(R.id.contentContainer, informationFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(informationFragment);
                }
                break;
            case R.id.tab_component_info:
                titleBar.setTitle("构件信息");

                if (componentInfoFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    componentInfoFragment = new ComponentDetailFragment();
                    componentInfoFragment.mainTabActivityWeakReference = new WeakReference<>(this);
                    transaction.add(R.id.contentContainer, componentInfoFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(componentInfoFragment);
                }
                break;
            case R.id.tab_settings:
                titleBar.setTitle("系统设置");

                if (settingsFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    settingsFragment = new SettingsFragment();
                    transaction.add(R.id.contentContainer, settingsFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(settingsFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (todoListFragment != null) {
            transaction.hide(todoListFragment);
        }
        if (sitesFragment != null) {
            transaction.hide(sitesFragment);
        }
        if (informationFragment != null) {
            transaction.hide(informationFragment);
        }
        if (componentInfoFragment != null) {
            transaction.hide(componentInfoFragment);
        }
        if (settingsFragment != null) {
            transaction.hide(settingsFragment);
        }

        titleBar.removeAllActions();
    }

    public void scan() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivityForResult(intent, requset_scanner_code);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, request_camera_auth_code);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case requset_scanner_code: {
                if (resultCode == CaptureActivity.ZXING_SCAN_RESULT_CODE && data != null) {
                    final String contentUri = data.getStringExtra(CaptureActivity.ZXING_SCAN_CONTENT_DATA);
                    //showToast(("扫码成功，二维码：" + contentUri));
                    showToast("获取构件详情成功");
                    componentInfoFragment.onScanedSuccess(contentUri);
                }
                break;
            }
            default:
                // ignored
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == request_camera_auth_code) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, requset_scanner_code);
            } else {
                // Permission Denied
                showToast("扫码需要相机权限，请在设置中打开");
            }
        }
    }
}
