package com.example.android.scmu_epra.connection;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.scmu_epra.mn_home.HomeItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetSimulatorJsonData extends AsyncTask<String, Void, List<HomeItem>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetSimulatorJsonData";

    private List<HomeItem> mRowList = null;
    private String mBaseURL;

    private final OnDataAvailable mCallBack;

    public interface OnDataAvailable {
        void onDataAvailable(List<HomeItem> data, DownloadStatus status);
    }

    public GetSimulatorJsonData(OnDataAvailable callBack, String baseURL) {
        Log.d(TAG, "GetJsonSimulatorData called");
        mBaseURL = baseURL;
        mCallBack = callBack;
    }

    @Override
    protected void onPostExecute(List<HomeItem> rows) {
        Log.d(TAG, "onPostExecute starts");

        if(mCallBack != null) {
            mCallBack.onDataAvailable(mRowList, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute ends");
    }

    @Override
    protected List<HomeItem> doInBackground(String... params) {
        Log.d(TAG, "doInBackground starts");
        String destinationUri = createUri(params[0]);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground ends");
        return mRowList;
    }

    private String createUri(String searchCriteria) {
        Log.d(TAG, "createUri starts");

        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("test", searchCriteria)
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete starts. Status = " + status);

        if(status == DownloadStatus.OK) {
            mRowList = new ArrayList<>();

            try {
                JSONArray itemsArray = new JSONArray(data);

                for(int i=0; i<itemsArray.length(); i++) {
                    JSONObject jsonRow = itemsArray.getJSONObject(i);
                    String id = jsonRow.getString("id");
                    String nome = jsonRow.getString("nome");
                    String estadoAtual = jsonRow.getString("estadoAtual");
                    String areaId = jsonRow.getString("areaId");

                    HomeItem rowObject = new HomeItem(Integer.parseInt(id), nome, Integer.parseInt(estadoAtual), Integer.parseInt(areaId));
                    mRowList.add(rowObject);

                    Log.d(TAG, "onDownloadComplete " + rowObject.toString());
                }
            } catch(JSONException jsone) {
                jsone.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json data " + jsone.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        Log.d(TAG, "onDownloadComplete ends");
    }
}
