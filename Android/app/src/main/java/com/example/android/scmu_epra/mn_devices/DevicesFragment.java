package com.example.android.scmu_epra.mn_devices;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Switch;

import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetDevicesJsonData;
import com.example.android.scmu_epra.connection.GetHistoryJsonData;
import com.example.android.scmu_epra.connection.GetJsonData;
import com.example.android.scmu_epra.connection.GetSimulatorJsonData;
import com.example.android.scmu_epra.connection.PostJsonData;
import com.example.android.scmu_epra.connection.Row;
import com.example.android.scmu_epra.mn_history.AlarmHistoryListAdapter;
import com.example.android.scmu_epra.mn_users.AreaItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DevicesFragment extends Fragment
        implements GetDevicesJsonData.OnDataAvailable {

    public static final String TAG = "DevicesFragment";

    @BindView(R.id.list_devices)
    ListView listView;
    @BindView(R.id.devices_progress_spinner)
    ProgressBar progressSpinner;

    private Context mContext;
    private Handler mHandler;
    private Runnable mRunnable;
    private Switch sw;
    private DevicesListAdapter mListAdapter;

    private NavigationView navigationView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.frag_devices, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(mContext.getString(R.string.devices_fragment_title));

        getData();

        mHandler = new Handler();
        mRunnable = () -> getData();
        mHandler.postDelayed(mRunnable, Constants.DATA_UPDATE_FREQUENCY);
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    @Override
    public void onDataAvailable(List<DeviceItem> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");
        if (status == DownloadStatus.OK && data != null && data.size() > 0) {
            mListAdapter = new DevicesListAdapter(mContext, 0, data);
            listView.setAdapter(mListAdapter);
            listView.setOnItemClickListener((adapterView, view, position, id) ->
                    sw.setChecked(!sw.isChecked()));
        } else {
            // download or processing failed
            Log.e(TAG, "onDataAvailable failed with status " + status);
        }
        if (progressSpinner.isEnabled()) {
            progressSpinner.setVisibility(View.GONE);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_Search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mListAdapter.getFilter().filter(s);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getData() {
        GetDevicesJsonData getJsonData = new GetDevicesJsonData(this,
                "https://test966996.000webhostapp.com/api/get_devices.php");
        getJsonData.execute();
    }

    public void setNavigationView(NavigationView n) {
        if (navigationView == null) {
            navigationView = n;
        }
    }



}
