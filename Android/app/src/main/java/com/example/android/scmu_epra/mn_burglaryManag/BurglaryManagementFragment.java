package com.example.android.scmu_epra.mn_burglaryManag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android.scmu_epra.BottomSheetListView;
import com.example.android.scmu_epra.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BurglaryManagementFragment extends Fragment {

    @BindView(R.id.bottom_sheet)
    LinearLayout bottomListView;
    @BindView(R.id.baseOfBM)
    LinearLayout baseLayout;
    @BindView(R.id.notify_police_button)
    AppCompatButton notifyPoliceButton;
    @BindView(R.id.simulate_button)
    AppCompatButton simulateButton;
    @BindView(R.id.turn_off_alarm_button)
    AppCompatButton turnOffAlarmButton;
    @BindView(R.id.ignore_button)
    AppCompatButton ignoreButton;
    @BindView(R.id.list_view)
    BottomSheetListView listView;

    private boolean bottomSheetIsSet = false;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.burglary_management_layout, container, false);
        ButterKnife.bind(this, view);

        notifyPoliceButton.setOnClickListener(v -> {});
        simulateButton.setOnClickListener(v -> {});
        turnOffAlarmButton.setOnClickListener(v -> {});
        ignoreButton.setOnClickListener(v -> {});

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Burglary Management");

        List<BurglaryHistoryItem> list = Arrays.asList(
                new BurglaryHistoryItem("Entrada", "1 minute"),
                new BurglaryHistoryItem("Cozinha", "5 minutes"),
                new BurglaryHistoryItem("Quarto 1", "3 minutes"),
                new BurglaryHistoryItem("Quarto 2", "10 minutes"),
                new BurglaryHistoryItem("Casa de Banho 1", "1 minute"),
                new BurglaryHistoryItem("Casa de Banho 2", "2 minutes"),
                new BurglaryHistoryItem("Sala", "20 minutes"));
        BurglaryHistoryListAdapter listAdapter = new BurglaryHistoryListAdapter(getContext(), 0, list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener((adapterView, v, position, id) -> {
            //TODO: Define item click action here
        });


        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomListView);

        baseLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if (!bottomSheetIsSet && Math.abs(i1-i3) > 0) {
                    bottomSheetBehavior.setPeekHeight(Math.abs(i1-i3));
                    bottomSheetIsSet = true;
                }
            }
        });

    }

}
