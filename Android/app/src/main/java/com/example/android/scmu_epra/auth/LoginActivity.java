package com.example.android.scmu_epra.auth;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.MainActivity;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.DownloadStatus;
import com.example.android.scmu_epra.connection.GetUsersJsonData;
import com.example.android.scmu_epra.mn_users.UserItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_CONTACTS;

// TODO


public class LoginActivity extends AppCompatActivity
        implements GetUsersJsonData.OnDataAvailable,
        LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.email_layout)
    TextInputLayout mEmailLayout;
    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;
    @BindView(R.id.password_layout)
    TextInputLayout mPasswordLayout;
    @BindView(R.id.password)
    TextInputEditText mPasswordView;
    @BindView(R.id.login_button)
    AppCompatButton mLoginButton;
    @BindView(R.id.signup_button)
    FloatingActionButton mSignupButton;


    private AlphaAnimation linkClick = new AlphaAnimation(1F, 0.5F);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        populateAutoComplete();

        mPasswordView.setOnEditorActionListener((view, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                login();
                return true;
            }
            return false;
        });

        mLoginButton.setOnClickListener(view -> login());
        mSignupButton.setOnClickListener(view -> signup());
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_email, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, Constants.REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, Constants.REQUEST_READ_CONTACTS);
        }
        return false;
    }

    private boolean mayPerformNetworking() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(INTERNET) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(INTERNET)) {
            Snackbar.make(mLoginButton, R.string.permission_internet, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{INTERNET}, Constants.REQUEST_INTERNET_CONNECTION);
                        }
                    });
        } else {
            requestPermissions(new String[]{INTERNET}, Constants.REQUEST_INTERNET_CONNECTION);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
        else if (requestCode == Constants.REQUEST_INTERNET_CONNECTION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                login();
            }
        }
    }

    private void signup() {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivityForResult(intent, Constants.REQUEST_SIGNUP);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void login() {
        if (!mayPerformNetworking()) {
            return;
        }

        // Reset errors
        mEmailLayout.setError(null);
        mPasswordLayout.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            mEmailLayout.setError(getString(R.string.error_field_required));
            focusView = mEmailLayout;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordLayout.setError(getString(R.string.error_field_required));
            focusView = mPasswordLayout;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            executeLogin();
        }
    }

    private void executeLogin() {
        ProgressDialog d = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        d.setIndeterminate(true);
        d.setMessage("Authenticating...");
        GetUsersJsonData loginTask = new GetUsersJsonData(this,
                "https://test966996.000webhostapp.com/api/get_users.php", d);
        loginTask.execute(mEmailView.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                String email = data.getStringExtra(Constants.Signup.EMAIL);
                String password = data.getStringExtra(Constants.Signup.PASSWORD);
                mEmailView.setText(email);
                mPasswordView.setText(password);
                Toast.makeText(this, getString(R.string.signup_success), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
                LoginActivity.ProfileQuery.PROJECTION,
                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},
                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(LoginActivity.ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    @Override
    public void onDataAvailable(List<UserItem> user, DownloadStatus status) {
        if (user.size() < 1) {
            onLoginFailed(Constants.Login.LOGIN_FAILURE_UNREGISTERED_EMAIL);
        }
        else if (!user.get(0).getPassword().equalsIgnoreCase(mPasswordView.getText().toString())) {
            onLoginFailed(Constants.Login.LOGIN_FAILURE_INCORRECT_PASSWORD);
        }
        else {
            onLoginSuccess(user.get(0));
        }
    }

    private void onLoginSuccess(UserItem user) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString(Constants.SIGNED_ACCOUNT_TAG, json);
        prefsEditor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void onLoginFailed(int failureCode) {
        Log.i("login_code", String.valueOf(failureCode));
        if (failureCode == Constants.Login.LOGIN_FAILURE_UNREGISTERED_EMAIL) {
            mEmailLayout.setError(getString(R.string.error_unregistered_email));
            mEmailLayout.requestFocus();
        }
        else if (failureCode == Constants.Login.LOGIN_FAILURE_INCORRECT_PASSWORD) {
            mPasswordLayout.setError(getString(R.string.error_incorrect_password));
            mPasswordLayout.requestFocus();
        }
        else {
            Toast.makeText(getBaseContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();
        }
        hideKeyboard();
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
