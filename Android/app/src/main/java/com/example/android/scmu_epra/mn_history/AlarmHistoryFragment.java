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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

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
        implements GetHistoryJsonData.OnDataAvailable {

    public static final String TAG = "History";

    @BindView(R.id.alarm_history)
    ListView listView;
    @BindView(R.id.history_progress_spinner)
    ProgressBar progressSpinner;
    AlarmHistoryListAdapter mListAdapter;

    private Context mContext;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
            mListAdapter = new AlarmHistoryListAdapter(mContext, 0, data);
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
        GetHistoryJsonData getJsonData = new GetHistoryJsonData(this,
                "https://test966996.000webhostapp.com/api/get_history.php");
        getJsonData.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_Search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mListAdapter.getFilter().filter(s);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}