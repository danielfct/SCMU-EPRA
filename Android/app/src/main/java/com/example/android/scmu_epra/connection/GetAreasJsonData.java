package com.example.android.scmu_epra.connection;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.scmu_epra.users.AreaItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetAreasJsonData  extends AsyncTask<String, Void, Void>
        implements GetRawData.OnDownloadComplete {

    private static final String TAG = "GetAreasJsonData";

    private List<AreaItem> mList;
    private String mBaseURL;
    private final GetAreasJsonData.OnDataAvailable mCallBack;

    public interface OnDataAvailable {
        void onDataAvailable(List<AreaItem> data, DownloadStatus status);
    }

    public GetAreasJsonData(GetAreasJsonData.OnDataAvailable callBack, String baseURL) {
        mCallBack = callBack;
        mBaseURL = baseURL;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mCallBack != null) {
            mCallBack.onDataAvailable(mList, DownloadStatus.OK);
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        String destinationUri;
        if (params.length > 0) {
            destinationUri = Uri.parse(mBaseURL).buildUpon()
                    .appendQueryParameter("search", params[0])
                    .build().toString();
        } else {
            destinationUri = Uri.parse(mBaseURL).buildUpon().toString();
        }
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        return null;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete starts. Status = " + status);

        if (status == DownloadStatus.OK) {
            mList = new ArrayList<>();

            try {
                JSONArray itemsArray = new JSONArray(data);
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonRow = itemsArray.getJSONObject(i);
                    int id = Integer.parseInt(jsonRow.getString("id"));
                    String name = jsonRow.getString("nome");
                    boolean isAlarmOn = jsonRow.getString("alarmeLigado").equals("1");
                    String sensor = jsonRow.getString("sensor");

                    AreaItem item = new AreaItem(id, name, isAlarmOn, sensor);
                    mList.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json data " + e.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        Log.d(TAG, "onDownloadComplete ends");
    }

}
