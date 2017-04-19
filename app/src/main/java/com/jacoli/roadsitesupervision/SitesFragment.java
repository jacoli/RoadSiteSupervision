package com.jacoli.roadsitesupervision;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jacoli.roadsitesupervision.SupervisionPatrol.SupervisionPatrolListActivity;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.views.MyToast;


public class SitesFragment extends Fragment {
    //final String[] models = {"施工旁站", "质量巡视", "安全巡视", "环保巡视", "质量抽检"};
    final String[] models = {"施工旁站", "质量巡视", "安全巡视", "环保巡视", "质量抽检", "监理巡查"};

    public SitesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View selfView = getActivity().getLayoutInflater().inflate(R.layout.fragment_sites, container, false);
        ListView listView = (ListView)selfView.findViewById(R.id.listView);

        BaseAdapter baseAdapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return models.length;
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
                View v = getActivity().getLayoutInflater().inflate(R.layout.list_item_base_action_button, null);
                TextView textView = (TextView)v.findViewById(R.id.textView);
                textView.setText(models[position]);
                return v;
            }
        };

        listView.setAdapter(baseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //final String[] models = {"施工旁站", "质量巡视", "安全巡视", "环保巡视", "质量抽检"};

//                if (position < 0 || position > 4) {
//                    return;
//                }
//
//                MyToast.showMessage(getActivity(), models[position]);

                Intent intent = null;
                if (position == 0) {
                    intent = new Intent(getActivity(), ProjectDetailActivity.class);
                    intent.putExtra("type", MainService.project_detail_type_pz);
                }
                else if (position == 1) {
                    intent = new Intent(getActivity(), ProjectDetailForInspectionActivity.class);
                    intent.putExtra("type", MainService.project_detail_type_quality_inspection);
                }
                else if (position == 2) {
                    intent = new Intent(getActivity(), ProjectDetailForInspectionActivity.class);
                    intent.putExtra("type", MainService.project_detail_type_safety_inspection);
                }
                else if (position == 3) {
                    intent = new Intent(getActivity(), ProjectDetailForInspectionActivity.class);
                    intent.putExtra("type", MainService.project_detail_type_environmental_inspection);
                }
                else if (position == 4) {
                    intent = new Intent(getActivity(), ProjectDetailActivity.class);
                    intent.putExtra("type", MainService.project_detail_type_quality_sampling_inspection);
                }
                else if (position == 5) {
                    intent = new Intent(getActivity(), SupervisionPatrolListActivity.class);
                }

                if (intent != null) {
                    intent.putExtra("title", models[position]);
                    startActivity(intent);
                }
            }
        });

        return selfView;
    }
}
