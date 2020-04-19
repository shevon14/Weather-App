package com.example.dell.weatherapp;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int RequestPermissioncode = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (internet_connection()){
            EnableRuntimePermission();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage("Check your connection and\n try again.");
            dialog.setCancelable(false);
            dialog.setTitle("Alert!");
            dialog.setIcon(getResources().getDrawable(R.drawable.ic_signal_wifi_off_black_24dp));
            dialog.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (internet_connection()){
                        EnableRuntimePermission();
                    }else {
                        Toast.makeText(getApplicationContext(),"No internet available, closing the app",Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                }
            });
            dialog.show();
        }

    }

    boolean internet_connection(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    public void EnableRuntimePermission() {
       ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, RequestPermissioncode);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            FetchData fetchData = new FetchData();
            fetchData.execute("a18b978603316d47c572d98d52a420f6");
        } else{
        }
    }

    public class FetchData extends AsyncTask<String, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader bufferReader = null;
        String forecastJsonStr = null;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Connecting to Internet");
            progressDialog.setTitle("Loading Weather Data...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();


            final DateFormat datelist = new SimpleDateFormat("EEEE");
            Date date = new Date();
            Date d = new Date();

     /*         try {
                    JSONObject rootobject = new JSONObject(forecastJsonStr);
                    String msg = rootobject.getString("message");
                    if (msg.equalsIgnoreCase("Error: Not found city")) {
                        Log.e("TAG", "city not found");
                    }
                    else{
                        // Use the data
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
*/

            try {
                JSONObject rootobject = new JSONObject(forecastJsonStr);
                JSONArray listArray = rootobject.getJSONArray("list");
                final JSONObject cityname = rootobject.getJSONObject("city");
                final String name = cityname.getString("name");
                final String country = cityname.getString("country");

                final String[] day_list = new String[listArray.length()];
                final String[] temp_day = new String[listArray.length()];
                final String[] weather_des = new String[listArray.length()];
                final String[] weather_full_des = new String[listArray.length()];
                final String[] temp_min = new String[listArray.length()];
                final String[] temp_max = new String[listArray.length()];
                final String[] humidity = new String[listArray.length()];
                final String[] pressure = new String[listArray.length()];
                final String[] iconid = new String[listArray.length()];

                final Integer[] iconArray = new Integer[7];


                for (int i = 0; i < listArray.length(); i++) {
                    JSONObject dt = listArray.getJSONObject(i);
                    date.setTime((Long.parseLong(dt.getString("dt"))) * 1000);
                    day_list[0] = datelist.format(d) + " (Today)";
                    day_list[i] = datelist.format(date);

                    JSONObject tempdata = listArray.getJSONObject(i);
                    JSONObject temp = tempdata.getJSONObject("temp");
                    temp_day[i] = temp.getString("day");
                    temp_max[i] = temp.getString("max");
                    temp_min[i] = temp.getString("min");

                    JSONObject weatherdata = listArray.getJSONObject(i);
                    JSONArray weatherlist = weatherdata.getJSONArray("weather");
                    JSONObject w1 = weatherlist.getJSONObject(0);
                    weather_des[i] = w1.getString("main");
                    weather_full_des[i] = w1.getString("description");
                    iconid[i] = w1.getString("icon");

                    JSONObject pressurelist = listArray.getJSONObject(i);
                    pressure[i] = pressurelist.getString("pressure");

                    JSONObject humiditylist = listArray.getJSONObject(i);
                    humidity[i] = humiditylist.getString("humidity");


                    for (int j = 0; j < listArray.length(); j++) {
                        iconArray[j] = getResources().getIdentifier("drawable/" + "w" + iconid[j], "drawable", getPackageName());
                    }


                }


                CustomListAdapter adapter = new CustomListAdapter(MainActivity.this, day_list, temp_day, weather_des, iconArray);
                ListView list = (ListView) findViewById(R.id.list_view);
                list.setAdapter(adapter);

                final TextView CityName = (TextView) findViewById(R.id.Cityname);
                CityName.setText(String.valueOf(name) + "," + String.valueOf(country));

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent loadSecondActivity = new Intent(MainActivity.this, SecondActivity.class);
                        loadSecondActivity.putExtra("dt", day_list[position]);
                        loadSecondActivity.putExtra("day", temp_day[position]);
                        loadSecondActivity.putExtra("max", temp_max[position]);
                        loadSecondActivity.putExtra("min", temp_min[position]);
                        loadSecondActivity.putExtra("humidity", humidity[position]);
                        loadSecondActivity.putExtra("pressure", pressure[position]);
                        loadSecondActivity.putExtra("description", weather_full_des[position]);
                        loadSecondActivity.putExtra("city", name);
                        loadSecondActivity.putExtra("country", country);
                        Bundle bundle = new Bundle();
                        bundle.putInt("image", iconArray[position]);
                        loadSecondActivity.putExtras(bundle);
                        startActivity(loadSecondActivity);
                    }
                });

            } catch (JSONException je ) {
                Log.d("jasoncode", je.toString());
            }

        }

            @Override
            protected String doInBackground (String...params){
                try {
                    Bundle extras = getIntent().getExtras();
                    SharedPreferences preferences = getSharedPreferences("MyData", 0);
                    if (preferences == null) {
                        //String name = extras.getString("Unit", "metric");
                        final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Negombo&mode=json&units=metric&cnt=7";
                        final String KEY = "APPID";

                        Uri uriBuild1 = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(KEY, params[0]).build();
                        URL url1 = new URL(uriBuild1.toString());

                        urlConnection = (HttpURLConnection) url1.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.connect();

                    } else if (preferences != null) {
                        // String defaultname = "metric";
                        // String name = extras.getString("Unit",defaultname);
                        // String nameCity = extras.getString("City","Negombo");
                            String nameCity = preferences.getString("FinalCity", "Negombo");
                            String unit = preferences.getString("Unit", "metric");
                            //String name = sharedPreferences.getString("Unit","metric");

                            final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=";
                            final String BASE_URL1 = nameCity;
                            final String BASE_URL2 = "&mode=json&units=";
                            final String BASE_URL3 = unit;
                            final String BASE_URL4 = "&cnt=7";
                            final String KEY = "APPID";

                            Uri uriBuild1 = Uri.parse(BASE_URL + BASE_URL1 + BASE_URL2 + BASE_URL3 + BASE_URL4).buildUpon().appendQueryParameter(KEY, params[0]).build();
                            URL url1 = new URL(uriBuild1.toString());
                            urlConnection = (HttpURLConnection) url1.openConnection();
                            urlConnection.setRequestMethod("GET");
                            urlConnection.connect();

                    }

                        InputStream inputStream = urlConnection.getInputStream();
                        StringBuffer stringBuffer = new StringBuffer();

                    if (inputStream == null) {
                        return null;
                    }

                    bufferReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line1;
                    while ((line1 = bufferReader.readLine()) != null) {
                        stringBuffer.append(line1 + "\n");

                    }

                    if (stringBuffer.length() == 0) {
                        return null;
                    }
                    forecastJsonStr = stringBuffer.toString();
                } catch (Exception e) {
                   Log.e("Weather", "Error closing stream", e);
                }

                finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }

                    if (bufferReader != null) {
                        try {
                            bufferReader.close();
                        } catch (final IOException e) {
                            Log.e("Weather", "Error closing stream", e);
                        }

                    }

                }


                return null;
            }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }


    }

    @Override
    public void onBackPressed() {
        finishAffinity();
       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure want to Exit").setTitle("Exit");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               // MainActivity.this.finish();
                finishAffinity();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.create().show();*/

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_button, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.about:
                Intent loadAboutActivity = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(loadAboutActivity);
                return true;

            case R.id.settings:
                Intent loadSettingsActivity = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(loadSettingsActivity);
                return true;

            default:
        return super.onOptionsItemSelected(item);
    }

    }

    private void clearAppData() {
        try {
            // clearing app data
           // if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
           //     ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
           // } else {
                String packageName = getApplicationContext().getPackageName();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm clear " + packageName);
            //  }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 }








