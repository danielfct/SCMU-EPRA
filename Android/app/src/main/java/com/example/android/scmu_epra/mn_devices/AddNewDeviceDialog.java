package com.example.android.scmu_epra.mn_devices;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import com.example.android.scmu_epra.connection.PostJsonData;
import com.example.android.scmu_epra.mn_users.UserItem;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewDeviceDialog extends DialogFragment {

    TextInputLayout deviceNameLayout;
    TextInputEditText inputDeviceName;
    Spinner deviceType;
    SwitchCompat deviceStatus;

    public AddNewDeviceDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.new_device_layout, null);

        deviceNameLayout = v.findViewById(R.id.device_name_layout);
        inputDeviceName= v.findViewById(R.id.input_device_name);
        deviceType = v.findViewById(R.id.device_type);
        deviceStatus = v.findViewById(R.id.device_status);

        String[] items = new String[]{"Sensor", "Actuator", "Simulator"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        deviceType.setAdapter(adapter);

        b.setView(v);

        b.setTitle("New Device");
        b.setPositiveButton("Save", null);
        b.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss());

        Dialog dialog = b.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener((view) -> {

                        String name = inputDeviceName.getText().toString();
                        if (name.equals("")) {
                            deviceNameLayout.setError("The name can't be empy.");
                            return;
                        }
                        else {
                            PostJsonData postJsonData = new PostJsonData((MainActivity) getActivity(),
                                    "https://test966996.000webhostapp.com/api/post_devices.php", Constants.Status.ADD_NEW_DEVICE);
                            postJsonData.execute("nome=" + name,
                                    "tipo=" + deviceType.getSelectedItem().toString(),
                                    "ligado=" + (deviceStatus.isChecked() ? "1" : "0"));
                            dialog.dismiss();
                        }
                    });
            }
        });
        return dialog;
    }

}
