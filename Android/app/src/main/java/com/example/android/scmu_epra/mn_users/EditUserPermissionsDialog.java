package com.example.android.scmu_epra.mn_users;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.MainActivity;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetAreasJsonData;
import com.example.android.scmu_epra.connection.PostJsonData;
import com.google.gson.Gson;

import java.util.List;

public class EditUserPermissionsDialog extends DialogFragment implements GetAreasJsonData.OnDataAvailable {

    private ProgressBar mSpinner;
    private ListView mPermissionsList;
    private TextView mEmptyView;
    private UserItem mUser;

    public EditUserPermissionsDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditUserPermissionsDialog newInstance(UserItem user) {
        EditUserPermissionsDialog frag = new EditUserPermissionsDialog();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String jsonUser = gson.toJson(user);
        args.putString(Constants.Bundle.USER, jsonUser);
        frag.setArguments(args);
        return frag;
    }

    private void getData() {
        mSpinner.setVisibility(View.VISIBLE);
        GetAreasJsonData getJsonData = new GetAreasJsonData(this,
                "https://test966996.000webhostapp.com/api/get_areas.php");
        getJsonData.execute();
    }

    private void saveData() {
        PostJsonData postJsonData = new PostJsonData((MainActivity) getActivity(),
                "https://test966996.000webhostapp.com/api/update_users.php", Constants.Status.EDIT_USER_PERMISSIONS_DIALOG);
        postJsonData.execute("privilegios=" + TextUtils.join(",", mUser.getPermissions()),
                "email=" + mUser.getEmail());
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        super.onResume();
        getData();
    }

    @Override
    public void onDataAvailable(List<AreaItem> data, DownloadStatus status) {
        Log.d("data_size", String.valueOf(data.size()));
        if (status == DownloadStatus.OK) {

            ArrayAdapter<AreaItem> arrayAdapter = new ArrayAdapter<AreaItem>(getContext(),
                    android.R.layout.select_dialog_multichoice,
                    data) {
                @Override @NonNull
                public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                    View v = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_multiple_choice, null);

                    AreaItem item = data.get(position);
                    CheckedTextView checkedText = v.findViewById(android.R.id.text1);
                    checkedText.setText(item.getName());
                    checkedText.setChecked(mUser.getPermissions().contains(item.getId()));
                    checkedText.setOnClickListener(view -> {
                        boolean isChecked = checkedText.isChecked();
                        if (isChecked) {
                            mUser.removePermission(item.getId());
                        }
                        else {
                            mUser.addPermission(item.getId());
                        }
                        checkedText.setChecked(!isChecked);
                    });
                    return v;
                }
            };
            mPermissionsList.setAdapter(arrayAdapter);


        } else {
            // download or processing failed
        }
        mSpinner.setVisibility(View.INVISIBLE);
        mEmptyView.setVisibility(data.size() == 0 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.edit_permissions_layout, null);

        mSpinner = v.findViewById(R.id.progress_bar);
        mPermissionsList = v.findViewById(R.id.permissions);
        mEmptyView = v.findViewById(android.R.id.empty);
        String stringUser = getArguments().getString(Constants.Bundle.USER);
        Gson gson = new Gson();
        mUser = gson.fromJson(stringUser, UserItem.class);
        // Here's the LayoutParams setup
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ViewGroup.LayoutParams layoutParams = mPermissionsList.getLayoutParams();
        layoutParams.width = SwipeRefreshLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = (int) (displayMetrics.heightPixels*0.5f);
        v.setLayoutParams(layoutParams);

        b.setView(v);

        b.setTitle("Permissions");
        b.setPositiveButton("Save", (dialog, whichButton) -> saveData());
        b.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss());

        return b.create();
    }

}
