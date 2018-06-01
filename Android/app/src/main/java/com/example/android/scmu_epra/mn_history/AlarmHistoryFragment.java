package com.example.android.scmu_epra.mn_history;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetHistoryJsonData;
import com.example.android.scmu_epra.connection.PostJsonData;
import com.example.android.scmu_epra.connection.Row;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmHistoryFragment extends Fragment
        implements GetHistoryJsonData.OnDataAvailable, PostJsonData.OnStatusAvailable {

    public static final String TAG = "History";

    @BindView(R.id.alarm_history)
    ListView listView;
    @BindView(R.id.history_progress_spinner)
    ProgressBar progressSpinner;

    private Context mContext;
    private Handler mHandler;
    private Runnable mRunnable;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_history, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(TAG);

        getData();

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                getData();
            }
        };
        mHandler.postDelayed(mRunnable, Constants.DATA_UPDATE_FREQUENCY);

       /* List<AlarmHistoryItem> list = Arrays.asList(
                new AlarmHistoryItem(AlarmHistoryItem.AlarmHistoryType.AlarmTrigger,"Alarm triggered", "22/04 at 14h27m"),
                new AlarmHistoryItem(AlarmHistoryItem.AlarmHistoryType.AlarmOnOff,"WZ activated the alarm", "22/04 at 9h17m"),
                new AlarmHistoryItem(AlarmHistoryItem.AlarmHistoryType.AlarmOnOff,"WZ deactivated the alarm", "21/04 at 17h03m"),
                new AlarmHistoryItem(AlarmHistoryItem.AlarmHistoryType.AlarmOnOff,"XYZ activated the alarm", "21/04 at 10h31m"));
        AlarmHistoryListAdapter listAdapter = new AlarmHistoryListAdapter(getContext(), 0, list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener((adapterView, v, position, id) -> {
            //TODO: Define item click action here
        });*/
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
    public void onDataAvailable(List<Row> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");
        if(status == DownloadStatus.OK && data != null && data.size() > 0) {
            AlarmHistoryListAdapter listAdapter = new AlarmHistoryListAdapter(mContext, 0, data);
            listView.setAdapter(listAdapter);
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

    @Override
    public void onStatusAvailable(Boolean status) {
        Log.d(TAG, "onStatusAvailable: starts");

        if (status) {
            Log.d(TAG, "onStatusAvailable: SHOW SUCCESS MESSAGE!!");
        } else {
            Log.d(TAG, "onStatusAvailable: SHOW ERROR MESSAGE!!");
        }
        
        Log.d(TAG, "onStatusAvailable: ends");
    }

    private void getData() {
        GetHistoryJsonData getJsonData = new GetHistoryJsonData(this,"https://test966996.000webhostapp.com/api/get_history.php");
        getJsonData.execute("test");
        Log.d(TAG, "getData: data aqquired");

        //PostJsonData postJsonData = new PostJsonData(this, "https://test966996.000webhostapp.com/api/post_history.php");
        //postJsonData.execute("evento=Teste Post Android!");
    }
}