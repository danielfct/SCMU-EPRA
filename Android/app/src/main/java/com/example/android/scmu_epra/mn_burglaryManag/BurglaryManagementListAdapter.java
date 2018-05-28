package com.example.android.scmu_epra.mn_burglaryManag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.android.scmu_epra.R;

import java.util.List;

public class BurglaryManagementListAdapter extends ArrayAdapter<BurglaryManagementItem> {

    private Context context;
    private List<BurglaryManagementItem> list;
    private TextDrawable.IBuilder builder;
    private ColorGenerator generator;

    public BurglaryManagementListAdapter(Context context, int resource, List<BurglaryManagementItem> items) {
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
            v = LayoutInflater.from(this.context).inflate(R.layout.frag_burglary_manag_list_item, parent, false);
        }

        BurglaryManagementItem item = this.list.get(position);

        TextView textView = v.findViewById(R.id.division_name);
        textView.setText(item.getName());

        char ch = item.getName().charAt(0);
        TextDrawable textDrawable = builder.build(String.valueOf(ch), generator.getColor(ch));
        ImageView imageView = v.findViewById(R.id.imgLetter);
        imageView.setImageDrawable(textDrawable);

        TextView time = v.findViewById(R.id.time_text);
        time.setText(item.getTime());

        return v;
    }
}


