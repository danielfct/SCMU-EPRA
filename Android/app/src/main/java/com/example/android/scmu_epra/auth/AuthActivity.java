package com.example.android.scmu_epra.auth;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.scmu_epra.R;

public class AuthActivity extends AppCompatActivity {

    private TextView mHeadingLabel;
    private ImageView mFingerprintImage;
    private TextView mParagraphLabel;

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mHeadingLabel = findViewById(R.id.headingLabel);
        mFingerprintImage = findViewById(R.id.fingerprintImage);
        mParagraphLabel = findViewById(R.id.paragraphLabel);

        // Android version must be greater or equal to Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            // Check if device has fingerprint scanner
            if (!fingerprintManager.isHardwareDetected()) {
                mParagraphLabel.setText("Fingerprint scanner not detected in your device.");
                // Check if permission to use fingerprint scanner has been granted
            } else if(ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                mParagraphLabel.setText("Permission not granted to use fingerprint scanner.");
                // Check if lock screen is secured with at least one type of lock
            } else if(keyguardManager.isKeyguardSecure()) {
                mParagraphLabel.setText("Add lock to your phone in settings.");
                // Check if at least one fingerprint is registered
            } else if(!fingerprintManager.hasEnrolledFingerprints()) {
                mParagraphLabel.setText("You should add at least one fingerprint to use this feature.");
            } else {
                mParagraphLabel.setText("Place your finger on the scanner to access the system.");


                FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                fingerprintHandler.startAuth(fingerprintManager, null);
            }
        }
    }
}
