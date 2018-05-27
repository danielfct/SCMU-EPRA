package com.example.android.scmu_epra;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.android.scmu_epra.mn_burglaryManag.BurglaryManagementFragment;
import com.example.android.scmu_epra.mn_devices.DevicesFragment;
import com.example.android.scmu_epra.mn_history.AlarmHistoryFragment;
import com.example.android.scmu_epra.mn_home.Home;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private NavigationView navigationView;
    private int currentFragmentID = Integer.MIN_VALUE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void displaySelectedScreen(int id) {
        Fragment fragment = null;

        if (currentFragmentID != id) {
            switch (id) {
                case R.id.nav_home:
                    Home h = new Home();
                    h.setNavigationView(navigationView);
                    fragment = h;
                    currentFragmentID = R.id.nav_home;
                    break;
                case R.id.nav_history:
                    fragment = new AlarmHistoryFragment();
                    currentFragmentID = R.id.nav_history;
                    break;
                case R.id.nav_devices:
                    fragment = new DevicesFragment();
                    currentFragmentID = R.id.nav_devices;
                    break;
                case R.id.nav_burglaryManag:
                    fragment = new BurglaryManagementFragment();
                    currentFragmentID = R.id.nav_burglaryManag;
                    break;
                case R.id.nav_settings:
                    Intent intent = new Intent(this, com.example.android.scmu_epra.SettingsActivity.class);
                    startActivity(intent);
                    currentFragmentID = R.id.nav_settings;
                    break;
            }

            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.screen_area, fragment);
                ft.commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        return true;
    }
}
