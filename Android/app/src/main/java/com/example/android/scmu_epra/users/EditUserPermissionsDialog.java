package com.example.android.scmu_epra.users;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.scmu_epra.MainActivity;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetAreasJsonData;
import com.example.android.scmu_epra.connection.PostJsonData;

import java.util.ArrayList;
import java.util.List;

public class EditUserPermissionsDialog extends DialogFragment implements GetAreasJsonData.OnDataAvailable {

    private ProgressBar mSpinner;
    private ListView mPermissionsList;
    private TextView mEmptyView;
    private ArrayList<Integer> mPermissions;

    public EditUserPermissionsDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditUserPermissionsDialog newInstance(ArrayList<Integer> permissions) {
        EditUserPermissionsDialog frag = new EditUserPermissionsDialog();
        Bundle args = new Bundle();
        args.putIntegerArrayList("permissions", permissions);
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
                "https://test966996.000webhostapp.com/api/post_user.php");
        postJsonData.execute("privilegios=" + TextUtils.join(", ", mPermissions));
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
                    checkedText.setChecked(mPermissions.contains(item.getId()));
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
        mPermissionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AreaItem item = (AreaItem)mPermissionsList.getItemAtPosition(position);
                CheckedTextView checkedText = view.findViewById(android.R.id.text1);
                Log.d("text", checkedText.getText().toString());
                if (checkedText.isChecked()) {
                    checkedText.setChecked(false);
                    mPermissions.add(item.getId());
                }
                else {
                    checkedText.setChecked(true);
                    mPermissions.remove((Integer)item.getId());
                }
            }
        });
        mEmptyView = v.findViewById(android.R.id.empty);
        mPermissions = getArguments().getIntegerArrayList("permissions");

        // Here's the LayoutParams setup
        ViewGroup.LayoutParams layoutParams = mPermissionsList.getLayoutParams();
        layoutParams.width = SwipeRefreshLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = getListItemHeight() * (mPermissions.size() + 1);
        v.setLayoutParams(layoutParams);

        b.setView(v);

        b.setTitle("Permissions");
        b.setPositiveButton("Save", (dialog, whichButton) -> saveData());
        b.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss());

        return b.create();
    }

    private int getListItemHeight() {
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.listPreferredItemHeight, typedValue, true);
        DisplayMetrics metrics = new android.util.DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (int) typedValue.getDimension(metrics);
    }


}
