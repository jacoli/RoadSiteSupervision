package com.jacoli.roadsitesupervision.SupervisionPatrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.AssignedMatterDetailActivity;
import com.jacoli.roadsitesupervision.AssignedMatterReplyActivity;
import com.jacoli.roadsitesupervision.CommonActivity;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.EasyRequest.StringNullAdapter;
import com.jacoli.roadsitesupervision.PhotoAdapter;
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.RecyclerItemClickListener;
import com.jacoli.roadsitesupervision.services.AssignedMatterDetailModel;
import com.jacoli.roadsitesupervision.services.ImageUrlModel;
import com.jacoli.roadsitesupervision.services.MainService;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPreview;

public class SupervisionPatrolNormalProcessActivity extends CommonActivity {

    public String matterId;
    private AssignedMatterDetailModel model;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_patrol_normal_process);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle(SupervisionPatrolUtils.title);

        Intent intent = getIntent();
        matterId = intent.getStringExtra("id");
        if (!MainService.getInstance().sendQueryAssignedMatterDetail(matterId, handler)) {
            Toast.makeText(getBaseContext(), "获取交办事项详情失败", Toast.LENGTH_SHORT).show();
        }

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
                finishAssignedMatter();
            }
        });

        ListView listView = (ListView) findViewById(R.id.listView);
        setupListView(listView);
    }

    private void setupListView(ListView listView) {
        adapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return model == null ? 0 : (1 + model.getReply().size());
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
                    String text = "上报人：\n时间：\n工程构件：\n检查大项：\n检查细目：\n\n审批人：\n处理意见：\n时间：";
                    textView.setText(text);

                    final ArrayList<String> imageUrls = new ArrayList<>();
                    for (ImageUrlModel imageUrlModel : model.getPhotoList()) {
                        imageUrls.add(imageUrlModel.getWebPath());
                    }

                    setupPhotoPicker(recyclerView, imageUrls);
                } else {
                    String text = "回复人：\n时间：\n内容：";
                    textView.setText(text);

                    final ArrayList<String> imageUrls = new ArrayList<>();
                    for (ImageUrlModel imageUrlModel : model.getPhotoList()) {
                        imageUrls.add(imageUrlModel.getWebPath());
                    }

                    setupPhotoPicker(recyclerView, imageUrls);
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

    private void setupPhotoPicker(RecyclerView recyclerView, final ArrayList<String> imgUrls) {
        if (recyclerView != null && imgUrls != null) {
            PhotoAdapter photoAdapter = new PhotoAdapter(SupervisionPatrolNormalProcessActivity.this, imgUrls, false);

            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, OrientationHelper.VERTICAL));
            recyclerView.setAdapter(photoAdapter);

            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(SupervisionPatrolNormalProcessActivity.this,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            PhotoPreview.builder()
                                    .setPhotos(imgUrls)
                                    .setCurrentItem(position)
                                    .start(SupervisionPatrolNormalProcessActivity.this);
                        }
                    }));
        }
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_QUERY_ASSIGNED_MATTER_DETAIL_SUCCESS:
                model = (AssignedMatterDetailModel) obj;
                adapter.notifyDataSetChanged();

                if (adapter.getCount() > 1) {
                    ListView listView = (ListView) findViewById(R.id.listView);
                    listView.smoothScrollToPosition(adapter.getCount() - 1);
                }

                break;
            case MainService.MSG_QUERY_ASSIGNED_MATTER_DETAIL_FAILED:
                Toast.makeText(getBaseContext(), "获取交办事项详情失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SupervisionPatrolNormalProcessReplyActivity.RequestCode) {
            if (resultCode == RESULT_OK) {
                if (!MainService.getInstance().sendQueryAssignedMatterDetail(matterId, handler)) {
                    Toast.makeText(getBaseContext(), "获取交办事项详情失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    void finishAssignedMatter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SupervisionPatrolNormalProcessActivity.this);
        builder.setTitle("提示");
        builder.setMessage("是否归档交办事项？");

        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                MainService.getInstance().sendFinishAssignedMatter(matterId, new Callbacks() {
                    @Override
                    public void onSuccess(ResponseBase responseModel) {
                        Toast.makeText(getBaseContext(), " 归档交办事项成功", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFailed(String error) {
                        Toast.makeText(getBaseContext(), " 归档交办事项失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });

        builder.create().show();
    }
}
