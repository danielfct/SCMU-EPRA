package com.example.android.scmu_epra.mn_home;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.example.android.scmu_epra.BottomSheetListView;
import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.MainActivity;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetJsonData;
import com.example.android.scmu_epra.connection.GetSimulatorJsonData;
import com.example.android.scmu_epra.mn_history.AlarmHistoryFragment;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements GetJsonData.OnDataAvailable, GetSimulatorJsonData.OnDataAvailable {

    public static final String TAG = "Home";

    @BindView(R.id.show_history)
    Button btnShowHistory;
    @BindView(R.id.toggleOnOff)
    ToggleSwitch toggleOnOff;
    @BindView(R.id.bottom_sheet)
    LinearLayout bottomListView;
    @BindView(R.id.backgroundOfButton)
    LinearLayout baseLayout;
    @BindView(R.id.home_progress_spinner)
    ProgressBar progressSpinner;
    @BindView(R.id.list_view)
    BottomSheetListView listView;


    private boolean alarmIsOn;
    private boolean bottomSheetIsSet = false;
    private Switch sw;
    private Context mContext;
    private Handler mHandler;
    private Runnable mRunnable;

    private NavigationView navigationView;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alarmIsOn = false;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        getActivity().setTitle(TAG);

        Log.d(TAG, "onViewCreated: sw = " + (sw != null));


        getData();

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                getData();
            }
        };
        mHandler.postDelayed(mRunnable, Constants.DATA_UPDATE_FREQUENCY);

        btnShowHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(1).setChecked(true);

                Fragment f = new AlarmHistoryFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.screen_area, f);
                ft.commit();
            }
        });

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

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomListView);

        Rect baseRect = new Rect();
        baseLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if (!bottomSheetIsSet && Math.abs(i1-i3) > 0) {
                    bottomSheetBehavior.setPeekHeight(Math.abs(i1-i3));
                    bottomSheetIsSet = true;
                }
            }
        });
        int rH = baseRect.height();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return getLayoutInflater().inflate(R.layout.frag_home, container, false);
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    public void setNavigationView(NavigationView n) {
        if (navigationView == null) {
            navigationView = n;
        }
    }

    @Override
    public void onDataAvailable(JSONObject data) {
        Log.d(TAG, "onDataAvailable HomeFragment: starts");
        if (data != null) {
            Log.d(TAG, "onDataAvailable: data is not null");
            try {
                String state = data.getString("estadoAtual");
                Log.d(TAG, "onDataAvailable: state = " + state);
                if (state != null && toggleOnOff != null) {
                    Log.d(TAG, "onDataAvailable: state = " + state + " sw is not null");
                    int stateInt = Integer.parseInt(state);
                    Log.d(TAG, "onDataAvailable: stateInt = " + stateInt);
                    toggleOnOff.setCheckedTogglePosition(stateInt);
                }
            } catch (JSONException e) {
                Log.e(TAG, "onDataAvailable: HomeFragment JSON GET error: " + e.getMessage() );
            }
        }
        Log.d(TAG, "onDataAvailable HomeFragment: ends");
    }

    @Override
    public void onDataAvailable(List<HomeItem> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");
        if(status == DownloadStatus.OK && data != null && data.size() > 0) {
            HomeListAdapter listAdapter = new HomeListAdapter(mContext, 0, data);
            listView = getView().findViewById(R.id.list_view);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    sw = view.findViewById(R.id.switch1);
                    sw.setChecked(!sw.isChecked());
                }
            });
        } else {
            // download or processing failed
            Log.e(TAG, "onDataAvailable failed with status or  " + status + " or it has no items");
        }
        if (progressSpinner.isEnabled()) {
            progressSpinner.setVisibility(View.GONE);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }

    private void getData() {
        GetJsonData getJsonData = new GetJsonData(this, "https://test966996.000webhostapp.com/api/get_alarminfo.php");
        getJsonData.execute();

        GetSimulatorJsonData getSimulatorJsonData = new GetSimulatorJsonData(this, "https://test966996.000webhostapp.com/api/get_simulators.php");
        getSimulatorJsonData.execute("test");

        Log.d(TAG, "getData: data aqquired");
    }
}