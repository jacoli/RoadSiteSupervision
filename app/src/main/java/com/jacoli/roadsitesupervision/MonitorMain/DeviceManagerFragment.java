package com.jacoli.roadsitesupervision.MonitorMain;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.jacoli.roadsitesupervision.DataMonitor.MonitorSensorTypesActivity;
import com.jacoli.roadsitesupervision.R;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceManagerFragment extends Fragment {

    @BindView(R.id.grid_view)
    GridView gridView;

    public DeviceManagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_manager, container, false);
        ButterKnife.bind(this, view);

        final List<Integer> items = new ArrayList<>();
        items.add(R.layout.grid_item_sensors);

        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return items.size();
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
                int layoutId = items.get(position);
                return LayoutInflater.from(getActivity()).inflate(layoutId, null);
            }
        };

        gridView.setAdapter(baseAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(getActivity(), MonitorSensorTypesActivity.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }
}
