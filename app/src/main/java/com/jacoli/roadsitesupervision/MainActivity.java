package com.jacoli.roadsitesupervision;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;

import com.jacoli.roadsitesupervision.views.TitleBar;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends CommonActivity {

    private TodoListFragment todoListFragment;
    private SitesFragment sitesFragment;
    private InformationFragment informationFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createTitleBar();
        //titleBar.setLeftText("注销");
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
                        Intent intent = new Intent(MainActivity.this, AssignedMatterCreateActivity.class);
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
        if (settingsFragment != null) {
            transaction.hide(settingsFragment);
        }

        titleBar.removeAllActions();
    }
}
