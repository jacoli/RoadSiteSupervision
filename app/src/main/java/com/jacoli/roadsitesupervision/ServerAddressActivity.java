package com.jacoli.roadsitesupervision;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.services.*;
import com.jacoli.roadsitesupervision.views.MyToast;

public class ServerAddressActivity extends CommonActivity {
    private BGConfigsModel configsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_address);

        createTitleBar();
        titleBar.setTitle("修改服务器地址");

        final Button modifyBtn = (Button)findViewById(R.id.modifyBtn);
        final EditText oldServerAddress = (EditText)findViewById(R.id.oldServerAddress);
        final EditText newServerAddress = (EditText)findViewById(R.id.newServerAddress);

        configsModel = BGConfigsModel.fetch(this);

        oldServerAddress.setText(configsModel.getServerAddress());
        newServerAddress.setText(configsModel.getServerPort());

        final ServerAddressActivity activity = this;

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newAddress = oldServerAddress.getText().toString();
                if (newAddress.length() == 0) {
                    MyToast.showMessage(getApplicationContext(), "服务器地址不能为空");
                    return;
                }

                String port = newServerAddress.getText().toString();
                if (port == null || port.length() == 0) {
                    MyToast.showMessage(getApplicationContext(), "服务器端口不能为空");
                    return;
                }

                if (!MainService.getInstance().setServerAddress(newAddress, port)) {
                    MyToast.showMessage(getApplicationContext(), "服务器地址格式不对");
                    return;
                }

                configsModel.setServerPort(port);
                configsModel.setServerAddress(newAddress);
                configsModel.persist(activity);
                MyToast.showMessage(getApplicationContext(), "服务器地址修改成功");
            }
        });
    }
}
