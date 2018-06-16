package com.example.android.scmu_epra.services;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.MainActivity;
import com.example.android.scmu_epra.connection.PostJsonData;
import com.example.android.scmu_epra.mn_users.UserItem;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService implements PostJsonData.OnStatusAvailable {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        sendNewTokenToServer(token);

        /*SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MyFirebaseInstanceIdService.this);
        String user = sharedPref.getString(Constants.SIGNED_ACCOUNT_TAG, null);
        Gson gson = new GsonBuilder().create();
        UserItem userItem = gson.fromJson(user, UserItem.class);
        PostJsonData postJsonData = new PostJsonData(this, "https://test966996.000webhostapp.com/api/register_token.php");
        postJsonData.execute("email="+userItem.getEmail(),"token="+token);*/
    }

    private void sendNewTokenToServer(String token) {
        Log.d("TOKEN", token);
    }

    @Override
    public void onStatusAvailable(Boolean status) {

    }
}
