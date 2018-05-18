package com.example.android.scmu_epra.home;

import android.graphics.Rect;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.android.scmu_epra.history.History;
import com.example.android.scmu_epra.R;

import java.util.ArrayList;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

public class Home extends Fragment {

    private Button btnShowHistory;
    private ToggleSwitch toggleOnOff;
    private boolean alarmIsOn;
    private LinearLayout bottomListView;
    private CoordinatorLayout baseLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alarmIsOn = false;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");


        btnShowHistory = getView().findViewById(R.id.show_history);
        btnShowHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = new History();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.screen_area, f);
                ft.commit();
            }
        });


        toggleOnOff = getView().findViewById(R.id.toggleOnOff);
        toggleOnOff.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener(){

            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if (position == 0) {
                    //TODO: turn alarm off
                }
                else {
                    //TODO: turn alarm on
                }
            }
        });

        bottomListView = getView().findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomListView);
        baseLayout = getView().findViewById(R.id.baseLayout);


        Rect rect = new Rect();
        getView().getWindowVisibleDisplayFrame(rect);
        bottomSheetBehavior.setPeekHeight((rect.bottom - rect.top) / 2);


        ArrayList<Item> list = new ArrayList<Item>();

        Item a = new Item("Alberto");
        Item b = new Item("Maria");
        Item c = new Item("Xavier");
        Item d = new Item("Teresa");
        Item e = new Item("Marco");
        Item f = new Item("Coiso");
        Item g = new Item("Filipe");
        Item h = new Item("Matumbo");
        Item i = new Item("Pessoa");
        Item j = new Item("Paco");

        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);
        list.add(f);
        list.add(g);
        list.add(i);
        list.add(h);


        ListAdapter listAdapter = new ListAdapter(getContext(), 0, list);
        BottomSheetListView listView = (BottomSheetListView) getView().findViewById(R.id.list_view);
        listView.setAdapter(listAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO: Define item click action here

                Switch sw = view.findViewById(R.id.switch1);
                if (sw.isChecked()) {
                    sw.setChecked(false);
                }
                else {
                    sw.setChecked(true);
                }

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return getLayoutInflater().inflate(R.layout.home, container, false);
    }




    private void switchAlarmState() {
        if (alarmIsOn) {
            // Turn off alarm

            alarmIsOn = false;

        }                            
        else {
            // Turn on alarm

            alarmIsOn = true;

        }
    }
}
