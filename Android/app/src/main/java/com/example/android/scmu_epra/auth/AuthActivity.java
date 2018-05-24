package com.example.android.scmu_epra.auth;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.scmu_epra.R;

public class AuthActivity extends AppCompatActivity {

    private TextView mHeadingLabel;
    private ImageView mFingerprintImage;
    private TextView mParagraphLabel;

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private EditText pin1EditText;
    private EditText pin2EditText;
    private EditText pin3EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mFingerprintImage = findViewById(R.id.fingerprintImage);
        mParagraphLabel = findViewById(R.id.paragraphLabel);
        pin1EditText = findViewById(R.id.pin1);
        pin2EditText = findViewById(R.id.pin2);
        pin3EditText = findViewById(R.id.pin3);

        if (pin1EditText != null && pin2EditText != null && pin3EditText != null) {
            pin1EditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String pin1Text = pin1EditText.getText().toString();
                    String pin2Text = pin2EditText.getText().toString();
                    String pin3Text = pin3EditText.getText().toString();

                    if (pin1Text.length() > 0 && pin2Text.length() > 0 && pin3Text.length() > 0) {
                        // TODO: checkPin(pin1Text+pin2Text+pin3Text)
                    } else if (pin1Text.length() > 0) {
                            pin2EditText.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            pin2EditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String pin1Text = pin1EditText.getText().toString();
                    String pin2Text = pin2EditText.getText().toString();
                    String pin3Text = pin3EditText.getText().toString();

                    if (pin1Text.length() > 0 && pin2Text.length() > 0 && pin3Text.length() > 0) {
                        // TODO: checkPin(pin1Text+pin2Text+pin3Text)
                    } else if (pin2Text.length() > 0) {
                            pin3EditText.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            pin3EditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String pin1Text = pin1EditText.getText().toString();
                    String pin2Text = pin2EditText.getText().toString();
                    String pin3Text = pin3EditText.getText().toString();

                    if (pin1Text.length() > 0 && pin2Text.length() > 0 && pin3Text.length() > 0) {
                        // TODO: checkPin(pin1Text+pin2Text+pin3Text)
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        }


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
            } else if(!keyguardManager.isKeyguardSecure()) {
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
