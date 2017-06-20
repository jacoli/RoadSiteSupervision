package com.jacoli.roadsitesupervision.MonitorMain;

import android.os.Bundle;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.DataMonitor.DataMonitorFragment;
import com.jacoli.roadsitesupervision.R;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.View;
import com.jacoli.roadsitesupervision.AssignedMatterCreateActivity;
import com.jacoli.roadsitesupervision.TodoListFragment;
import com.jacoli.roadsitesupervision.UserSystem.ComponentDetailFragment;
import com.jacoli.roadsitesupervision.views.TitleBar;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MonitorMainActivity extends CommonActivity {

    private TodoListFragment todoListFragment;
    private SitesFragment sitesFragment;
    private DataMonitorFragment informationFragment;
    private ComponentDetailFragment componentInfoFragment;
    private MonitorSettingFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_main);

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
        setTabSelection(R.id.tab_todo_list);
    }

    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);

        switch (index) {
            case R.id.tab_todo_list:
                titleBar.setTitle("情况速报");

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
                        Intent intent = new Intent(MonitorMainActivity.this, AssignedMatterCreateActivity.class);
                        startActivity(intent);
                    }
                });

                break;
            case R.id.tab_sites:
                titleBar.setTitle("安全巡查");

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
                titleBar.setTitle("资料文件");

                if (componentInfoFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    componentInfoFragment = new ComponentDetailFragment();
                    //componentInfoFragment.mainTabActivityWeakReference = new WeakReference<>(this);
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
}

