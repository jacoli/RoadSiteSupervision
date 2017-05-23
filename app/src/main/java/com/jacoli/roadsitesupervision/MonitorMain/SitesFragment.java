package com.jacoli.roadsitesupervision.MonitorMain;


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
import com.jacoli.roadsitesupervision.R;
import com.jacoli.roadsitesupervision.SupervisionPatrol.SupervisionPatrolListActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SitesFragment extends Fragment {

    final String[] models = {"安全巡查"};

    public SitesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View selfView = getActivity().getLayoutInflater().inflate(R.layout.fragment_monitor_sites, container, false);
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

                Intent intent = null;
                if (position == 0) {
                    intent = new Intent(getActivity(), SupervisionPatrolListActivity.class);
                }

                if (intent != null) {
                    startActivity(intent);
                }
            }
        });

        return selfView;
    }

}
