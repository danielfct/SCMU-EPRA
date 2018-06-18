package com.example.android.scmu_epra.mn_devices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.android.scmu_epra.Constants;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.PostJsonData;

import java.util.ArrayList;
import java.util.List;

public class DevicesListAdapter extends ArrayAdapter<DeviceItem>
        implements Filterable, PostJsonData.OnStatusAvailable {

    private Context context;
    private List<DeviceItem> listDevices;
    private List<DeviceItem> listOriginal;
    private TextDrawable.IBuilder builder;
    private ColorGenerator generator;
    private NameFilter nameFilter;
    private View devicesView;


    public DevicesListAdapter(Context context, int resource, List<DeviceItem> items, View v) {
        super(context, 0, items);
        this.context = context;
        this.listDevices = items;
        this.builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .round();
        this.generator = ColorGenerator.MATERIAL;
        this.listOriginal = items;
        this.devicesView = v;
    }

    @Override
    public int getCount() {
        return listDevices.size();
    }

    @Nullable
    @Override
    public DeviceItem getItem(int position) {
        return listDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(this.context).inflate(R.layout.frag_devices_list_item, parent, false);
        }

        DeviceItem item = this.listDevices.get(position);

        TextView textView = v.findViewById(R.id.device_name);
        textView.setText(item.getName());

        char ch = item.getName().charAt(0);
        TextDrawable textDrawable = builder.build(String.valueOf(ch), generator.getColor(ch));
        ImageView imageView = v.findViewById(R.id.device_imgLetter);
        imageView.setImageDrawable(textDrawable);

        Switch aSwitch = v.findViewById(R.id.device_switch);
        aSwitch.setChecked(item.isOn());
        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            executePostJson("https://test966996.000webhostapp.com/api/update_devices.php",
                    "nome=" + item.getName(),
                    "ligado=" + (isChecked ? "1" : "0"));
        });
        return v;
    }

    private final void executePostJson(String url, String... params) {
        PostJsonData postJsonData = new PostJsonData(this, url, Constants.Status.DEVICES_FRAGMENT);
        postJsonData.execute(params);
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
                ArrayList<DeviceItem> filterList = new ArrayList<DeviceItem>();
                for (int i = 0; i < listOriginal.size(); i++) {
                    if ((listOriginal.get(i).getName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        DeviceItem item = new DeviceItem(
                                listOriginal.get(i).getName(),
                                listOriginal.get(i).getType(),
                                listOriginal.get(i).isOn());
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
            listDevices = (List<DeviceItem>) results.values;
            notifyDataSetChanged();
        }

    }

    @Override
    public void onStatusAvailable(Boolean status, Integer statusId) {
        if (status) {
            Snackbar.make(devicesView, "Device status changed.", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(devicesView, "Unable to connect to the server.", Snackbar.LENGTH_SHORT).show();
        }
    }

}
