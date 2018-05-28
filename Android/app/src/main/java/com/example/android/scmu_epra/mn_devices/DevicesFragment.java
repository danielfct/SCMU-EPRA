package com.example.android.scmu_epra.mn_devices;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;

import com.example.android.scmu_epra.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DevicesFragment extends Fragment {

    public static final String TAG = "DevicesFragment";

    @BindView(R.id.list_devices)
    ListView listView;

    private Switch sw;
    private DevicesListAdapter listAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        getActivity().setTitle("Devices");


        List<DevicesItem> list = Arrays.asList(
                new DevicesItem("Altifalante do alarme",DevicesItem.DevicesType.Actuator, true),
                new DevicesItem("Sensor de movimento da Sala", DevicesItem.DevicesType.Sensor, true),
                new DevicesItem("Sensor de vibração do Escritório", DevicesItem.DevicesType.Sensor, false),
                new DevicesItem("Janela da cozinha", DevicesItem.DevicesType.Simulator, false));

        listAdapter = new DevicesListAdapter(getActivity().getApplicationContext(), 0, list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO: Define item click action here

                sw = view.findViewById(R.id.device_switch);

                if (sw.isChecked()) {
                    sw.setChecked(false);
                }
                else {
                    sw.setChecked(true);
                }

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_Search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                listAdapter.getFilter().filter(s);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return getLayoutInflater().inflate(R.layout.frag_devices, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

}
