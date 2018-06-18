package com.example.android.scmu_epra.mn_history;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.Row;

import java.util.ArrayList;
import java.util.List;

public class AlarmHistoryListAdapter extends ArrayAdapter<Row> implements Filterable {

    private Context context;
    private List<Row> listOriginal;
    private List<Row> listRow;
    private TextDrawable.IBuilder builder;
    private ColorGenerator generator;
    private DescriptionFilter descriptionFilter;


    public AlarmHistoryListAdapter(Context context, int resource, List<Row> items) {
        super(context, 0, items);
        this.context = context;
        this.listRow = items;
        this.listOriginal = items;
        this.builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .round();
        this.generator = ColorGenerator.MATERIAL;
    }

    @Override
    public int getCount() {
        return listRow.size();
    }

    @Nullable
    @Override
    public Row getItem(int position) {
        return listRow.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(this.context).inflate(R.layout.frag_history_list_item, parent, false);
        }

        Row item = this.listRow.get(position);
        String evento = item.getEvento();
        String date = item.getData();

        TextView messageView = v.findViewById(R.id.message);
        messageView.setText(evento);

        ImageView imageView = v.findViewById(R.id.image);
        Drawable d;
        if (evento.contains("intrusion was detected")){
            d = ContextCompat.getDrawable(context, R.drawable.ic_error_outline);
        }
        else if (evento.contains("turned the alarm")) {
            d = ContextCompat.getDrawable(context, R.drawable.ic_menu_home);
        }
        else if (evento.contains(" simulated ")){
            d = ContextCompat.getDrawable(context, R.drawable.ic_simulation);
        }
        else if (evento.contains("called the Police")) {
            d = ContextCompat.getDrawable(context, R.drawable.ic_call);
        }
        else {
            d = ContextCompat.getDrawable(context, R.drawable.ic_history);
        }
        imageView.setImageDrawable(d);


        TextView time = v.findViewById(R.id.date);
        time.setText(date);

        return v;
    }


    @Override
    public Filter getFilter() {
        if (descriptionFilter == null) {
            descriptionFilter = new DescriptionFilter();
        }
        return descriptionFilter;
    }


    private class DescriptionFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<Row> filterList = new ArrayList<Row>();
                for (int i = 0; i < listOriginal.size(); i++) {
                    if ((listOriginal.get(i).getEvento().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        Row row = new Row(
                                listOriginal.get(i).getId(),
                                listOriginal.get(i).getEvento(),
                                listOriginal.get(i).getData());

                        filterList.add(row);
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
            listRow = (List<Row>) results.values;
            notifyDataSetChanged();
        }

    }

}



