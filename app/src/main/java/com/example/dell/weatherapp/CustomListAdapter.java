package com.example.dell.weatherapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

public class CustomListAdapter extends ArrayAdapter <String> {

    private final Activity context;
    private final String [] temperatureView;
    private final String []dateView;
    private final String [] descriptionView;
    private final Integer [] img;


    public CustomListAdapter (Activity context,String [] dateView, String[] temperatureView,String[]descriptionView,Integer[] img) {
        super(context, R.layout.list, dateView);
        this.context=context;
        this.temperatureView = temperatureView;
        this.dateView = dateView;
        this.descriptionView = descriptionView;
        this.img=img;
    }




    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list, null,true);

        TextView txttemperatureview = (TextView) rowView.findViewById(R.id.temperature_view);
        TextView txtdateview = (TextView) rowView.findViewById(R.id.day_view);
        TextView txtdescriptionview = (TextView) rowView.findViewById(R.id.discription_view);
        ImageView imgimg = (ImageView) rowView.findViewById(R.id.image_view);
        txttemperatureview.setText(temperatureView [position]+(char) 0x00B0);
        //txtdateview.setText(dateView[0]+" (Today)");
        txtdateview.setText(dateView[position]);
        txtdescriptionview.setText(descriptionView[position]);
        imgimg.setImageResource(img[position]);

        return rowView;
    };
}