package com.example.android.scmu_epra;

public interface Constants {

    int REQUEST_SIGNUP = 0;
    int REQUEST_READ_CONTACTS = 1;
    int REQUEST_INTERNET_CONNECTION = 2;
    int DATA_UPDATE_FREQUENCY = 15000;
    String SIGNED_ACCOUNT_TAG = "SignedAccount";


    interface Login {
        int LOGIN_SUCCESS = -1;
        int LOGIN_FAILURE_ACTIVITY_INVALID = 0;
        int LOGIN_FAILURE_UNREGISTERED_EMAIL = 1;
        int LOGIN_FAILURE_INCORRECT_PASSWORD = 2;
        int LOGIN_FAILURE = 3;
    }

    interface Signup {
        int SIGNUP_SUCCESS = -1;
        int SIGNUP_FAILURE_ACTIVITY_INVALID = 0;
        int SIGNUP_FAILURE_EMAIl_EXISTS = 1;
        int SIGNUP_FAILURE = 2;
        String EMAIL = "com.example.android.scmu_epra.auth.email";
        String PASSWORD = "com.example.android.scmu_epra.auth.password";

    }

    interface Status {
        int HOME_FRAGMENT = 0;
        int BURGLARY_MANAGEMENT_FRAGMENT = 1;
        int DEVICES_FRAGMENT = 2;
        int EDIT_USER_PERMISSIONS_DIALOG = 3;
        int DELETE_USER = 4;
        int MY_FIREBASE_INSTANCE_ID_SERVICE = 5;
        int ADD_NEW_DEVICE = 6;
        int DELETE_DEVICE = 7;
        int UPDATE_DEVICE = 8;
        int BURG_ALARM_OFF = 9;
        int DELETE_AREA = 10;
        int UPDATE_HISTORY = 11;
        int UPDATE_ALARM_STATUS = 12;
        int UPDATE_AREA = 13;
    }

    interface Bundle {
        String USER = "USER";
        String DEVICE = "DEVICE";
        String AREA = "AREA";
    }
}
