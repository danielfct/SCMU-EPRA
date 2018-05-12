package com.example.android.scmu_epra;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    private TextView stateTxV;
    private boolean alarmIsOn;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");

        stateTxV = getView().findViewById(R.id.alarmStateTV);
        stateTxV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switchAlarmState();
                return true;
            }
        });


        ArrayList<Item> list = new ArrayList<Item>();

        Item a = new Item("Alberto");
        Item c = new Item("Maria");
        Item f = new Item("Xavier");
        Item q = new Item("Teresa");
        Item e = new Item("Marco");

        list.add(a);
        list.add(c);
        list.add(f);
        list.add(q);
        list.add(e);

        ListAdapter listAdapter = new ListAdapter(getContext(), 0, list);
        ListView listView = (ListView) getView().findViewById(R.id.list_view);
        listView.setAdapter(listAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return getLayoutInflater().inflate(R.layout.home, container, false);
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
