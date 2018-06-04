package com.example.android.scmu_epra.connection;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.scmu_epra.mn_burglaryManag.BurglaryManagementItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetBurglaryHistoryJsonData extends AsyncTask<String, Void, Void>
        implements GetRawData.OnDownloadComplete {

    private static final String TAG = "GetBuglHistoryJsonData";

    private List<BurglaryManagementItem> mList;
    private String mBaseURL;
    private final OnDataAvailable mCallBack;

    public interface OnDataAvailable {
        void onDataAvailable(List<BurglaryManagementItem> data, DownloadStatus status);
    }

    public GetBurglaryHistoryJsonData(OnDataAvailable callBack, String baseURL) {
        mCallBack = callBack;
        mBaseURL = baseURL;
        mList = new ArrayList<>();
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
        if (status == DownloadStatus.OK) {
            try {
                JSONArray itemsArray = new JSONArray(data);
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonRow = itemsArray.getJSONObject(i);
                    String area = jsonRow.getString("area");
                    int duracao = Integer.valueOf(jsonRow.getString("duracao"));
                    BurglaryManagementItem item = new BurglaryManagementItem(area, duracao);
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
