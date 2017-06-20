package com.jacoli.roadsitesupervision.DataMonitor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.services.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonitorAddSensorDataActivity extends CommonActivity {

    @BindView(R.id.code)
    TextView code;

    @BindView(R.id.edit_text1)
    EditText editText1;

    @BindView(R.id.edit_text2)
    EditText editText2;

    @BindView(R.id.edit_text3)
    EditText editText3;

    @BindView(R.id.submit_btn)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_add_sensor_data);

        ButterKnife.bind(this);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("添加传感器数据");

        code.setText(getIntent().getStringExtra("code"));
        if (getIntent().getStringExtra("type_name").equalsIgnoreCase("变形")) {
            editText2.setHint("必填");
            editText3.setHint("必填");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        if (Utils.isStringEmpty(editText1.getText().toString())) {
            showToast("数值1不能为空");
            return;
        }

        if (getIntent().getStringExtra("type_name").equalsIgnoreCase("变形")) {
            if (Utils.isStringEmpty(editText2.getText().toString())) {
                showToast("数值2不能为空");
                return;
            }

            if (Utils.isStringEmpty(editText3.getText().toString())) {
                showToast("数值3不能为空");
                return;
            }
        }

        DataMonitorService.getInstance().AddSensorData(getIntent().getStringExtra("id"),
                getIntent().getStringExtra("code"),
                editText1.getText().toString(),
                editText2.getText().toString(),
                editText3.getText().toString(), new Callbacks() {
                    @Override
                    public void onSuccess(ResponseBase responseModel) {
                        showToast("添加数据成功");
                        finish();
                    }

                    @Override
                    public void onFailed(String error) {
                        showToast(error);
                    }
                });
    }
}
