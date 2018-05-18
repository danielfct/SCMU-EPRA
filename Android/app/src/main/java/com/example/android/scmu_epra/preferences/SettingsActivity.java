package com.example.android.scmu_epra;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;


public class SettingsActivity extends AppCompatActivity {

    private static final String KEY_SEEKBAR_SETTINGS = "seek_bar";
    private static final String KEY_LISTINTERVAL_SETTINGS = "list_interval";
    private static final String KEY_SWITCHPERIOD_SETTINGS = "switch_period";
    private static final String KEY_LISTPERIOD_SETTINGS = "list_period";


    public SettingsActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new LocationFragment())
                .commit();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Integer seekPref = sharedPref.getInt(SettingsActivity.KEY_SEEKBAR_SETTINGS, 0);
        String listInt = sharedPref.getString(SettingsActivity.KEY_LISTINTERVAL_SETTINGS, "-1");
        Boolean switchPer = sharedPref.getBoolean(SettingsActivity.KEY_SWITCHPERIOD_SETTINGS, false);
        String listPer = sharedPref.getString(SettingsActivity.KEY_LISTPERIOD_SETTINGS, "-1");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class LocationFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
        }
    }
}