package com.example.android.scmu_epra.mn_home;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.scmu_epra.BottomSheetListView;
import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.MainActivity;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.Utils;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetAreasJsonData;
import com.example.android.scmu_epra.connection.GetJsonData;
import com.example.android.scmu_epra.connection.GetSimulatorJsonData;
import com.example.android.scmu_epra.connection.PostJsonData;
import com.example.android.scmu_epra.mn_history.AlarmHistoryFragment;
import com.example.android.scmu_epra.mn_users.AreaItem;
import com.example.android.scmu_epra.mn_users.UserItem;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements
        GetJsonData.OnDataAvailable,
        GetAreasJsonData.OnDataAvailable,
        PostJsonData.OnStatusAvailable {

    public static final String TAG = "Home";

    @BindView(R.id.show_history)
    Button btnShowHistory;
    @BindView(R.id.toggleOnOff)
    ToggleButtonMine toggleOnOff;
    @BindView(R.id.activeInfo)
    TextView activeInfo;
    @BindView(R.id.bottom_sheet)
    LinearLayout bottomListView;
    @BindView(R.id.backgroundOfButton)
    LinearLayout baseLayout;
    @BindView(R.id.home_progress_spinner)
    ProgressBar progressSpinner;
    @BindView(R.id.list_view)
    BottomSheetListView listView;
    @BindView(R.id.home_baseLayout)
    CoordinatorLayout coordinatorLayout;


    private View thisView;
    private View unlockView;
    private boolean alarmIsOn;
    private boolean bottomSheetIsSet = false;
    private Switch sw;
    private Context mContext;
    private Handler mHandler;
    private Runnable mRunnable;
    private HomeListAdapter listAdapter;

    private NavigationView navigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        alarmIsOn = false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getActivity().setTitle(TAG);

        getData();

        mHandler = new Handler();
        mRunnable = this::getData;
        mHandler.postDelayed(mRunnable, Constants.DATA_UPDATE_FREQUENCY);

        btnShowHistory.setOnClickListener((v) -> {
            navigationView.getMenu().getItem(1).setChecked(true);

            Fragment f = new AlarmHistoryFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.screen_area, f);
            ft.commit();
        });

        toggleOnOff.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        toggleOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onclick", "toggle1");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                unlockView = getLayoutInflater().from(getActivity()).inflate(R.layout.activity_auth, null);
                builder.setView(unlockView);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                processUnlockDialog(builder.show());

            }
        });

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomListView);

        Rect baseRect = new Rect();
        baseLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if (!bottomSheetIsSet && Math.abs(i1 - i3) > 0) {
                    bottomSheetBehavior.setPeekHeight(Math.abs(i1 - i3));
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
                    if (stateInt == 1) {
                        toggleOnOff.setChecked(true);
                        String ativoDesde = data.getString("ativoDesde");
                        String dataAtual = data.getString("dataAtual");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date date1 = format.parse(ativoDesde);
                            Date date2 = format.parse(dataAtual);
                            long diff = date2.getTime() - date1.getTime();
                            long seconds = diff / 1000;
                            long minutes = seconds / 60;
                            long hours = minutes / 60;
                            long days = hours / 24;
                            if (days >= 1) {
                                activeInfo.setText("Alarm active for " + days + " day" + (days >= 2 ? "s" : ""));
                            } else if (hours >= 1) {
                                activeInfo.setText("Alarm active for " + hours + " hour" + (hours >= 2 ? "s" : ""));
                            } else if (minutes >= 1) {
                                activeInfo.setText("Alarm active for " + minutes + " minute" + (minutes >= 2 ? "s" : ""));
                            } else {
                                activeInfo.setText("Alarm active for " + seconds + " second" + (seconds >= 2 ? "s" : ""));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        activeInfo.setVisibility(View.VISIBLE);
                    } else {
                        toggleOnOff.setChecked(false);
                        activeInfo.setText("Alarm is inactive.");
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "onDataAvailable: HomeFragment JSON GET error: " + e.getMessage());
            }
        }
        Log.d(TAG, "onDataAvailable HomeFragment: ends");
    }

    private void getData() {
        GetJsonData getJsonData = new GetJsonData(this, "https://test966996.000webhostapp.com/api/get_alarminfo.php");
        getJsonData.execute();

        GetAreasJsonData getAreasJsonData = new GetAreasJsonData(this, "https://test966996.000webhostapp.com/api/get_areas.php");
        getAreasJsonData.execute();

        Log.d(TAG, "getData: data aqquired");
    }


    private void processUnlockDialog(AlertDialog dialog) {
        TextView uParagraphLabel;
        FingerprintManager uFingerprintManager;
        KeyguardManager uKeyguardManager;
        EditText uPin1EditText;

        UserItem currentAccount = Utils.getCurrentUser(mContext);
        String userPassword = currentAccount.getPassword();

        uParagraphLabel = unlockView.findViewById(R.id.paragraphLabel);
        uPin1EditText = unlockView.findViewById(R.id.pin1);

        if (uPin1EditText != null) {
            uPin1EditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String pin1Text = uPin1EditText.getText().toString();

                    if (pin1Text.length() > 0) {
                        if (pin1Text.equals(userPassword)) {
                            dialog.dismiss();
                            toggleAlarm();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }


        // Android version must be greater or equal to Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uFingerprintManager = (FingerprintManager) getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
            uKeyguardManager = (KeyguardManager) getActivity().getSystemService(Context.KEYGUARD_SERVICE);


            // Check if device has fingerprint scanner
            if (!uFingerprintManager.isHardwareDetected()) {
                uParagraphLabel.setText("Fingerprint scanner not detected in your device.");
                // Check if permission to use fingerprint scanner has been granted
            } else if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                uParagraphLabel.setText("Permission not granted to use fingerprint scanner.");
                // Check if lock screen is secured with at least one type of lock
            } else if (!uKeyguardManager.isKeyguardSecure()) {
                uParagraphLabel.setText("Add lock to your phone in settings.");
                // Check if at least one fingerprint is registered
            } else if (!uFingerprintManager.hasEnrolledFingerprints()) {
                uParagraphLabel.setText("You should add at least one fingerprint to use this feature.");
            } else {
                uParagraphLabel.setText("Place your finger on the scanner to access the system.");


                FingerprintHandler fingerprintHandler = new FingerprintHandler(this, getActivity(), uParagraphLabel, dialog);
                fingerprintHandler.startAuth(uFingerprintManager, null);
            }
        }
    }

    //TODO: Connect to the server
    public void toggleAlarm() {
        if (toggleOnOff.isChecked()) {
            toggleOnOff.setChecked(false);
            activeInfo.setText("Alarm is now inactive.");
        } else {
            toggleOnOff.setChecked(true);
            activeInfo.setText("Alarm is now active.");
        }

        UserItem user = Utils.getCurrentUser(mContext);
        executePostJson(Constants.Status.UPDATE_ALARM_STATUS,"https://test966996.000webhostapp.com/api/post_alarminfo.php", "estadoAtual=" + (toggleOnOff.isChecked() ? "1" : "0"));
        executePostJson(Constants.Status.UPDATE_HISTORY,"https://test966996.000webhostapp.com/api/post_history.php", "evento=" + user.getName() + (toggleOnOff.isChecked() ? " turned the alarm on." : " turned the alarm off."));
    }

    @Override
    public void onDataAvailable(List<AreaItem> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");
        if (status == DownloadStatus.OK && data != null && data.size() > 0) {
            listAdapter = new HomeListAdapter(mContext, 0, data);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener((adapterView, view, position, id) -> {
                UserItem currentAccount = Utils.getCurrentUser(mContext);

                AreaItem item = data.get(position);
                boolean b = (currentAccount != null && currentAccount.getPermissions().contains(item.getId()));
                if (b) {
                    sw = view.findViewById(R.id.switch1);

                    boolean newState = !sw.isChecked();
                    executePostJson(Constants.Status.UPDATE_AREA,"https://test966996.000webhostapp.com/api/update_areas.php",
                            "id=" + item.getId(),
                            "nome=" + item.getName(),
                            "alarmeLigado=" + (newState ? "1" : "0"));
                    sw.setChecked(newState);
                    UserItem user = Utils.getCurrentUser(mContext);
                    executePostJson(Constants.Status.UPDATE_HISTORY,"https://test966996.000webhostapp.com/api/post_history.php", "evento="+user.getName()+" turned "+item.getName()+" "+ (newState? "on." : "off."));
                }

                registerForContextMenu(listView);

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

    private void executePostJson(int id, String url, String... params) {
        PostJsonData postJsonData = new PostJsonData(this, url, id);
        postJsonData.execute(params);
    }


    @Override
    public void onStatusAvailable(Boolean status, Integer statusId) {
        if (status) {
            String feedback = "Updated successfully!";
            boolean showFeedback = false;
            switch (statusId) {
                case Constants.Status.UPDATE_ALARM_STATUS:
                    feedback = "Alarm status updated successfully!";
                    showFeedback = true;
                    break;
                case Constants.Status.UPDATE_AREA:
                    feedback = "Area status updated successfully!";
                    showFeedback = true;
                    break;
                default: break;
            }
            if (showFeedback)
                Snackbar.make(getView(), feedback, Snackbar.LENGTH_SHORT).show();
        } else {
            if (statusId != Constants.Status.UPDATE_HISTORY)
                Snackbar.make(getView(), "Unable to connect to the server.", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        UserItem currentAccount = Utils.getCurrentUser(mContext);
        AdapterView.AdapterContextMenuInfo info = ( AdapterView.AdapterContextMenuInfo) menuInfo;
        int position = info.position;

        boolean isAbleTo = (currentAccount != null && currentAccount.getPermissions()
                .contains( listAdapter.getItem(position).getId()));
        if (isAbleTo) {
            getActivity().getMenuInflater().inflate(R.menu.menu_list_home, menu);
        }
        else {
            Snackbar.make(getView(), "Not enough permission to edit this area", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        AreaItem area = (AreaItem)listView.getAdapter().getItem(info.position);
        switch(item.getItemId()) {
            case R.id.edit:
                showEditAreaDialog(area);
                return true;
            case R.id.delete:
                showDeleteAreaConfirmation(area);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showEditAreaDialog(AreaItem area) {
        FragmentManager fm = ((Activity)mContext).getFragmentManager();
        EditAreaDialog dialog = EditAreaDialog.newInstance(area);
        dialog.show(fm, "dialog_edit_area");
    }

    private boolean showAddNewAreaDialog() {
        FragmentManager fm = ((Activity)mContext).getFragmentManager();
        NewAreaDialog dialog = new NewAreaDialog();
        dialog.show(fm, "dialog_edit_area");
        return true;
    }

    private void showDeleteAreaConfirmation(AreaItem area) {
        new AlertDialog.Builder(mContext)
                .setIcon(R.drawable.ic_delete_forever_red)
                .setTitle(mContext.getString(R.string.delete_area_confirmation_title))
                .setMessage(mContext.getString(R.string.delete_area_confirmation, area.getName()))
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> removeArea(area))
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void removeArea(AreaItem area) {
        PostJsonData postJsonData = new PostJsonData((MainActivity) mContext,
                "https://test966996.000webhostapp.com/api/delete_areas.php", Constants.Status.DELETE_AREA);
        postJsonData.execute("id=" + area.getId());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        MenuItem addItem = menu.findItem(R.id.menu_new_area);
        addItem.setOnMenuItemClickListener((item) -> showAddNewAreaDialog());
        super.onCreateOptionsMenu(menu, inflater);
    }


}






