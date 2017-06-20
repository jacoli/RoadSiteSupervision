package com.jacoli.roadsitesupervision.DataMonitor;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.jacoli.roadsitesupervision.EasyRequest.Callbacks;
import com.jacoli.roadsitesupervision.EasyRequest.ResponseBase;
import com.jacoli.roadsitesupervision.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataMonitorFragment extends Fragment {

    @BindView(R.id.text_view_project_name)
    TextView projectNameTextView;

    @BindView(R.id.grid_view)
    GridView gridView;

    @BindView(R.id.id_swipe_ly)
    SwipeRefreshLayout swipeRefreshLayout;

    private BaseAdapter baseAdapter;

    private ProjectDetailModel detailModel;

    public DataMonitorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_monitor, container, false);
        ButterKnife.bind(this, view);

        projectNameTextView.setText("桥梁名称：");

        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return detailModel != null ? detailModel.getItems().size() : 0;
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
                final ProjectDetailModel.UnitProjectModel unitProjectModel = detailModel.getItems().get(position);

                TextView title;
                if(convertView==null){
                    convertView=LayoutInflater.from(getActivity()).inflate(R.layout.grid_view_item, null);
                    title=(TextView) convertView.findViewById(R.id.text);
                } else {
                    title=(TextView) convertView.findViewById(R.id.text);
                }
                title.setText(unitProjectModel.getName());

                if (unitProjectModel.getProgress() == 0) {
                    convertView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.material_grey_300));
                } else if (unitProjectModel.getProgress() == 1) {
                    convertView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.material_green_300));
                } else {
                    convertView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.material_blue_300));
                }

                if (unitProjectModel.isAlarm()) {
                    convertView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.material_red_300));
                }

                return convertView;
            }
        };

        gridView.setAdapter(baseAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ProjectDetailModel.UnitProjectModel unitProjectModel = detailModel.getItems().get(position);
                if (unitProjectModel.getProgress() == 0) {
                    alertToActiveUnitProject(unitProjectModel);
                } else {
                    showUnitProjectDetailActivity(unitProjectModel);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        return view;
    }

    private void loadData() {
        DataMonitorService.getInstance().sendProjectDetailQuery(new Callbacks() {
            @Override
            public void onSuccess(ResponseBase responseModel) {
                detailModel = (ProjectDetailModel) responseModel;
                baseAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        loadData();
    }

    private void alertToActiveUnitProject(final ProjectDetailModel.UnitProjectModel unitProjectModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage(unitProjectModel.getName() + "\n是否开始施工？");

        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                DataMonitorService.getInstance().sendActiveUnitProject(unitProjectModel.getID(), new Callbacks() {
                    @Override
                    public void onSuccess(ResponseBase responseModel) {
                        ActiveUnitProjectResp activeUnitProjectResp = (ActiveUnitProjectResp)responseModel;
                        detailModel.setUnitProjectProgress(unitProjectModel.getID(), activeUnitProjectResp.getProgress());
                        baseAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(String error) {
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("否", null);
        builder.create().show();
    }

    private void showUnitProjectDetailActivity(ProjectDetailModel.UnitProjectModel unitProjectModel) {
        Intent intent = new Intent(getActivity() ,MonitorPointListActivity.class);
        intent.putExtra("id", unitProjectModel.getID());
        intent.putExtra("title", unitProjectModel.getName());
        intent.putExtra("isAlarm", unitProjectModel.isAlarm());
        startActivity(intent);
    }
}
