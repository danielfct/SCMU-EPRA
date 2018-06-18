package com.example.android.scmu_epra.mn_home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.MainActivity;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetDevicesJsonData;
import com.example.android.scmu_epra.connection.PostJsonData;
import com.example.android.scmu_epra.mn_devices.DeviceItem;
import com.example.android.scmu_epra.mn_users.AreaItem;
import com.google.gson.Gson;

import java.util.List;

public class NewAreaDialog extends DialogFragment {

    TextInputLayout mAreaLayout;
    TextInputEditText mInputAreaName;
    SwitchCompat mSwitch;

    public NewAreaDialog() {
    }

    private void saveData() {
        PostJsonData postJsonData = new PostJsonData((MainActivity) getActivity(),
                "https://test966996.000webhostapp.com/api/post_areas.php", Constants.Status.NEW_AREA);
        postJsonData.execute("nome=" + mInputAreaName.getText().toString(), "alarmeLigado=" + (mSwitch.isChecked() ? "1" : "0"));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.new_area_layout, null);

        mAreaLayout = v.findViewById(R.id.area_name_layout);
        mInputAreaName = v.findViewById(R.id.input_area_name);
        mSwitch = v.findViewById(R.id.area_status);

        b.setView(v);

        b.setTitle("New area");
        b.setPositiveButton("Save", null);
        b.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss());

        Dialog dialog = b.create();

        dialog.setOnShowListener((dialogInterface) -> {
            Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener((view) -> {

                String name = mInputAreaName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    mAreaLayout.setError("Area's name is required.");
                }
                else {
                    saveData();
                    dialog.dismiss();
                }
            });
        });

        return dialog;
    }


}

