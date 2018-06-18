package com.example.android.scmu_epra.mn_users;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.MainActivity;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.PostJsonData;
import com.example.android.scmu_epra.mn_devices.DeviceItem;
import com.example.android.scmu_epra.mn_devices.DevicesListAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

public class UsersListAdapter extends ArrayAdapter<UserItem> implements Filterable {

    private Context context;
    private List<UserItem> listUsers;
    private List<UserItem> listOriginal;
    private TextDrawable.IBuilder builder;
    private ColorGenerator generator;
    private NameFilter nameFilter;

    public UsersListAdapter(Context context, int resource, List<UserItem> items) {
        super(context, 0, items);
        this.context = context;
        this.listUsers = items;
        this.builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .round();
        this.generator = ColorGenerator.MATERIAL;
        this.listOriginal = items;
    }

    @Override
    public int getCount() {
        return listUsers.size();
    }

    @Nullable
    @Override
    public UserItem getItem(int position) {
        return listUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(this.context).inflate(R.layout.frag_users_list_item, parent, false);
        }

        UserItem user = this.listUsers.get(position);
        String name = user.getName();

        ImageView imageView = v.findViewById(R.id.image);
        char ch = name.charAt(0);
        TextDrawable textDrawable = builder.build(String.valueOf(ch), generator.getColor(ch));
        imageView.setImageDrawable(textDrawable);

        TextView nameView = v.findViewById(R.id.name);
        nameView.setText(name);

        AppCompatImageView isAdminImage = v.findViewById(R.id.image_is_admin);
        isAdminImage.setVisibility(user.isAdmin() ? View.VISIBLE : View.INVISIBLE);

        return v;
    }

    @Override
    public Filter getFilter() {
        if (nameFilter == null) {
            nameFilter = new NameFilter();
        }
        return nameFilter;
    }

    private class NameFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<UserItem> filterList = new ArrayList<>();
                for (int i = 0; i < listOriginal.size(); i++) {
                    UserItem original = listOriginal.get(i);

                    if ((original.getName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        UserItem item = new UserItem(
                                original.getName(),
                                original.getMobileNr(),
                                original.getEmail(),
                                original.getPassword(),
                                original.isAdmin(),
                                original.getPermissions());
                        filterList.add(item);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = listOriginal.size();
                results.values = listOriginal;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            listUsers = (List<UserItem>) results.values;
            notifyDataSetChanged();
        }

    }

}
