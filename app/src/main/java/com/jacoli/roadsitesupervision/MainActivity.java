package com.jacoli.roadsitesupervision;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends CommonActivity {

    private TodoListFragment todoListFragment;
    private SitesFragment sitesFragment;
    private InformationsFragment informationsFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createTitleBar();
        titleBar.setLeftText("注销");

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                setTabSelection(tabId);
            }
        });

        bottomBar.selectTabAtPosition(1);
        setTabSelection(R.id.tab_sites);
    }

    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);

        switch (index) {
            case R.id.tab_todo_list:
                titleBar.setTitle("我的任务");

                if (todoListFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    todoListFragment = new TodoListFragment();
                    transaction.add(R.id.contentContainer, todoListFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(todoListFragment);
                }
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

                if (informationsFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    informationsFragment = new InformationsFragment();
                    transaction.add(R.id.contentContainer, informationsFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(informationsFragment);
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
        if (informationsFragment != null) {
            transaction.hide(informationsFragment);
        }
        if (settingsFragment != null) {
            transaction.hide(settingsFragment);
        }
    }
}
