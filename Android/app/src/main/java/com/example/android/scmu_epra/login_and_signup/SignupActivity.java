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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.READ_CONTACTS;

public class SignupActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.close_button)
    FloatingActionButton mCloseButton;
    @BindView(R.id.name_layout)
    TextInputLayout mNameLayout;
    @BindView(R.id.input_name)
    TextInputEditText mNameView;
    @BindView(R.id.mobile_nr_layout)
    TextInputLayout mMobileLayout;
    @BindView(R.id.input_mobile)
    TextInputEditText mMobileView;
    @BindView(R.id.email_layout)
    TextInputLayout mEmailLayout;
    @BindView(R.id.input_email)
    AutoCompleteTextView mEmailView;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;
    @BindView(R.id.input_password)
    TextInputEditText mPasswordView;
    @BindView(R.id.re_enter_password_layout)
    TextInputLayout mReEnterPasswordLayout;
    @BindView(R.id.input_re_enter_password)
    TextInputEditText mReEnterPasswordView;
    @BindView(R.id.signup_button)
    AppCompatButton mSignupButton;
    @BindView(R.id.login_link)
    TextView mLoginLink;

    private AlphaAnimation linkClick = new AlphaAnimation(1F, 0.5F);

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

        mCloseButton.setOnClickListener(view -> login());
        mSignupButton.setOnClickListener(view -> signup());
        mLoginLink.setOnClickListener(view -> {view.startAnimation(linkClick); login();});
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

        // Reset errors
        mNameLayout.setError(null);
        mMobileLayout.setError(null);
        mEmailLayout.setError(null);
        passwordLayout.setError(null);
        mReEnterPasswordLayout.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Store values at the time of the signup attempt.
        String name = mNameView.getText().toString();
        String mobile = mMobileView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String rePassword = mReEnterPasswordView.getText().toString();

        // Check for a valid mobile
        if (!TextUtils.isEmpty(mobile) && !isMobileValid(mobile)) {
            mMobileLayout.setError(getString(R.string.error_invalid_mobile));
            focusView = mMobileLayout;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailLayout.setError(getString(R.string.error_field_required));
            focusView = mEmailLayout;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailLayout.setError(getString(R.string.error_invalid_email));
            focusView = mEmailLayout;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            passwordLayout.setError(getString(R.string.error_field_required));
            focusView = passwordLayout;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordLayout.setError(getString(R.string.error_invalid_password));
            focusView = passwordLayout;
            cancel = true;
        } else if (TextUtils.isEmpty(rePassword)) {
            mReEnterPasswordLayout.setError(getString(R.string.error_field_required));
            focusView = mReEnterPasswordLayout;
            cancel = true;
        } else if (!password.equals(rePassword)) {
            mReEnterPasswordLayout.setError(getString(R.string.error_nonmatching_password));
            focusView = mReEnterPasswordLayout;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mSignupTask = new UserSignupTask(this, name, Integer.parseInt(mobile), email, password);
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
    public static class UserSignupTask extends AsyncTask<Void, Void, Integer> {

        private final WeakReference<SignupActivity> activityReference;
        private final String mName;
        private final int mMobile;
        private final String mEmail;
        private final String mPassword;
        private final ProgressDialog progressDialog;

        UserSignupTask(SignupActivity context, String name, int mobile, String email, String password) {
            this.activityReference = new WeakReference<>(context);
            mName = name;
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
        protected Integer doInBackground(Void... params) {
            SignupActivity activity = getActivity();
            if (activity == null)
                return Constants.Signup.SIGNUP_FAILURE_ACTIVITY_INVALID;
            try {
                URL url = new URL("http://10.22.120.229/epra/api/register_user.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("nome", mName);
                jsonParam.put("telemovel", mMobile);
                jsonParam.put("email", mEmail);
                jsonParam.put("password", Utils.digest("SHA-256", mPassword));

                Log.i("JSON", jsonParam.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());
                Log.i("MESSAGE" , readStream(conn));

                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return Constants.Signup.SIGNUP_FAILURE_EXECUTION_FAILED;
        }

        private String readStream(HttpURLConnection conn) {
            String result = null;
            StringBuffer sb = new StringBuffer();

            try (InputStream is = new BufferedInputStream(conn.getInputStream())) {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String inputLine = "";
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                result = sb.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }


        @Override
        protected void onPostExecute(final Integer failureCode) {
            SignupActivity activity = getActivity();
            if (activity == null)
                return;
            activity.mSignupTask = null;
            progressDialog.dismiss();

            if (failureCode < 0) {
                activity.onSignupSuccess();
            } else {
                activity.onSignupFailed(failureCode);
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
        setResult(RESULT_OK, null); //TODO conta
        finish();
    }

    public void onSignupFailed(int failureCode) {
        Toast.makeText(getBaseContext(), getString(R.string.signup_failed), Toast.LENGTH_LONG).show();
    }

}