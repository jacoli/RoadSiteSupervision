package com.jacoli.roadsitesupervision.DataMonitor;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.jacoli.roadsitesupervision.EasyRequest.StringNullAdapter;
import com.jacoli.roadsitesupervision.R;

import java.util.ArrayList;

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

    public DataMonitorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_monitor, container, false);
        ButterKnife.bind(this, view);

        final ArrayList<String> models = new ArrayList<>();
        models.add("1");
        models.add("2");
        models.add("3");

        for (int idx = 0; idx < 200; ++idx) {
            models.add(Integer.toString(idx));
        }

        projectNameTextView.setText("测试项目");

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.grid_view_item, R.id.text, models);

        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return models.size();
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
                TextView title=null;
                if(convertView==null){
                    convertView=LayoutInflater.from(getActivity()).inflate(R.layout.grid_view_item, null);
                    title=(TextView) convertView.findViewById(R.id.text);
                } else {
                    title=(TextView) convertView.findViewById(R.id.text);
                }
                title.setText(models.get(position));

                convertView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.material_accentPink_a200));

                return convertView;
            }
        };

        gridView.setAdapter(baseAdapter);

        return view;
    }
}
