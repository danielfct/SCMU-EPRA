package com.example.android.scmu_epra;

public interface Constants {

    int REQUEST_SIGNUP = 0;
    int REQUEST_READ_CONTACTS = 1;
    int REQUEST_INTERNET_CONNECTION = 2;

    interface Login {
        int LOGIN_SUCCESS = -1;
        int LOGIN_FAILURE_ACTIVITY_INVALID = 0;
        int LOGIN_FAILURE_INCORRECT_EMAIL = 1;
        int LOGIN_FAILURE_INCORRECT_PASSWORD = 2;
        int LOGIN_FAILURE_TIMEOUT = 3;
        int LOGIN_FAILURE_UNKNOWN = 4;
        int LOGIN_FAILURE_EXECUTION_FAILED = 5;
    }
}
