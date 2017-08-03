package com.jacoli.roadsitesupervision;

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

import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.services.AssignedMatterDetailModel;
import com.jacoli.roadsitesupervision.services.ImageUrlModel;
import com.jacoli.roadsitesupervision.services.MainService;
import java.util.ArrayList;
import me.iwf.photopicker.PhotoPreview;

public class AssignedMatterDetailActivity extends CommonActivity {

    public String matterId;
    private AssignedMatterDetailModel model;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_matter_detail);

        createTitleBar();

        titleBar.setTitle("交办事项详情");

        Intent intent = getIntent();
        matterId = intent.getStringExtra("id");
        if (!MainService.getInstance().sendQueryAssignedMatterDetail(matterId, handler)) {
            Toast.makeText(getBaseContext(), "获取交办事项详情失败", Toast.LENGTH_SHORT).show();
        }

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssignedMatterDetailActivity.this, AssignedMatterReplyActivity.class);
                intent.putExtra("id", getIntent().getStringExtra("id"));
                startActivityForResult(intent, AssignedMatterReplyActivity.RequestCode);
            }
        });

        Button submit2Btn = (Button) findViewById(R.id.submit2_btn);
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
                View v = getLayoutInflater().inflate(R.layout.list_item_assigned_mater_detail, null);
                TextView senderTextView = (TextView)v.findViewById(R.id.text_view_sender);
                TextView contentTextView = (TextView)v.findViewById(R.id.text_view_content);
                TextView timeTextView = (TextView)v.findViewById(R.id.text_view_time);

                final ArrayList<String> imageUrls = new ArrayList<>();

                if (position == 0) {
                    senderTextView.setText(model.getSenderName());
                    contentTextView.setText(model.getAssignContent());
                    timeTextView.setText(model.getAddTime());

                    for (ImageUrlModel imageUrlModel : model.getPhotoList()) {
                        imageUrls.add(imageUrlModel.getWebPath());
                    }
                }
                else {
                    AssignedMatterDetailModel.Reply reply = model.getReply().get(position - 1);
                    senderTextView.setText(reply.getReplyName());
                    contentTextView.setText(reply.getAssignContent());
                    timeTextView.setText(reply.getAddTime());

                    for (ImageUrlModel imageUrlModel : reply.getPhotoList()) {
                        imageUrls.add(imageUrlModel.getWebPath());
                    }
                }

                RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
                setupPhotoPicker(recyclerView, imageUrls);

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
            PhotoAdapter photoAdapter = new PhotoAdapter(AssignedMatterDetailActivity.this, imgUrls, false);

            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, OrientationHelper.VERTICAL));
            recyclerView.setAdapter(photoAdapter);

            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(AssignedMatterDetailActivity.this,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            PhotoPreview.builder()
                                    .setPhotos(imgUrls)
                                    .setCurrentItem(position)
                                    .start(AssignedMatterDetailActivity.this);
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

                TextView textView = (TextView) findViewById(R.id.text_view_subject);
                String text = model.getSubject();
                textView.setText(text);
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

        if (requestCode == AssignedMatterReplyActivity.RequestCode) {
            if (resultCode == RESULT_OK) {
                if (!MainService.getInstance().sendQueryAssignedMatterDetail(matterId, handler)) {
                    Toast.makeText(getBaseContext(), "获取交办事项详情失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    void finishAssignedMatter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AssignedMatterDetailActivity.this);
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
