package com.example.mycseapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sri on 10/14/17.
 */
public class EventAdapter extends ArrayAdapter<Eventclass> {
  //  private ArrayList<Item> dataSet;
    private ArrayList<Eventclass> Image;
    Context mContext;

    public EventAdapter(Context context,  ArrayList<Eventclass> t ) {

        super(context, R.layout.events, t );
        this.Image = t;
        this.mContext = context;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.events_row, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.name);
         TextView valueView = (TextView) rowView.findViewById(R.id.post_info);
        ImageView I = (ImageView) rowView.findViewById(R.id.imageView);
        labelView.setText(Image.get(position).s ) ;

        // 4. Set the text for textView
//        labelView.setText(dataSet.get(position).getName());
//        valueView.setText(dataSet.get(position).getMessage());
        valueView.setText(Image.get(position).info + " posted a " + Image.get(position).type + " post");
        if(Image.get(position).type.equals("culture"))
        {
            I.setImageResource(R.drawable.culture);
        }
        else I.setImageResource(R.drawable.sports);
        rowView.setBackgroundColor(
                Color.argb(255, 255, 0, 255)) ;

        // 5. retrn rowView
        return rowView;
    }
}
