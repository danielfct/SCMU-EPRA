package com.example.android.scmu_epra.mn_devices;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Switch;

import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.MainActivity;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetDevicesJsonData;
import com.example.android.scmu_epra.connection.PostJsonData;

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
            mListAdapter = new DevicesListAdapter(mContext, 0, data, getView());
            listView.setAdapter(mListAdapter);
            listView.setOnItemClickListener((adapterView, view, position, id) -> {
                sw = view.findViewById(R.id.device_switch);
                sw.setChecked(!sw.isChecked());
            });
            registerForContextMenu(listView);
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
        MenuItem searchItem = menu.findItem(R.id.menu_Search);
        SearchView searchView = (SearchView) searchItem.getActionView();
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

        MenuItem addItem = menu.findItem(R.id.menu_new_device);
        addItem.setOnMenuItemClickListener((item) -> showAddNewDeviceDialog());

        super.onCreateOptionsMenu(menu, inflater);
    }

    private boolean showAddNewDeviceDialog() {
        FragmentManager fm = ((Activity)mContext).getFragmentManager();
        EditDeviceDialog dialog = new EditDeviceDialog();
        dialog.show(fm, "fragment_edit_device");
        return true;
    }

    private void showEditDeviceDialog(DeviceItem device) {
        FragmentManager fm = ((Activity)mContext).getFragmentManager();
        EditDeviceDialog dialog = EditDeviceDialog.newInstance(device);
        dialog.show(fm, "fragment_edit_device");
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_list_devices, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        DeviceItem device = (DeviceItem)listView.getAdapter().getItem(info.position);
        switch(item.getItemId()) {
            case R.id.edit:
                showEditDeviceDialog(device);
                return true;
            case R.id.delete:
                showDeleteDeviceConfirmation(device);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showDeleteDeviceConfirmation(DeviceItem device) {
        new AlertDialog.Builder(mContext)
                .setIcon(R.drawable.ic_delete_forever_red)
                .setTitle(mContext.getString(R.string.delete_device_confirmation_title))
                .setMessage(mContext.getString(R.string.delete_device_confirmation, device.getName()))
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> removeDevice(device))
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void removeDevice(DeviceItem device) {
        PostJsonData postJsonData = new PostJsonData((MainActivity) mContext,
                "https://test966996.000webhostapp.com/api/delete_devices.php", Constants.Status.DELETE_DEVICE);
        postJsonData.execute("nome=" + device.getName());
    }


}
