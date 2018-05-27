package com.example.android.scmu_epra.mn_devices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.android.scmu_epra.R;

import java.util.ArrayList;
import java.util.List;

public class DevicesListAdapter extends ArrayAdapter<DevicesItem> implements Filterable {

    private Context context;
    private List<DevicesItem> listDevices;
    private List<DevicesItem> listOriginal;
    private TextDrawable.IBuilder builder;
    private ColorGenerator generator;
    private NameFilter nameFilter;


    public DevicesListAdapter(Context context, int resource, List<DevicesItem> items) {
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
    }

    @Override
    public int getCount() {
        return listDevices.size();
    }

    @Nullable
    @Override
    public DevicesItem getItem(int position) {
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

        DevicesItem item = this.listDevices.get(position);

        TextView textView = v.findViewById(R.id.device_name);
        textView.setText(item.getName());

        char ch = item.getName().charAt(0);
        TextDrawable textDrawable = builder.build(String.valueOf(ch), generator.getColor(ch));
        ImageView imageView = v.findViewById(R.id.device_imgLetter);
        imageView.setImageDrawable(textDrawable);

        Switch aSwitch = v.findViewById(R.id.device_switch);
        aSwitch.setChecked(item.isOn());

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
                ArrayList<DevicesItem> filterList = new ArrayList<DevicesItem>();
                for (int i = 0; i < listOriginal.size(); i++) {
                    if ((listOriginal.get(i).getName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        DevicesItem item = new DevicesItem(
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
            listDevices = (List<DevicesItem>) results.values;
            notifyDataSetChanged();
        }

    }
}
