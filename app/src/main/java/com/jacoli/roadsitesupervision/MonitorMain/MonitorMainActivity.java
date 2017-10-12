package com.jacoli.roadsitesupervision.MonitorMain;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.DataMonitor.DataMonitorFragment;
import com.jacoli.roadsitesupervision.DataMonitor.MessagesFragment;
import com.jacoli.roadsitesupervision.R;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.jacoli.roadsitesupervision.AssignedMatterCreateActivity;
import com.jacoli.roadsitesupervision.TodoListFragment;
import com.jacoli.roadsitesupervision.UserSystem.ComponentDetailFragment;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.views.TitleBar;
import com.lichuange.bridges.scan.scan.qrmodule.CaptureActivity;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.lang.ref.WeakReference;

public class MonitorMainActivity extends CommonActivity {
    static public final int request_camera_auth_code = 1001;
    static public final int requset_scanner_code = 1002;

    private TodoListFragment todoListFragment;
    private SitesFragment sitesFragment;
    private DataMonitorFragment informationFragment;
    private DeviceManagerFragment deviceManagerFragment;
    private MonitorSettingFragment settingsFragment;
    private MessagesFragment messagesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainService.getInstance().getLoginModel().isDeviceMange()) {
            setContentView(R.layout.activity_monitor_main);
        } else {
            setContentView(R.layout.activity_monitor_main_no_device_manager);
        }

        createTitleBar();
        titleBar.setLeftVisible(false);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                setTabSelection(tabId);
            }
        });

        bottomBar.selectTabAtPosition(0);
        setTabSelection(R.id.tab_informations);

        //bottomBar.getTabWithId(R.id.tab_messages).setBadgeCount(5);
    }

    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);

        switch (index) {
//            case R.id.tab_todo_list:
//                titleBar.setTitle("情况速报");
//
//                if (todoListFragment == null) {
//                    // 如果MessageFragment为空，则创建一个并添加到界面上
//                    todoListFragment = new TodoListFragment();
//                    transaction.add(R.id.contentContainer, todoListFragment);
//                } else {
//                    // 如果MessageFragment不为空，则直接将它显示出来
//                    transaction.show(todoListFragment);
//                }
//
//                titleBar.addAction(new TitleBar.TextAction("创建") {
//                    @Override
//                    public void performAction(View view) {
//                        Intent intent = new Intent(MonitorMainActivity.this, AssignedMatterCreateActivity.class);
//                        startActivity(intent);
//                    }
//                });
//
//                break;
            case R.id.tab_messages:
                titleBar.setTitle("报警信息");

                if (messagesFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    messagesFragment = new MessagesFragment();
                    transaction.add(R.id.contentContainer, messagesFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(messagesFragment);
                }
                break;
            case R.id.tab_informations:
                titleBar.setTitle("数据监控");

                if (informationFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    informationFragment = new DataMonitorFragment();
                    transaction.add(R.id.contentContainer, informationFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(informationFragment);
                }
                break;
            case R.id.tab_component_info:
                titleBar.setTitle("设备管理");

                if (deviceManagerFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    deviceManagerFragment = new DeviceManagerFragment();
                    transaction.add(R.id.contentContainer, deviceManagerFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(deviceManagerFragment);
                }
                break;
            case R.id.tab_settings:
                titleBar.setTitle("系统设置");

                if (settingsFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    settingsFragment = new MonitorSettingFragment();
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
        if (messagesFragment != null) {
            transaction.hide(messagesFragment);
        }
        if (informationFragment != null) {
            transaction.hide(informationFragment);
        }
        if (deviceManagerFragment != null) {
            transaction.hide(deviceManagerFragment);
        }
        if (settingsFragment != null) {
            transaction.hide(settingsFragment);
        }

        titleBar.removeAllActions();
    }
}

