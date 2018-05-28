package com.example.android.scmu_epra;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.example.android.scmu_epra.connection.PostJsonData;
import com.example.android.scmu_epra.users.UsersFragment;
import com.example.android.scmu_epra.mn_burglaryManag.BurglaryManagementFragment;
import com.example.android.scmu_epra.mn_devices.DevicesFragment;
import com.example.android.scmu_epra.mn_history.AlarmHistoryFragment;
import com.example.android.scmu_epra.mn_home.HomeFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PostJsonData.OnStatusAvailable {


    private NavigationView navigationView;

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
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.nav_home:
                HomeFragment h = new HomeFragment();
                h.setNavigationView(navigationView);
                fragment = h;
                ft.replace(R.id.screen_area, fragment, HomeFragment.TAG);
                break;
            case R.id.nav_history:
                fragment = new AlarmHistoryFragment();
                ft.replace(R.id.screen_area, fragment, AlarmHistoryFragment.TAG);
                break;
            case R.id.nav_devices:
                fragment = new DevicesFragment();
                ft.replace(R.id.screen_area, fragment, DevicesFragment.TAG);
                break;
            case R.id.nav_burglaryManag:
                fragment = new BurglaryManagementFragment();
                ft.replace(R.id.screen_area, fragment, BurglaryManagementFragment.TAG);
                break;
            case R.id.nav_contacts:
                fragment = new UsersFragment();
                ft.replace(R.id.screen_area, fragment, UsersFragment.TAG);
                break;
            case R.id.nav_settings:
                Intent intent = new Intent(this, com.example.android.scmu_epra.SettingsActivity.class);
                startActivity(intent);
                break;
        }

        ft.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        return true;
    }

    @Override
    public void onStatusAvailable(Boolean status) {
        // TODO adicionar id ao statusAvailable para saber de onde veio
//        //if (id == whatever) {
//            if (status) {
//                Toast.makeText(this, R.string.permissions_saved, Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(this, R.string.permissions_not_saved, Toast.LENGTH_LONG).show();
//            }
//        //} else if (id == whatever) {
            if (status) {
                Toast.makeText(this, R.string.account_deleted, Toast.LENGTH_LONG).show();

                // Update fragment
                UsersFragment fragment = (UsersFragment) getSupportFragmentManager().findFragmentByTag(UsersFragment.TAG);
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(fragment);
                ft.attach(fragment);
                ft.commit();
            } else {
                Toast.makeText(this, R.string.account_delete_failed, Toast.LENGTH_LONG).show();
            }
    }

}
