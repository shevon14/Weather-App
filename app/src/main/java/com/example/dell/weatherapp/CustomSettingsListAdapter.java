package com.example.dell.weatherapp;

import android.app.Activity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSettingsListAdapter extends ArrayAdapter<String>{

    private final Activity context;
    private final String [] item1;
    private final String [] subitem1;

    public CustomSettingsListAdapter (Activity context,String [] item1, String[] subitem1) {
        super(context, R.layout.settingslist, item1);
        this.context = context;
        this.item1 = item1;
        this.subitem1 = subitem1;
    }

    public View getView(int position, View view,ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowview1 = inflater.inflate(R.layout.settingslist,null,true);
        TextView txtitem = (TextView) rowview1.findViewById(R.id.txtitem);
        TextView txtsubitem = (TextView) rowview1.findViewById(R.id.txtsubitem);
        txtitem.setText(item1[position]);
        txtsubitem.setText(subitem1[position]);
        return rowview1;
    }








}
