package com.example.android.scmu_epra.mn_history;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.android.scmu_epra.R;
import com.example.android.scmu_epra.connection.Row;

import java.util.List;

public class AlarmHistoryListAdapter extends ArrayAdapter<Row> {

    private Context context;
    private List<Row> list;
    private TextDrawable.IBuilder builder;
    private ColorGenerator generator;

    public AlarmHistoryListAdapter(Context context, int resource, List<Row> items) {
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
            v = LayoutInflater.from(this.context).inflate(R.layout.frag_history_list_item, parent, false);
        }

        Row item = this.list.get(position);
        //AlarmHistoryItem.AlarmHistoryType type = item.getType();
        //String message = item.getMessage();
        String evento = item.getEvento();
        String date = item.getData();

        TextView messageView = v.findViewById(R.id.message);
        messageView.setText(evento);

        ImageView imageView = v.findViewById(R.id.image);
        //if (type == AlarmHistoryItem.AlarmHistoryType.AlarmTrigger) {
            Drawable d = ContextCompat.getDrawable(context, R.drawable.ic_error_outline);
            imageView.setImageDrawable(d);
        /*} else {
            char ch = message.charAt(0);
            TextDrawable textDrawable = builder.build(String.valueOf(ch), generator.getColor(ch));
            imageView.setImageDrawable(textDrawable);
        }*/

        TextView time = v.findViewById(R.id.date);
        time.setText(date);

        return v;
    }
}



