package com.example.android.scmu_epra.mn_home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.MainActivity;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetAreasJsonData;
import com.example.android.scmu_epra.connection.GetDevicesJsonData;
import com.example.android.scmu_epra.connection.PostJsonData;
import com.example.android.scmu_epra.mn_devices.DeviceItem;
import com.example.android.scmu_epra.mn_devices.EditDeviceDialog;
import com.example.android.scmu_epra.mn_users.AreaItem;
import com.example.android.scmu_epra.mn_users.EditUserPermissionsDialog;
import com.example.android.scmu_epra.mn_users.UserItem;
import com.google.gson.Gson;

import java.util.List;

public class EditAreaDialog extends DialogFragment implements GetDevicesJsonData.OnDataAvailable {


    private ProgressBar mSpinner;
    private ListView mDevicesList;
    private TextView mEmptyView;
    private AreaItem mArea;

    public EditAreaDialog() {
    }

    public static EditAreaDialog newInstance(AreaItem area) {
        EditAreaDialog frag = new EditAreaDialog();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String jsonArea = gson.toJson(area);
        args.putString(Constants.Bundle.AREA, jsonArea);
        frag.setArguments(args);
        return frag;
    }

    private void getData() {
        mSpinner.setVisibility(View.VISIBLE);
        GetDevicesJsonData getJsonData = new GetDevicesJsonData(this,
                "https://test966996.000webhostapp.com/api/get_devices.php");
        getJsonData.execute("areaId=" + mArea.getId());
    }

    private void saveData() {
//        PostJsonData postJsonData = new PostJsonData((MainActivity) getActivity(),
//                "https://test966996.000webhostapp.com/api/post_user.php", Constants.Status.EDIT_USER_PERMISSIONS_DIALOG);
//        postJsonData.execute("privilegios=" + TextUtils.join(",", mUser.getPermissions()),
//                "email=" + mUser.getEmail());
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        super.onResume();
        getData();
    }

    @Override
    public void onDataAvailable(List<DeviceItem> data, DownloadStatus status) {
        Log.d("data_size", String.valueOf(data.size()));
        if (status == DownloadStatus.OK) {

            ArrayAdapter<DeviceItem> arrayAdapter = new ArrayAdapter<DeviceItem>(getContext(),
                    android.R.layout.select_dialog_multichoice,
                    data) {
                @Override @NonNull
                public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                    View v = LayoutInflater.from(getContext()).inflate(R.layout.edit_area_list_item, null);

                    DeviceItem item = data.get(position);

                    TextView deviceName = v.findViewById(R.id.device_name);
                    deviceName.setText(item.getName());
                    Switch deviceSwitch = v.findViewById(R.id.device_switch);
                    deviceSwitch.setChecked(item.isOn());

                    return v;
                }
            };
            mDevicesList.setAdapter(arrayAdapter);

        } else {
            // download or processing failed
        }
        mSpinner.setVisibility(View.INVISIBLE);
        mEmptyView.setVisibility(data.size() == 0 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.edit_devices_layout, null);

        mSpinner = v.findViewById(R.id.progress_bar);
        mDevicesList = v.findViewById(R.id.devices);
        mEmptyView = v.findViewById(android.R.id.empty);
        String stringArea = getArguments().getString(Constants.Bundle.AREA);
        Gson gson = new Gson();
        mArea = gson.fromJson(stringArea, AreaItem.class);
        // Here's the LayoutParams setup
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ViewGroup.LayoutParams layoutParams = mDevicesList.getLayoutParams();
        layoutParams.width = SwipeRefreshLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = (int) (displayMetrics.heightPixels*0.5f);
        v.setLayoutParams(layoutParams);

        b.setView(v);

        b.setTitle("Devices");
        b.setPositiveButton("Save", (dialog, whichButton) -> saveData());
        b.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss());

        return b.create();
    }


}
