package com.example.android.scmu_epra.mn_users;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetUsersJsonData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersFragment extends Fragment
        implements GetUsersJsonData.OnDataAvailable {

    public static final String TAG = "UsersFragment";


    @BindView(R.id.users)
    ListView listView;
    @BindView(android.R.id.empty)
    TextView emptyView;
    @BindView(R.id.users_progress_spinner)
    ProgressBar progressSpinner;

    private Context mContext;
    private Handler mHandler;
    private Runnable mRunnable;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_users, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.users);

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
    public void onDataAvailable(List<UserItem> data, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            UsersListAdapter listAdapter = new UsersListAdapter(mContext, 0, data);
            listView.setAdapter(listAdapter);
        } else {
            // download or processing failed
            Toast.makeText(getContext(), R.string.get_users_failed, Toast.LENGTH_LONG).show();
        }
        if (progressSpinner.isEnabled()) {
            progressSpinner.setVisibility(View.GONE);
        }
        emptyView.setVisibility(data.size() == 0 ? View.VISIBLE : View.INVISIBLE);
    }


    private void getData() {
        GetUsersJsonData getJsonData = new GetUsersJsonData(this,
                "https://test966996.000webhostapp.com/api/get_users.php");
        getJsonData.execute();
    }
}
