package com.example.android.scmu_epra.mn_home;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
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
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.scmu_epra.BottomSheetListView;
import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.MainActivity;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetJsonData;
import com.example.android.scmu_epra.connection.GetSimulatorJsonData;
import com.example.android.scmu_epra.mn_history.AlarmHistoryFragment;
import com.example.android.scmu_epra.mn_users.UserItem;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements GetJsonData.OnDataAvailable, GetSimulatorJsonData.OnDataAvailable {

    public static final String TAG = "Home";

    @BindView(R.id.show_history)
    Button btnShowHistory;
    @BindView(R.id.toggleOnOff)
    ToggleButtonMine toggleOnOff;
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

    private NavigationView navigationView;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alarmIsOn = false;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        getActivity().setTitle(TAG);

        Log.d(TAG, "onViewCreated: sw = " + (sw != null));

        getData();

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                getData();
            }
        };
        mHandler.postDelayed(mRunnable, Constants.DATA_UPDATE_FREQUENCY);

        btnShowHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(1).setChecked(true);

                Fragment f = new AlarmHistoryFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.screen_area, f);
                ft.commit();
            }
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
                Log.d("onclick","toggle1");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                unlockView = getLayoutInflater().from(getActivity()).inflate(R.layout.activity_auth, null);
                builder.setView(unlockView);
                processUnlockDialog(builder.show());

            }
        });



        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomListView);

        Rect baseRect = new Rect();
        baseLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if (!bottomSheetIsSet && Math.abs(i1-i3) > 0) {
                    bottomSheetBehavior.setPeekHeight(Math.abs(i1-i3));
                    bottomSheetIsSet = true;
                }
            }
        });
        int rH = baseRect.height();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        thisView = getLayoutInflater().inflate(R.layout.frag_home, container, false);
        return thisView;
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
                        //TODO:turn toggle on
                    }
                    else {
                        toggleOnOff.setChecked(false);
                        //TODO: turn toggle off
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "onDataAvailable: HomeFragment JSON GET error: " + e.getMessage() );
            }
        }
        Log.d(TAG, "onDataAvailable HomeFragment: ends");
    }

    @Override
    public void onDataAvailable(List<HomeItem> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");
        if(status == DownloadStatus.OK && data != null && data.size() > 0) {
            HomeListAdapter listAdapter = new HomeListAdapter(mContext, 0, data);
            listView = getView().findViewById(R.id.list_view);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //TODO: Define item click action here
                    sw = view.findViewById(R.id.switch1);
                    sw.setChecked(!sw.isChecked());
                }
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

    private void getData() {
        GetJsonData getJsonData = new GetJsonData(this, "https://test966996.000webhostapp.com/api/get_alarminfo.php");
        getJsonData.execute();

        GetSimulatorJsonData getSimulatorJsonData = new GetSimulatorJsonData(this, "https://test966996.000webhostapp.com/api/get_simulators.php");
        getSimulatorJsonData.execute("test");

        Log.d(TAG, "getData: data aqquired");
    }




    private void processUnlockDialog(AlertDialog dialog) {
        TextView uHeadingLabel;
        ImageView uFingerprintImage;
        TextView uParagraphLabel;
        FingerprintManager uFingerprintManager;
        KeyguardManager uKeyguardManager;
        EditText uPin1EditText;
        EditText uPin2EditText;
        EditText uPin3EditText;


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = sharedPref.getString(Constants.SIGNED_ACCOUNT_TAG, "");
        UserItem currentAccount = gson.fromJson(json, UserItem.class);
        String userPassword = UserItem.getPassword();


        uFingerprintImage = unlockView.findViewById(R.id.fingerprintImage);
        uParagraphLabel = unlockView.findViewById(R.id.paragraphLabel);
        uPin1EditText = unlockView.findViewById(R.id.pin1);
//        uPin2EditText = unlockView.findViewById(R.id.pin2);
//        uPin3EditText = unlockView.findViewById(R.id.pin3);


        if (uPin1EditText != null) {
            uPin1EditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String pin1Text = uPin1EditText.getText().toString();

                    if (pin1Text.length() > 0) {
                        // TODO: checkPin
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

//            uPin2EditText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    String pin1Text = uPin1EditText.getText().toString();
//                    String pin2Text = uPin2EditText.getText().toString();
//                    String pin3Text = uPin3EditText.getText().toString();
//
//                    if (pin1Text.length() > 0 && pin2Text.length() > 0 && pin3Text.length() > 0) {
//                        // TODO: checkPin(pin1Text+pin2Text+pin3Text)
//                    } else if (pin2Text.length() > 0) {
//                        uPin3EditText.requestFocus();
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                }
//            });
//
//            uPin3EditText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    String pin1Text = uPin1EditText.getText().toString();
//                    String pin2Text = uPin2EditText.getText().toString();
//                    String pin3Text = uPin3EditText.getText().toString();
//
//                    if (pin1Text.length() > 0 && pin2Text.length() > 0 && pin3Text.length() > 0) {
//                        // TODO: checkPin(pin1Text+pin2Text+pin3Text)
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                }
//            });

        }


        // Android version must be greater or equal to Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uFingerprintManager = (FingerprintManager) getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
            uKeyguardManager = (KeyguardManager) getActivity().getSystemService(Context.KEYGUARD_SERVICE);


            // Check if device has fingerprint scanner
            if (!uFingerprintManager.isHardwareDetected()) {
                uParagraphLabel.setText("Fingerprint scanner not detected in your device.");
                // Check if permission to use fingerprint scanner has been granted
            } else if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                uParagraphLabel.setText("Permission not granted to use fingerprint scanner.");
                // Check if lock screen is secured with at least one type of lock
            } else if(!uKeyguardManager.isKeyguardSecure()) {
                uParagraphLabel.setText("Add lock to your phone in settings.");
                // Check if at least one fingerprint is registered
            } else if(!uFingerprintManager.hasEnrolledFingerprints()) {
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
        if (toggleOnOff.isChecked()){
            toggleOnOff.setChecked(false);
        }
        else{
            toggleOnOff.setChecked(true);
        }
    }
}






