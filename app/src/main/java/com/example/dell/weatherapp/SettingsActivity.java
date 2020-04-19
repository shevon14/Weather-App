package com.example.dell.weatherapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    String[] item_list = {"City", "Temperature Unit"};
    String[] item_sub_list = {"Select your city","Select Temperature Unit"};
    private String[] citylist = {"Negombo","Colombo","Galle","Jaffna","Kandy"};
    private AppCompatAutoCompleteTextView autoTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CustomSettingsListAdapter adapter1 = new CustomSettingsListAdapter(this, item_list, item_sub_list);
        ListView list1 = (ListView) findViewById(R.id.settingsList);
        list1.setAdapter(adapter1);

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);

                    alert.setTitle("City");
                    alert.setMessage("Please Enter a valid City name");

                    final EditText CityNameInput = new EditText(SettingsActivity.this);
                    alert.setView(CityNameInput);

                    alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                            i.putExtra("City", CityNameInput.getText().toString());

                            String cityy = CityNameInput.getText().toString();
                            if (cityy.isEmpty()){
                                Toast.makeText(getApplicationContext(),"City name can't be empty",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                                SharedPreferences preferences = getSharedPreferences("MyData", 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("FinalCity", CityNameInput.getText().toString());
                                editor.apply();
                                startActivity(i);
                            }

                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

                    alert.show();

                }

                if (position == 1) {
                    final String[] TempItems = {"Celsius", "Fahrenheit", "Kelvin"};

                    final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setSingleChoiceItems(TempItems, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Toast.makeText(getApplicationContext(), "All the units are set to Celsius", Toast.LENGTH_SHORT).show();

                                String Unitvalue = "metric";
                                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                                i.putExtra("Unit", Unitvalue);
                                startActivity(i);

                                SharedPreferences preferences = getSharedPreferences("MyData", 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("Unit", Unitvalue);
                                editor.apply();
                            }

                            if (which == 1) {
                                Toast.makeText(getApplicationContext(), "All the units are set to Fahrenheit", Toast.LENGTH_SHORT).show();

                                String Unitvalue = "imperial";
                                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                                i.putExtra("Unit", Unitvalue);
                                startActivity(i);

                                SharedPreferences preferences = getSharedPreferences("MyData", 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("Unit", Unitvalue);
                                editor.apply();

                            }

                            if (which == 2) {
                                Toast.makeText(getApplicationContext(), "All the units are set to Kelvin", Toast.LENGTH_LONG).show();

                                String Unitvalue = "";
                                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                                i.putExtra("Unit", Unitvalue);
                                startActivity(i);

                                SharedPreferences preferences = getSharedPreferences("MyData", 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("Unit", Unitvalue);
                                editor.apply();

                            }
                        }
                    });

                    builder.show();
                }
            }
        });

    }

}



