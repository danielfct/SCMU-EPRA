package com.example.android.scmu_epra.connection;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.android.scmu_epra.mn_users.UserItem;

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
    private ProgressDialog mProgressDialog;

    public interface OnDataAvailable {
        void onDataAvailable(List<UserItem> data, DownloadStatus status);
    }

    public GetUsersJsonData(OnDataAvailable callBack, String baseUrl) {
        this(callBack, baseUrl, null);
    }

    public GetUsersJsonData(OnDataAvailable callBack, String baseUrl, ProgressDialog progressDialog) {
        mCallBack = callBack;
        mBaseURL = baseUrl;
        mList = new ArrayList<>();
        mProgressDialog = progressDialog;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
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
                    String mobileNr = jsonRow.getString("telemovel");
                    String email = jsonRow.getString("email");
                    String password = jsonRow.getString("password");
                    boolean isAdmin = jsonRow.getString("admin").equals("1");
                    String permissionsString = jsonRow.getString("privilegios");
                    ArrayList<Integer> permissions = new ArrayList<>();
                    for (String p : permissionsString.split(",")) {
                        if (TextUtils.isDigitsOnly(p)) {
                            permissions.add(Integer.parseInt(p));
                        }
                    }

                    UserItem item = new UserItem(name, mobileNr, email, password,
                            isAdmin, permissions);
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

    @Override
    protected void onCancelled() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mProgressDialog != null)
            mProgressDialog.show();
    }
}
