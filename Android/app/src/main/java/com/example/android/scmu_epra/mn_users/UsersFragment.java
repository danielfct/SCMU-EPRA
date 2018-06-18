package com.example.android.scmu_epra.mn_users;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.MainActivity;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.Utils;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetUsersJsonData;
import com.example.android.scmu_epra.connection.PostJsonData;

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
    private UsersListAdapter mListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_users, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
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
            mListAdapter = new UsersListAdapter(mContext, 0, data);
            listView.setAdapter(mListAdapter);
            registerForContextMenu(listView);
        } else {
            // download or processing failed
            Toast.makeText(getContext(), R.string.get_users_failed, Toast.LENGTH_LONG).show();
        }
        if (progressSpinner.isEnabled()) {
            progressSpinner.setVisibility(View.GONE);
        }
        emptyView.setVisibility(data.size() == 0 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user_fragment, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        boolean isAdmin = Utils.getCurrentUser(mContext).isAdmin();
        if (isAdmin) {
            getActivity().getMenuInflater().inflate(R.menu.menu_list_users, menu);
        }
        else {
            Snackbar.make(getView(), "Not enough permission to edit users", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        UserItem user = (UserItem) listView.getAdapter().getItem(info.position);
        switch (item.getItemId()) {
            case R.id.edit:
                showEditPermissionsDialog(user);
                return true;
            case R.id.delete:
                showDeleteUserConfirmation(user);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showEditPermissionsDialog(UserItem user) {
        FragmentManager fm = ((Activity)mContext).getFragmentManager();
        EditUserPermissionsDialog dialog = EditUserPermissionsDialog.newInstance(user);
        dialog.show(fm, "fragment_edit_permissions");
    }

    private void showDeleteUserConfirmation(UserItem user) {
        new AlertDialog.Builder(mContext)
                .setIcon(R.drawable.ic_delete_forever_red)
                .setTitle(mContext.getString(R.string.delete_user_confirmation_title))
                .setMessage(mContext.getString(R.string.delete_user_confirmation, user.getName()))
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> removeUser(user))
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void removeUser(UserItem user) {
        PostJsonData postJsonData = new PostJsonData((MainActivity) mContext,
                "https://test966996.000webhostapp.com/api/delete_contacts.php", Constants.Status.DELETE_USER);
        postJsonData.execute("email=" + user.getEmail());
    }

    private void getData() {
        GetUsersJsonData getJsonData = new GetUsersJsonData(this,
                "https://test966996.000webhostapp.com/api/get_users.php");
        getJsonData.execute();
    }
}
