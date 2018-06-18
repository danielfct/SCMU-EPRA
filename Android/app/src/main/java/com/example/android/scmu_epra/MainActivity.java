package com.example.android.scmu_epra;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.scmu_epra.auth.LoginActivity;
import com.example.android.scmu_epra.connection.PostJsonData;
import com.example.android.scmu_epra.mn_users.UserItem;
import com.example.android.scmu_epra.mn_users.UsersFragment;
import com.example.android.scmu_epra.mn_burglaryManag.BurglaryManagementFragment;
import com.example.android.scmu_epra.mn_devices.DevicesFragment;
import com.example.android.scmu_epra.mn_history.AlarmHistoryFragment;
import com.example.android.scmu_epra.mn_home.HomeFragment;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.Map;

import static com.example.android.scmu_epra.Constants.Status.ADD_NEW_DEVICE;
import static com.example.android.scmu_epra.Constants.Status.DELETE_DEVICE;
import static com.example.android.scmu_epra.Constants.Status.DELETE_USER;
import static com.example.android.scmu_epra.Constants.Status.EDIT_USER_PERMISSIONS_DIALOG;
import static com.example.android.scmu_epra.Constants.Status.UPDATE_DEVICE;


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

        String id = getIntent().getStringExtra("id");

        if (id != null) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.screen_area);

            if (fragment == null) {
                fragment = BurglaryManagementFragment.newInstance(id);
                fm.beginTransaction().add(R.id.screen_area, fragment).commit();
            }
        } else {
            displaySelectedScreen(R.id.nav_home);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Gson gson = new Gson();
        String json = sharedPref.getString(Constants.SIGNED_ACCOUNT_TAG, "");
        UserItem currentAccount = gson.fromJson(json, UserItem.class);

        View header = navigationView.getHeaderView(0);
        TextView tName = header.findViewById(R.id.user_name_drawer);
        TextView tEmail = header.findViewById(R.id.user_email_drawer);
        tName.setText(currentAccount.getName());
        tEmail.setText(currentAccount.getEmail());
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
            /*case R.id.nav_burglaryManag:
                BurglaryManagementFragment b = new BurglaryManagementFragment();
                b.setNavigationView(navigationView);
                fragment = b;
                ft.replace(R.id.screen_area, fragment, BurglaryManagementFragment.TAG);
                break;*/
            case R.id.nav_contacts:
                fragment = new UsersFragment();
                ft.replace(R.id.screen_area, fragment, UsersFragment.TAG);
                break;
            case R.id.nav_settings:
                Intent intent = new Intent(this, com.example.android.scmu_epra.SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.remove(Constants.SIGNED_ACCOUNT_TAG);
                                editor.commit();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();
                break;
        }

        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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
    public void onStatusAvailable(Boolean status, Integer statusId) {
        Fragment fragment = null;
        View v = getWindow().getDecorView().getRootView();
        if (statusId == EDIT_USER_PERMISSIONS_DIALOG) {
            if (status) {
                Snackbar.make(v, R.string.edit_permission_saved, Snackbar.LENGTH_LONG).show();
                fragment = getSupportFragmentManager().findFragmentByTag(UsersFragment.TAG);
            } else {
                Snackbar.make(v, R.string.edit_permission_failed, Snackbar.LENGTH_LONG).show();
            }
        } else if (statusId == DELETE_USER) {
            if (status) {
                Snackbar.make(v, R.string.account_deleted, Snackbar.LENGTH_LONG).show();
                fragment = getSupportFragmentManager().findFragmentByTag(UsersFragment.TAG);
            } else {
                Snackbar.make(v, R.string.account_delete_failed, Snackbar.LENGTH_LONG).show();
            }
        } else if (statusId == ADD_NEW_DEVICE){
            if (status) {
                Snackbar.make(v, R.string.new_device_added, Snackbar.LENGTH_LONG).show();
                fragment = getSupportFragmentManager().findFragmentByTag(DevicesFragment.TAG);
            } else {
                Snackbar.make(v, R.string.new_device_add_failed, Snackbar.LENGTH_LONG).show();
            }
        } else if (statusId == DELETE_DEVICE) {
            if (status) {
                Snackbar.make(v, R.string.device_deleted, Snackbar.LENGTH_LONG).show();
                fragment = getSupportFragmentManager().findFragmentByTag(DevicesFragment.TAG);
            } else {
                Snackbar.make(v, R.string.failed_to_delete_device, Snackbar.LENGTH_LONG).show();
            }
        } else if (statusId == UPDATE_DEVICE) {
            if (status) {
                Snackbar.make(v, R.string.device_updated, Snackbar.LENGTH_LONG).show();
                fragment = getSupportFragmentManager().findFragmentByTag(DevicesFragment.TAG);
            } else {
                Snackbar.make(v, R.string.failed_to_update_device, Snackbar.LENGTH_LONG).show();
            }
        }
        if (fragment != null) {
            // Update fragment
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(fragment);
            ft.attach(fragment);
            ft.commit();
        }

    }


}
