package com.example.android.scmu_epra.mn_users;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.MainActivity;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.PostJsonData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class UsersListAdapter extends ArrayAdapter<UserItem> {

    private Context context;
    private List<UserItem> list;
    private TextDrawable.IBuilder builder;
    private ColorGenerator generator;

    public UsersListAdapter(Context context, int resource, List<UserItem> items) {
        super(context, 0, items);
        this.context = context;
        this.list = items;
        this.builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .round();
        this.generator = ColorGenerator.MATERIAL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(this.context).inflate(R.layout.frag_users_list_item, parent, false);
        }

        UserItem item = this.list.get(position);
        String name = item.getName();
        ArrayList<Integer> permissions = item.getPermissions();
        boolean isAdmin = item.isAdmin();

        ImageView imageView = v.findViewById(R.id.image);
        char ch = name.charAt(0);
        TextDrawable textDrawable = builder.build(String.valueOf(ch), generator.getColor(ch));
        imageView.setImageDrawable(textDrawable);

        TextView nameView = v.findViewById(R.id.name);
        nameView.setText(name);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPref.getString(Constants.SIGNED_ACCOUNT_TAG, "");
        UserItem currentAccount = gson.fromJson(json, UserItem.class);

        AppCompatImageButton editPermissionsButton = v.findViewById(R.id.edit_permissions_button);
        editPermissionsButton.setOnClickListener((view) -> showEditPermissionsDialog(permissions));
        editPermissionsButton.setVisibility(currentAccount != null && currentAccount.isAdmin() ? View.VISIBLE : View.INVISIBLE);

        AppCompatImageButton deleteUserAccountButton = v.findViewById(R.id.delete_user_account_button);
        deleteUserAccountButton.setOnClickListener((view) -> showDeleteUserConfirmation(item));
        deleteUserAccountButton.setVisibility(currentAccount != null && currentAccount.isAdmin() ? View.VISIBLE : View.INVISIBLE);

        return v;
    }

    private void showEditPermissionsDialog(ArrayList<Integer> permissions) {
        FragmentManager fm = ((Activity)context).getFragmentManager();
        EditUserPermissionsDialog dialog = EditUserPermissionsDialog.newInstance(permissions);
        dialog.show(fm, "fragment_edit_permissions");
    }

    private void showDeleteUserConfirmation(UserItem user) {
        new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_delete_forever_red)
                .setTitle(context.getString(R.string.delete_user_confirmation_title))
                .setMessage(context.getString(R.string.delete_user_confirmation, user.getName()))
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> removeUser(user))
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void removeUser(UserItem user) {
        PostJsonData postJsonData = new PostJsonData((MainActivity) context,
                "https://test966996.000webhostapp.com/api/delete_contacts.php");
        postJsonData.execute("email=" + user.getEmail());
    }

}
