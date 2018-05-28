package com.example.android.scmu_epra.users;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetUsersJsonData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersFragment extends Fragment
        implements GetUsersJsonData.OnDataAvailable {

    public static final String TAG = "UsersFragment";

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.users)
    ListView listView;
    @BindView(android.R.id.empty)
    TextView emptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getData() {
        swipeRefresh.setRefreshing(true);
        GetUsersJsonData getJsonData = new GetUsersJsonData(this,
                "https://test966996.000webhostapp.com/api/get_users.php");
        getJsonData.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users_layout, container, false);
        ButterKnife.bind(this, view);

        swipeRefresh.setOnRefreshListener(() -> getData());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.users);
    }

    @Override
    public void onDataAvailable(List<UserItem> data, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            UsersListAdapter listAdapter = new UsersListAdapter(getActivity().getApplicationContext(), 0, data);
            listView.setAdapter(listAdapter);
        } else {
            // download or processing failed
            Toast.makeText(getContext(), R.string.get_users_failed, Toast.LENGTH_LONG).show();
        }
        swipeRefresh.setRefreshing(false);
        emptyView.setVisibility(data.size() == 0 ? View.VISIBLE : View.INVISIBLE);
    }

}
