package com.example.android.scmu_epra.mn_home;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.android.scmu_epra.BottomSheetListView;
import com.example.android.scmu_epra.history.History;
import com.example.android.scmu_epra.R;

import java.util.Arrays;
import java.util.List;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends Fragment {

    @BindView(R.id.show_history)
    Button btnShowHistory;
    @BindView(R.id.toggleOnOff)
    ToggleSwitch toggleOnOff;
    @BindView(R.id.bottom_sheet)
    LinearLayout bottomListView;
    @BindView(R.id.baseLayout)
    CoordinatorLayout baseLayout;
    boolean alarmIsOn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmIsOn = false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
        btnShowHistory.setOnClickListener(v -> {
                Fragment f = new History();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.screen_area, f);
                ft.commit();
        });
        toggleOnOff.setOnToggleSwitchChangeListener((position, isChecked) -> {
                if (position == 0) {
                    //TODO: turn alarm off
                } else {
                    //TODO: turn alarm on
                }
        });
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomListView);
        Rect rect = new Rect();
        getView().getWindowVisibleDisplayFrame(rect);
        bottomSheetBehavior.setPeekHeight((rect.bottom - rect.top) / 2);
        List<HomeItem> list = Arrays.asList(
                new HomeItem("Alberto"),
                new HomeItem("Maria"),
                new HomeItem("Xavier"),
                new HomeItem("Teresa"),
                new HomeItem("Marco"),
                new HomeItem("Coiso"),
                new HomeItem("Filipe"),
                new HomeItem("Matumbo"),
                new HomeItem("Pessoa"),
                new HomeItem("Paco"));
        HomeListAdapter listAdapter = new HomeListAdapter(getContext(), 0, list);
        BottomSheetListView listView = getView().findViewById(R.id.list_view);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener((adapterView, v, position, id) -> {
            //TODO: Define item click action here
            Switch sw = v.findViewById(R.id.switch1);
            sw.setChecked(!sw.isChecked());
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void switchAlarmState() {
        alarmIsOn = !alarmIsOn;
    }

}
