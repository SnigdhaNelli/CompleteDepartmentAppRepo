package com.example.mycseapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sri on 10/8/17.
 */
public class chatadapter extends ArrayAdapter<Item> {
    private ArrayList<Item> dataSet;
    Context mContext;
    public chatadapter(Context context, ArrayList<Item> itemsArrayList) {

        super(context, R.layout.list_view, itemsArrayList);

        this.mContext = context;
        this.dataSet = itemsArrayList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.list_view, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.name);
        TextView valueView = (TextView) rowView.findViewById(R.id.message);

        // 4. Set the text for textView
        labelView.setText(dataSet.get(position).getName());
        valueView.setText(dataSet.get(position).getMessage());
        rowView.setBackgroundColor(Color.argb(200,200,200,200));
        // 5. retrn rowView
        return rowView;
    }
}
