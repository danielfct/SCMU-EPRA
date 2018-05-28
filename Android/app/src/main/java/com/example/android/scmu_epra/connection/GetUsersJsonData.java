package com.example.android.scmu_epra.connection;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.scmu_epra.users.UserItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetUsersJsonData extends AsyncTask<String, Void, Void>
        implements GetRawData.OnDownloadComplete {

    private static final String TAG = "GetUsersJsonData";

    private List<UserItem> mList;
    private String mBaseURL;
    private final GetUsersJsonData.OnDataAvailable mCallBack;

    public interface OnDataAvailable {
        void onDataAvailable(List<UserItem> data, DownloadStatus status);
    }

    public GetUsersJsonData(OnDataAvailable callBack, String baseURL) {
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
        Log.d(TAG, "onDownloadComplete starts. Status = " + status);

        if (status == DownloadStatus.OK) {
            try {
                JSONArray itemsArray = new JSONArray(data);

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonRow = itemsArray.getJSONObject(i);
                    String name = jsonRow.getString("nome");
                    String email = jsonRow.getString("email");
                    String permissionsString = jsonRow.getString("privilegios");
                    ArrayList<Integer> permissions = new ArrayList<>();
                    for (String p : permissionsString.split(",")) {
                        permissions.add(Integer.parseInt(p));
                    }
                    boolean isAdmin = jsonRow.getString("admin").equals("1");

                    UserItem item = new UserItem(name, email, isAdmin, permissions);
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
