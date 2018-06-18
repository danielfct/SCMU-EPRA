package com.example.android.scmu_epra.connection;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class PostJsonData extends AsyncTask<String,Void, Boolean> {

    private static final String TAG = "PostJsonData";

    private String mBaseURL;
    private final OnStatusAvailable mCallBack;
    private final int statusId;

    public interface OnStatusAvailable {
        void onStatusAvailable(Boolean status, Integer statusId);
    }

    public PostJsonData(OnStatusAvailable mCallBack, String baseURL, int statusId) {
        Log.d(TAG, "PostJsonData: called");
        this.mBaseURL = baseURL;
        this.mCallBack = mCallBack;
        this.statusId = statusId;
    }

    @Override
    protected void onPostExecute(Boolean status) {
        Log.d(TAG, "onPostExecute starts");

        if (mCallBack != null) {
            mCallBack.onStatusAvailable(status, statusId);
        }
        Log.d(TAG, "onPostExecute ends");
    }

    @Override
    public Boolean doInBackground(String... strings) {
        HttpURLConnection connection = null;

        if(strings == null || strings.length == 0) {
            return false;
        }

        try {
            URL url = new URL(mBaseURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept","application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            JSONObject jsonParam = new JSONObject();

            for (String param : strings) {
                String[] paramSplit = param.split("=");
                if (paramSplit.length >= 2) {
                    jsonParam.put(paramSplit[0], paramSplit[1]);
                } else {
                    jsonParam.put(paramSplit[0], "");
                }
            }

            Log.d(TAG, "doInBackground: Json generated: " + jsonParam.toString());
            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            int responseCode = connection.getResponseCode();
            String responseMsg = connection.getResponseMessage();

            DataInputStream is = new DataInputStream(connection.getInputStream());
            byte[] d = new byte[500]; //TODO apagar
            is.read(d);

            Log.d(TAG, "doInBackground: Got message = " + new String(d).trim());
            Log.d(TAG, "doInBackground: Got responseCode = " + responseCode);
            Log.d(TAG, "doInBackground: Got responseMsg = " + responseMsg);

            return responseCode == HttpURLConnection.HTTP_OK;

        } catch (MalformedURLException e) {
            Log.e(TAG, "post doInBackground: Invalid URL " + e.getMessage() );
        } catch (IOException e) {
            Log.e(TAG, "post doInBackground: IO Exception: " + e.getMessage() );
        } catch(SecurityException e) {
            Log.e(TAG, "post doInBackground: Security Exception. Needs permission? " + e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, "post doInBackground: Json Exception: " + e.getMessage());;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }

        return false;
    }
}
