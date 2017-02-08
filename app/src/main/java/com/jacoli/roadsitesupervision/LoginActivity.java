package com.jacoli.roadsitesupervision;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginBtn = (Button) findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // validate username or password
//                String username = usernameEdit.getText().toString();
//                if (username.length() == 0) {
//                    Toast.makeText(getApplicationContext(), "帐号为空，请输入", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                String password = passwordEdit.getText().toString();
//                if (password.length() == 0) {
//                    Toast.makeText(getApplicationContext(), "密码为空，请输入", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (MainService.getInstance().login(username, password, handler)) {
//                    MyToast.showMessage(getApplicationContext(), "登录中，请稍后");
//                } else {
//                    Toast.makeText(getApplicationContext(), "登录失败，异常", Toast.LENGTH_SHORT).show();
//                }

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
