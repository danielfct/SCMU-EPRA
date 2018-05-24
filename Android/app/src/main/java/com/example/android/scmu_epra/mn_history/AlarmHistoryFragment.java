package com.example.android.scmu_epra.mn_history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.scmu_epra.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmHistoryFragment extends Fragment {

    @BindView(R.id.alarm_history)
    ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_history_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Alarm History");

        List<AlarmHistoryItem> list = Arrays.asList(
                new AlarmHistoryItem(AlarmHistoryItem.AlarmHistoryType.AlarmTrigger,"Alarm triggered", "22/04 at 14h27m"),
                new AlarmHistoryItem(AlarmHistoryItem.AlarmHistoryType.AlarmOnOff,"WZ activated the alarm", "22/04 at 9h17m"),
                new AlarmHistoryItem(AlarmHistoryItem.AlarmHistoryType.AlarmOnOff,"WZ deactivated the alarm", "21/04 at 17h03m"),
                new AlarmHistoryItem(AlarmHistoryItem.AlarmHistoryType.AlarmOnOff,"XYZ activated the alarm", "21/04 at 10h31m"));
        AlarmHistoryListAdapter listAdapter = new AlarmHistoryListAdapter(getContext(), 0, list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener((adapterView, v, position, id) -> {
            //TODO: Define item click action here
        });
    }

}