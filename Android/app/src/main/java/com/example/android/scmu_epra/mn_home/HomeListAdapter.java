package com.example.android.scmu_epra.mn_home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.android.scmu_epra.R;

import java.util.List;

public class HomeListAdapter extends ArrayAdapter<HomeItem> {

    private Context context;
    private List<HomeItem> list;
    private TextDrawable.IBuilder builder;
    private ColorGenerator generator;

    public HomeListAdapter(Context context, int resource, List<HomeItem> items) {
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
            v = LayoutInflater.from(this.context).inflate(R.layout.frag_home_list_item, parent, false);
        }

        HomeItem item = this.list.get(position);

        TextView textView = v.findViewById(R.id.divisionName);
        textView.setText(item.getName());

        char ch = item.getName().charAt(0);
        TextDrawable textDrawable = builder.build(String.valueOf(ch), generator.getColor(ch));
        ImageView imageView = v.findViewById(R.id.imgLetter);
        imageView.setImageDrawable(textDrawable);

        Switch switch1 = v.findViewById(R.id.switch1);
        switch1.setChecked(item.getAlarmeLigado() == 1);

        return v;
    }
}


