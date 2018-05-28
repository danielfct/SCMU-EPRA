package com.example.android.scmu_epra.mn_devices;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.scmu_epra.R;

//TODO: para modificar. É cópia da HomeFragment
public class Devices extends Fragment {

    private TextView stateTxV;
    private boolean alarmIsOn;

    public static final String TAG = "Devices";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Devices");

        stateTxV = getView().findViewById(R.id.alarmStateTV);
        stateTxV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switchAlarmState();
                return true;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return getLayoutInflater().inflate(R.layout.devices, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alarmIsOn = false;



    }


    private void switchAlarmState() {
        if (alarmIsOn) {
            // Turn off alarm

        }                            
        else {
            // Turn on alarm

        }
    }
}
