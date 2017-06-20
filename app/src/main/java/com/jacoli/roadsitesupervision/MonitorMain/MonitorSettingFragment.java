package com.jacoli.roadsitesupervision.MonitorMain;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jacoli.roadsitesupervision.DataMonitor.MonitorSensorTypesActivity;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.services.MainService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonitorSettingFragment extends Fragment {

    @BindView(R.id.cell_sensor)
    RelativeLayout cell_sensor;

    public MonitorSettingFragment() {
        // Required empty public constructor
    }

    public View selfView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        selfView = inflater.inflate(R.layout.fragment_monitor_setting, container, false);

        ButterKnife.bind(this, selfView);

        TextView accountTextView = (TextView) selfView.findViewById(R.id.text_view_account);
        accountTextView.setText(MainService.getInstance().getLoginModel().getName());

        Button submitBtn = (Button) selfView.findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        cell_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MonitorSensorTypesActivity.class);;
                startActivity(intent);
            }
        });

        return selfView;
    }

    public void submit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("是否注销");

        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                Button submitBtn = (Button) selfView.findViewById(R.id.submit_btn);
                submitBtn.setEnabled(false);
                doLogout();
            }
        });

        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });

        builder.create().show();
    }

    public void doLogout() {
        this.getActivity().finish();
    }

}
