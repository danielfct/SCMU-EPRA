package com.example.android.scmu_epra.mn_burglaryManag;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import com.example.android.scmu_epra.connection.PostJsonData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BurglaryManagementFragment extends Fragment
        implements GetBurglaryHistoryJsonData.OnDataAvailable, GetJsonData.OnDataAvailable, PostJsonData.OnStatusAvailable {

    public static final String TAG = "BurglaryManagement";
    private static final int REQUEST_LOCATION = 1;

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
    private String trackingId = "-1";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            trackingId = getArguments().getString("id");
            Log.d(TAG, "onCreateView: Burglary got id = " + trackingId);
        }

        GetJsonData getJsonData = new GetJsonData(this, "https://test966996.000webhostapp.com/api/get_tracking.php?id="+trackingId);
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

        ignoreButton.setOnClickListener(v -> {});
        simulateButton.setOnClickListener(v -> {});
        turnOffAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executePostJson("https://test966996.000webhostapp.com/api/post_alarminfo.php", "estadoAtual=0");
            }
        });
        notifyPoliceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:960000000"));

                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onClick: phone call - permission not granted!");
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_LOCATION);
                    return;
                }

                Log.d(TAG, "onClick: PERMISSION GRANTED!!");
                startActivity(callIntent);
            }
        });

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
                "https://test966996.000webhostapp.com/api/get_historicoTracking.php?id="+trackingId);
        getJsonData.execute();
    }

    @Override
    public void onDataAvailable(JSONObject data) {
        try {
            if (data != null) {
                String areaAtual = data.getString("atual");
                String areaEntrada = data.getString("entrada");
                String pessoasNotificadas = data.getString("pessoasNotificadas");
                currentRoomText.setText(areaAtual);
                entryPointText.setText(areaEntrada);
                usersNotifiedText.setText(pessoasNotificadas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final void executePostJson(String url, String... params) {
        PostJsonData postJsonData = new PostJsonData(this, url);
        postJsonData.execute(params);
    }

    @Override
    public void onStatusAvailable(Boolean status) {
        if (status) {
            Snackbar.make(getView(), "Alarm turned off.", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(getView(), "Unable to connect to the server.", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:960000000"));
                    startActivity(callIntent);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static BurglaryManagementFragment newInstance(String arg) {
        Bundle b = new Bundle();
        b.putString("id", arg);

        BurglaryManagementFragment fragment = new BurglaryManagementFragment();
        fragment.setArguments(b);

        return fragment;
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
