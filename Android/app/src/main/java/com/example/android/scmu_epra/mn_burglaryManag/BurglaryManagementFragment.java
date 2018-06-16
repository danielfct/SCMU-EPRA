package com.example.android.scmu_epra.mn_burglaryManag;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.android.scmu_epra.BottomSheetListView;
import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetBurglaryHistoryJsonData;
import com.example.android.scmu_epra.connection.GetHistoryJsonData;
import com.example.android.scmu_epra.connection.GetJsonData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BurglaryManagementFragment extends Fragment
        implements GetBurglaryHistoryJsonData.OnDataAvailable, GetJsonData.OnDataAvailable {

    public static final String TAG = "BurglaryManagement";

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
    @BindView(R.id.burglary_management_progress_spinner)
    ProgressBar progressSpinner;
    @BindView(R.id.entry_point_text)
    TextView entryPointText;
    @BindView(R.id.current_room_text)
    TextView currentRoomText;
    @BindView(R.id.users_notified_text)
    TextView usersNotifiedText;

    private boolean bottomSheetIsSet = false;
    private Context mContext;
    private Handler mHandler;
    private Runnable mRunnable;
    private BurglaryManagementListAdapter mListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        GetJsonData getJsonData = new GetJsonData(this, "https://test966996.000webhostapp.com/api/get_tracking.php?id=1");
        getJsonData.execute();

        View view = inflater.inflate(R.layout.frag_burglary_manag, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(TAG);

        getData();

        mHandler = new Handler();
        mRunnable = this::getData;

        mHandler.postDelayed(mRunnable, Constants.DATA_UPDATE_FREQUENCY);

        notifyPoliceButton.setOnClickListener(v -> {});
        simulateButton.setOnClickListener(v -> {});
        turnOffAlarmButton.setOnClickListener(v -> {});
        ignoreButton.setOnClickListener(v -> {});

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomListView);
        baseLayout.addOnLayoutChangeListener((v, i, i1, i2, i3, i4, i5, i6, i7) -> {
            if (!bottomSheetIsSet && Math.abs(i1-i3) > 0) {
                bottomSheetBehavior.setPeekHeight(Math.abs(i1 - i3));
                bottomSheetIsSet = true;
            }
        });
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

    @Override
    public void onDataAvailable(List<BurglaryManagementItem> data, DownloadStatus status) {
        if (status == DownloadStatus.OK && data != null && data.size() > 0) {
            mListAdapter = new BurglaryManagementListAdapter(mContext, 0, data);
            listView.setAdapter(mListAdapter);
            listView.setOnItemClickListener((adapterView, v, position, id) -> {
                //TODO: Define item click action here
            });
        } else {
            // download or processing failed
            Log.e(TAG, "onDataAvailable failed with status " + status);
        }
        if (progressSpinner.isEnabled()) {
            progressSpinner.setVisibility(View.GONE);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }

    private void getData() {
        GetBurglaryHistoryJsonData getJsonData = new GetBurglaryHistoryJsonData(this,
                "https://test966996.000webhostapp.com/api/get_historicoTracking.php");
        getJsonData.execute();
    }

    @Override
    public void onDataAvailable(JSONObject data) {
        try {
            String areaAtual = data.getString("atual");
            String areaEntrada = data.getString("entrada");
            String pessoasNotificadas = data.getString("pessoasNotificadas");
            currentRoomText.setText(areaAtual);
            entryPointText.setText(areaEntrada);
            usersNotifiedText.setText(pessoasNotificadas);

            GetJsonData getJsonData = new GetJsonData(this, "https://test966996.000webhostapp.com/api/get_tracking.php?id=1");
            getJsonData.execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // Unnecessary - for search in the toolbar
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_search, menu);
//        MenuItem item = menu.findItem(R.id.menu_Search);
//        SearchView searchView = (SearchView)item.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                mListAdapter.getFilter().filter(s);
//                return false;
//            }
//        });
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }

}
