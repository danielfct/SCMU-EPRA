package com.example.android.scmu_epra;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.READ_CONTACTS;

public class SignupActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.input_name)
    EditText mNameView;
    @BindView(R.id.input_address)
    EditText mAddressView;
    @BindView(R.id.input_mobile)
    EditText mMobileView;
    @BindView(R.id.input_email)
    AutoCompleteTextView mEmailView;
    @BindView(R.id.input_password)
    EditText mPasswordView;
    @BindView(R.id.input_reEnterPassword)
    EditText mReEnterPasswordView;
    @BindView(R.id.btn_signup)
    Button mSignupButton;
    @BindView(R.id.link_login)
    TextView mLoginLink;

    /**
     * Keep track of the signup task to ensure we can cancel it if requested.
     */
    private UserSignupTask mSignupTask = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        populateAutoComplete();

        mReEnterPasswordView.setOnEditorActionListener((view, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                signup();
                return true;
            }
            return false;
        });

        mSignupButton.setOnClickListener(view -> signup());
        mLoginLink.setOnClickListener((view) -> login());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
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
    }

    private void login() {
        // Finish the registration screen and return to the Login activity
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
    }

    private void signup() {
        if (mSignupTask != null) {
            return;
        }

        // Reset errors.
        mNameView.setError(null);
        mAddressView.setError(null);
        mMobileView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mReEnterPasswordView.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Store values at the time of the signup attempt.
        String name = mNameView.getText().toString();
        String address = mAddressView.getText().toString();
        String mobile = mMobileView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String rePassword = mReEnterPasswordView.getText().toString();

        // Check for a valid mobile
        if (!TextUtils.isEmpty(mobile) && !isMobileValid(mobile)) {
            mMobileView.setError(getString(R.string.error_invalid_mobile));
            focusView = mMobileView;
            cancel = true;
        }
        // Check for a valid email
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(rePassword)) {
            mReEnterPasswordView.setError(getString(R.string.error_field_required));
            focusView = mReEnterPasswordView;
            cancel = true;
        } else if (!password.equals(rePassword)) {
            mReEnterPasswordView.setError(getString(R.string.error_nonmatching_password));
            focusView = mReEnterPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user signup attempt.
            mSignupTask = new UserSignupTask(this,
                    name, address, Integer.parseInt(mobile), email, password);
            mSignupTask.execute((Void) null);
        }
    }

    private boolean isMobileValid(String mobile) {
        return Patterns.PHONE.matcher(mobile).matches();
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return 8 <= password.length() && password.length() <= 20;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
                ProfileQuery.PROJECTION,
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
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
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
                new ArrayAdapter<>(this,
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

    /**
     * Represents an asynchronous registration task used to register
     * the user.
     */
    public static class UserSignupTask extends AsyncTask<Void, Void, Boolean> {

        private final WeakReference<SignupActivity> activityReference;
        private final String mName;
        private final String mAddress;
        private final int mMobile;
        private final String mEmail;
        private final String mPassword;
        private final ProgressDialog progressDialog;

        UserSignupTask(SignupActivity context, String name, String address, int mobile, String email, String password) {
            this.activityReference = new WeakReference<>(context);
            mName = name;
            mAddress = address;
            mMobile = mobile;
            mEmail = email;
            mPassword = password;
            progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt signup user against a network service.
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            SignupActivity activity = getActivity();
            if (activity == null)
                return;
            activity.mSignupTask = null;
            progressDialog.dismiss();

            if (success) {
                activity.onSignupSuccess();
            } else {
                activity.onSignupFailed();
            }
        }

        @Override
        protected void onCancelled() {
            SignupActivity activity = getActivity();
            if (activity == null)
                return;
            activity.mSignupTask = null;
            progressDialog.dismiss();
        }

        private SignupActivity getActivity() {
            // get a reference to the activity if it is still there
            SignupActivity activity = activityReference.get();
            return activity == null || activity.isFinishing() ? null : activity;
        }
    }

    public void onSignupSuccess() {
        mSignupButton.setEnabled(true);
        setResult(RESULT_OK, null); //TODO
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.signup_failed), Toast.LENGTH_LONG).show();
    }

}