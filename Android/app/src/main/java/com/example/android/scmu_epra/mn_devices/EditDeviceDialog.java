package com.example.android.scmu_epra.mn_devices;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.MainActivity;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetAreasJsonData;
import com.example.android.scmu_epra.connection.PostJsonData;
import com.example.android.scmu_epra.mn_home.HomeListAdapter;
import com.example.android.scmu_epra.mn_users.AreaItem;
import com.example.android.scmu_epra.mn_users.UserItem;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditDeviceDialog extends DialogFragment implements GetAreasJsonData.OnDataAvailable {

    TextInputLayout deviceNameLayout;
    TextInputEditText inputDeviceName;
    Spinner deviceType;
    Spinner deviceArea;
    SwitchCompat deviceStatus;

    DeviceItem device;

    public EditDeviceDialog() {
    }

    public static EditDeviceDialog newInstance(DeviceItem device) {
        EditDeviceDialog frag = new EditDeviceDialog();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String jsonDevice = gson.toJson(device);
        args.putString(Constants.Bundle.DEVICE, jsonDevice);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.new_device_layout, null);

        deviceNameLayout = v.findViewById(R.id.device_name_layout);
        inputDeviceName = v.findViewById(R.id.input_device_name);
        deviceType = v.findViewById(R.id.device_type);
        deviceArea = v.findViewById(R.id.device_area);
        deviceStatus = v.findViewById(R.id.device_status);

        getAreas();

        String[] items = new String[]{"Sensor", "Actuator", "Simulator"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        deviceType.setAdapter(adapter);

        Bundle args = getArguments();
        String stringDevice = null;
        if (args != null) {
            stringDevice = args.getString(Constants.Bundle.DEVICE);
            if (stringDevice != null) {
                Gson gson = new Gson();
                device = gson.fromJson(stringDevice, DeviceItem.class);
                inputDeviceName.setText(device.getName());
                inputDeviceName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                inputDeviceName.setFocusable(false);
                deviceNameLayout.setEnabled(false);
                if (device.getType() == DeviceItem.DeviceType.Sensor) {
                    deviceType.setSelection(0);
                } else if (device.getType() == DeviceItem.DeviceType.Actuator) {
                    deviceType.setSelection(1);
                } else if (device.getType() == DeviceItem.DeviceType.Simulator) {
                    deviceType.setSelection(2);
                }
                deviceStatus.setChecked(device.isOn());
            }
        }
        final boolean isNewDevice = stringDevice == null;

        deviceArea.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Loading..."}));

        b.setView(v);

        b.setTitle(isNewDevice ? "New Device" : "Edit Device");
        b.setPositiveButton("Save", null);
        b.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss());

        Dialog dialog = b.create();
        dialog.setOnShowListener((dialogInterface) -> {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener((view) -> {

                        String name = inputDeviceName.getText().toString();
                        if (TextUtils.isEmpty(name)) {
                            deviceNameLayout.setError("Name is required.");

                        }
                        else if (deviceArea.getSelectedItem() instanceof AreaItem) {
                            PostJsonData postJsonData;
                            if (isNewDevice) {
                                postJsonData = new PostJsonData((MainActivity) getActivity(),
                                        "https://test966996.000webhostapp.com/api/post_devices.php", Constants.Status.ADD_NEW_DEVICE);

                            } else {
                                postJsonData = new PostJsonData((MainActivity)getActivity(),
                                        "https://test966996.000webhostapp.com/api/update_devices.php", Constants.Status.UPDATE_DEVICE);
                            }
                            postJsonData.execute("nome=" + name,
                                    "tipo=" + deviceType.getSelectedItem().toString(),
                                    "ligado=" + (deviceStatus.isChecked() ? "1" : "0"),
                                    "areaId=" + ((AreaItem)deviceArea.getSelectedItem()).getId());
                            dialog.dismiss();
                        }
                    });
        });

        return dialog;
    }

    private void getAreas() {
        GetAreasJsonData getAreasJsonData = new GetAreasJsonData(this, "https://test966996.000webhostapp.com/api/get_areas.php");
        getAreasJsonData.execute();
    }

    @Override
    public void onDataAvailable(List<AreaItem> data, DownloadStatus status) {
        if (status == DownloadStatus.OK && data != null && data.size() > 0) {
            ArrayAdapter<AreaItem> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, data);
            deviceArea.setAdapter(adapter);

            if (device != null) {
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getId() == device.getAreaId()) {
                        deviceArea.setSelection(i);
                        break;
                    }
                }
            }
        }
    }

}
