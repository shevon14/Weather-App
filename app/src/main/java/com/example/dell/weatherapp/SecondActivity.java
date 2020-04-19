package com.example.dell.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        Intent fromFirstActivity = getIntent();
        fromFirstActivity.getStringExtra("dt");
        fromFirstActivity.getStringExtra("day");
        fromFirstActivity.getStringExtra("description");

        TextView txtdayView = (TextView) findViewById(R.id.day_view2);
        txtdayView.setText(fromFirstActivity.getStringExtra("dt"));

        TextView txtTempDay = (TextView) findViewById(R.id.temperature_view2);
        txtTempDay.setText(fromFirstActivity.getStringExtra("day")+(char) 0x00B0);

        TextView txtdescriptionView =(TextView) findViewById(R.id.discription_view2);
        txtdescriptionView.setText(fromFirstActivity.getStringExtra("description"));

        TextView txthumidity = (TextView) findViewById(R.id.humidity);
        txthumidity.setText("Humidity: "+fromFirstActivity.getStringExtra("humidity")+"%");

        TextView txtpressure = (TextView) findViewById(R.id.pressure);
        txtpressure.setText("Pressure: "+fromFirstActivity.getStringExtra("pressure")+"Pa");

        TextView txtTempMin = (TextView) findViewById(R.id.min);
        txtTempMin.setText("Min: "+fromFirstActivity.getStringExtra("min")+(char) 0x00B0);

        TextView txtTempMax = (TextView) findViewById(R.id.Max);
        txtTempMax.setText("Max: "+fromFirstActivity.getStringExtra("max")+(char) 0x00B0);

        TextView txtCityName = (TextView) findViewById(R.id.Cityname2);
        txtCityName.setText(fromFirstActivity.getStringExtra("city")+","+fromFirstActivity.getStringExtra("country"));

        ImageView v =(ImageView)findViewById(R.id.icon2);
        Bundle bundle=this.getIntent().getExtras();
        int pic=bundle.getInt("image");
        v.setImageResource(pic);


    }
}
