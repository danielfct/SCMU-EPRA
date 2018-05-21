package com.example.android.scmu_epra.auth;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.example.android.scmu_epra.R;

@TargetApi(Build.VERSION_CODES.M)
class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    public FingerprintHandler(Context context) {
        this.context = context;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("There was an authentication error: " + errString, false);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Authentication failed!", false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Error: " + helpString, false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("You can now access the app!", true);
    }

    private void update(String s, boolean b) {
        TextView paragraphLabel = (TextView) ((Activity)context).findViewById(R.id.paragraphLabel);

        paragraphLabel.setText(s);

        if (!b) {
            paragraphLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            paragraphLabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }
    }
}
