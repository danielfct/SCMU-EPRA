package com.example.android.scmu_epra.mn_burglaryManag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.android.scmu_epra.BottomSheetListView;
import com.example.android.scmu_epra.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BurglaryManagementFragment extends Fragment {

    @BindView(R.id.bottom_sheet)
    LinearLayout bottomListView;
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

        List<HistoryItem> list = Arrays.asList(
                new HistoryItem("Entrada", "1 minute"),
                new HistoryItem("Cozinha", "5 minutes"),
                new HistoryItem("Quarto 1", "3 minutes"),
                new HistoryItem("Quarto 2", "10 minutes"),
                new HistoryItem("Casa de Banho 1", "1 minute"),
                new HistoryItem("Casa de Banho 2", "2 minutes"),
                new HistoryItem("Sala", "20 minutes"));
        HistoryListAdapter listAdapter = new HistoryListAdapter(getContext(), 0, list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener((adapterView, v, position, id) -> {
            //TODO: Define item click action here
        });
    }

}
