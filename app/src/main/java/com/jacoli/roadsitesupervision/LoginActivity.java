package com.jacoli.roadsitesupervision;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.MonitorMain.MonitorMainActivity;
import com.jacoli.roadsitesupervision.ProjectConfigs.Configs;
import com.jacoli.roadsitesupervision.Upgrade.download.DownLoadUtils;
import com.jacoli.roadsitesupervision.Upgrade.download.DownloadApk;
import com.jacoli.roadsitesupervision.UserSystem.MainTabActivity;
import com.jacoli.roadsitesupervision.Utils.*;
import com.jacoli.roadsitesupervision.services.*;
import com.jacoli.roadsitesupervision.services.Utils;
import com.jacoli.roadsitesupervision.views.MyToast;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginActivity extends CommonActivity {

    private boolean isCreating = true;

    private BGConfigsModel configsModel;

    private void showMainActivity() {
        if (BuildConfig.ProjectIdentifer.equals("Project9002")
                || BuildConfig.ProjectIdentifer.equals("Project9003")) {
            Intent intent = new Intent(LoginActivity.this, MainTabActivity.class);
            startActivity(intent);
        } else if (BuildConfig.ProjectIdentifer.equals("ProjectMonitor")) {
            Intent intent = new Intent(LoginActivity.this, MonitorMainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView titleTextView = (TextView) findViewById(R.id.login_title);
        titleTextView.setText(BuildConfig.AppTitle);

        TextView vendorTextView = (TextView) findViewById(R.id.text_view_vendor);
        vendorTextView.setText(BuildConfig.CompanyName);

        final Button loginBtn = (Button) findViewById(R.id.login_btn);
        final EditText usernameEdit = (EditText) findViewById(R.id.usernameEdit);
        final EditText passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        final CheckBox rememberUserName = (CheckBox) findViewById(R.id.rememberUserName);
        final CheckBox rememberPassword = (CheckBox) findViewById(R.id.rememberPassword);
        final Button serverIPBtn = (Button) findViewById(R.id.login_server_ip_btn);

        configsModel = BGConfigsModel.fetch(this);

        MainService.getInstance().setServerAddress(configsModel.getServerAddress(), configsModel.getServerPort());

        rememberUserName.setChecked(configsModel.isRememberUserName());
        rememberPassword.setChecked(configsModel.isRememberPassword());

        final LoginActivity activity = this;

        rememberUserName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                configsModel.setRememberUserName(arg1);

                if (!arg1) {
                    configsModel.setUserName(null);
                    if (configsModel.isRememberPassword()) {
                        rememberPassword.setChecked(false);
                    }
                }

                configsModel.persist(activity);
            }
        });

        rememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                configsModel.setRememberPassword(arg1);

                if (arg1) {
                    if (!configsModel.isRememberUserName()) {
                        rememberUserName.setChecked(true);
                    }
                } else {
                    configsModel.setPassword(null);
                }

                configsModel.persist(activity);
            }
        });

        serverIPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCreating = true;
                Intent intent = new Intent(LoginActivity.this, ServerAddressActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate username or password
                String username = usernameEdit.getText().toString();
                if (username.length() == 0) {
                    Toast.makeText(getApplicationContext(), "帐号为空，请输入", Toast.LENGTH_SHORT).show();
                    return;
                }

                String password = passwordEdit.getText().toString();
                if (password.length() == 0) {
                    Toast.makeText(getApplicationContext(), "密码为空，请输入", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (MainService.getInstance().login(username, password, handler)) {
                    MyToast.showMessage(getApplicationContext(), "登录中，请稍后");
                } else {
                    Toast.makeText(getApplicationContext(), "登录失败，异常", Toast.LENGTH_SHORT).show();
                }
            }
        });

        try {
            FileInputStream stream = this.openFileInput("login.s");
            ObjectInputStream ois = new ObjectInputStream(stream);
            LoginModel model = (LoginModel) ois.readObject();

            if (model != null && model.getExpirDate() != null && model.isLoginSuccess()) {
                if (!Utils.isCurrentTimeExpired(model.getExpirDate())) {
                    // 自动登录
                    MainService.getInstance().setLoginModel(model);
                    MyToast.showMessage(getApplicationContext(), "Token仍有效，自动登录");

                    showMainActivity();
                }
            }
        } catch (Exception e) {
        } finally {
        }

        TextView versionTextView = (TextView) findViewById(R.id.version_text);
        String version = "版本：" + CommonUtils.getVersionName(getApplicationContext());
        versionTextView.setText(version);

        setupUpgradeCheck();
    }

    private void setupUpgradeCheck() {
        // 1.注册下载广播接收器
        DownloadApk.registerBroadcast(this);

        // 2.删除已存在的Apk
        DownloadApk.removeFile(this);

        // 3.查询是否有更新
        MainService.getInstance().sendQueryUpgrade(new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                UpgradeModel model = (UpgradeModel) responseModel;
                if (!Utils.isStringEmpty(model.getVer())
                        && !Utils.isStringEmpty(model.getURL())) {
                    try {
                        String latestVersion = model.getVer().replace(".", "");
                        String currentVersion = CommonUtils.getVersionName(getApplicationContext()).replace(".", "");
                        int latestVersionCode = Integer.valueOf(latestVersion);
                        int currentVersionCode = Integer.valueOf(currentVersion);
                        if (currentVersionCode < latestVersionCode) {
                            onUpgrade(model);
                        }
                    } catch (Exception e) {
                        Log.e("LoginActivity", e.toString());
                    }
                }
            }

            @Override
            public void onFailed(String error) {
            }
        });
    }

    public void onUpgrade(final UpgradeModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("有新版本");
        builder.setMessage(model.getUpdate());

        builder.setPositiveButton("马上更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                // 4.如果手机已经启动下载程序，执行downloadApk。否则跳转到设置界面
                if (DownLoadUtils.getInstance(getApplicationContext()).canDownload()) {
                    DownloadApk.downloadApk(getApplicationContext(), model.getURL(), model.getUpdate(), CommonUtils.getApplicationName(getApplicationContext()));
                } else {
                    DownLoadUtils.getInstance(LoginActivity.this).skipToDownloadManager();
                }
            }
        });

        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });

        builder.create().show();
    }

    @Override
    protected void onDestroy() {

        // 5.反注册广播接收器
        DownloadApk.unregisterBroadcast(this);
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (isCreating) {
            isCreating = false;
        }
        else {
            try {
                LoginModel model = MainService.getInstance().getLoginModel();
                model.setToken("");
                FileOutputStream stream = this.openFileOutput("login.s", MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(stream);
                oos.writeObject(model);//td is an Instance of TableData;
            }
            catch (Exception e) {
            }
            finally {
            }

            MainService.getInstance().logout();
        }

        EditText usernameEdit = (EditText)findViewById(R.id.usernameEdit);
        EditText passwordEdit = (EditText)findViewById(R.id.passwordEdit);
        usernameEdit.setText(configsModel.getUserName());
        passwordEdit.setText(configsModel.getPassword());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResponse(int msgCode) {
        switch (msgCode) {
            case MainService.MSG_LOGIN_SUCCESS: {

                // 登录数据写到文件
                try {
                    LoginModel model = MainService.getInstance().getLoginModel();
                    FileOutputStream stream = this.openFileOutput("login.s", MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(stream);
                    oos.writeObject(model);//td is an Instance of TableData;
                }
                catch (Exception e) {
                }
                finally {
                }


                EditText usernameEdit = (EditText)findViewById(R.id.usernameEdit);
                EditText passwordEdit = (EditText)findViewById(R.id.passwordEdit);

                if (configsModel.isRememberUserName()) {
                    configsModel.setUserName(usernameEdit.getText().toString());
                }
                else {
                    configsModel.setUserName(null);
                }

                if (configsModel.isRememberPassword()) {
                    configsModel.setPassword(passwordEdit.getText().toString());
                }
                else {
                    configsModel.setPassword(null);
                }

                configsModel.persist(this);

                MyToast.showMessage(getApplicationContext(), "登录成功");

                showMainActivity();
                break;
            }
            case MainService.MSG_LOGIN_FAILED: {
                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                break;
        }
    }
}
