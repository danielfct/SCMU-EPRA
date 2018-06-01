package com.example.android.scmu_epra;

public interface Constants {

    int REQUEST_SIGNUP = 0;
    int REQUEST_READ_CONTACTS = 1;
    int REQUEST_INTERNET_CONNECTION = 2;

    int DATA_UPDATE_FREQUENCY = 15000;


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
}
