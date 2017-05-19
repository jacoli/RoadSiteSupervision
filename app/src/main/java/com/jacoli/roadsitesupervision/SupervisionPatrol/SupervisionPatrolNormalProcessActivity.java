package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.BuildConfig;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.ProjectConfigs.Configs;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.services.ImageUrlModel;

import java.util.ArrayList;

public class SupervisionPatrolNormalProcessActivity extends CommonActivity {

    private String modelId;
    private DetailModel model;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_patrol_normal_process);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle(BuildConfig.SupervisionPatrolTitle);

        Intent intent = getIntent();
        modelId = intent.getStringExtra("id");

        Button submitBtn = (Button) findViewById(R.id.submit_btn_1);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupervisionPatrolNormalProcessActivity.this, SupervisionPatrolNormalProcessReplyActivity.class);
                intent.putExtra("id", getIntent().getStringExtra("id"));
                startActivityForResult(intent, SupervisionPatrolNormalProcessReplyActivity.RequestCode);
            }
        });

        Button submit2Btn = (Button) findViewById(R.id.submit_btn_2);
        submit2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertToDocument();
            }
        });

        ListView listView = (ListView) findViewById(R.id.listView);
        setupListView(listView);

        loadData();
    }

    private void loadData() {
        SupervisionPatrolService.getInstance().sendQueryDetail(modelId, new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                model = (DetailModel) responseModel;
                adapter.notifyDataSetChanged();

                if (adapter.getCount() > 1) {
                    ListView listView = (ListView) findViewById(R.id.listView);
                    listView.smoothScrollToPosition(adapter.getCount() - 1);
                }
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListView(ListView listView) {
        adapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return model == null ? 0 : (2 + model.getReply().size());
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = getLayoutInflater().inflate(R.layout.list_item_supervision_patrol_normal_process, null);
                TextView textView = (TextView) v.findViewById(R.id.text_view);
                RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

                if (position == 0) {
                    String text = "工程构件：" + model.getProjectPart() + "\n"
                            + "检查大项：" + model.getCheckTypeDescription() + "\n"
                            + "检查细目：" + model.getCheckItemsDescription()
                            + "上报人：" + model.getAddByName() + "\n"
                            + "上报时间：" + model.getAddTime() + "\n"
                            + "补充说明：" + model.getDescription() + "\n";
                    textView.setText(text);

                    final ArrayList<String> imageUrls = new ArrayList<>();
                    for (ImageUrlModel imageUrlModel : model.getPhotoList()) {
                        imageUrls.add(imageUrlModel.getWebPath());
                    }

                    setupPhotoViewer(recyclerView, imageUrls);
                } else if (position == 1) {
                    String text = "审批人：" + model.getApprovalByName() + "\n"
                            + "处理意见：" + model.getApprovalComment() + "\n"
                            + "审批时间：" + model.getApprovalTime() + "\n"
                            + "承办人：" + model.getReceiverName() + "\n";
                    textView.setText(text);
                } else {
                    DetailModel.Reply reply = model.getReply().get(position - 2);

                    String text = "回复人：" + reply.getReplyName() + "\n"
                            + "时间：" + reply.getAddTime() + "\n"
                            + "内容：" + reply.getReplyContent() + "\n";
                    textView.setText(text);

                    final ArrayList<String> imageUrls = new ArrayList<>();
                    for (ImageUrlModel imageUrlModel : reply.getPhotoList()) {
                        imageUrls.add(imageUrlModel.getWebPath());
                    }

                    setupPhotoViewer(recyclerView, imageUrls);
                }

                return v;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        listView.setFooterDividersEnabled(true);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SupervisionPatrolNormalProcessReplyActivity.RequestCode) {
            if (resultCode == RESULT_OK) {
                loadData();
            }
        }
    }

    private void alertToDocument() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SupervisionPatrolNormalProcessActivity.this);
        builder.setTitle("提示");
        builder.setMessage("是否归档监理巡查？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                document();
            }
        });
        builder.setNegativeButton("否", null);
        builder.create().show();
    }

    private void document() {
        SupervisionPatrolService.getInstance().sendFinishSupervisionPatrol(modelId, new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                Toast.makeText(getBaseContext(), " 归档监理巡查成功", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
