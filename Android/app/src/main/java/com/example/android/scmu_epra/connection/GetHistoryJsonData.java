package com.example.android.scmu_epra.connection;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetHistoryJsonData extends AsyncTask<String, Void, List<Row>>
        implements GetRawData.OnDownloadComplete {

    private static final String TAG = "GetHistoryJsonData";

    private List<Row> mRowList;
    private String mBaseURL;

    private final OnDataAvailable mCallBack;

    public interface OnDataAvailable {
        void onDataAvailable(List<Row> data, DownloadStatus status);
    }

    public GetHistoryJsonData(OnDataAvailable callBack, String baseURL) {
        Log.d(TAG, "GetJsonData called");
        mBaseURL = baseURL;
        mCallBack = callBack;
        mRowList = new ArrayList<>();
    }

    @Override
    protected void onPostExecute(List<Row> rows) {
        Log.d(TAG, "onPostExecute starts");

        if(mCallBack != null) {
            mCallBack.onDataAvailable(mRowList, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute ends");
    }

    @Override
    protected List<Row> doInBackground(String... params) {
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

        if (status == DownloadStatus.OK) {
            try {
                JSONArray itemsArray = new JSONArray(data);

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonRow = itemsArray.getJSONObject(i);
                    String id = jsonRow.getString("id");
                    String evento = jsonRow.getString("evento");
                    String datahora = jsonRow.getString("datahora");

                    Row rowObject = new Row(Integer.parseInt(id), evento, datahora);
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
