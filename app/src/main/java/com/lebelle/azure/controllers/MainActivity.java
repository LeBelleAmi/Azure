package com.lebelle.azure.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lebelle.azure.R;
import com.lebelle.azure.adapters.WeatherAdapter;
import com.lebelle.azure.api.Client;
import com.lebelle.azure.api.Service;
import com.lebelle.azure.classes.RootData;
import com.lebelle.azure.classes.WeatherList;
import com.lebelle.azure.data.AppPrefs;
import com.lebelle.azure.url.Url;
import com.lebelle.azure.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    /*recycler view layout manager*/
    LinearLayoutManager llm;
    /*recycler view*/
    RecyclerView recyclerView;
    RelativeLayout weatherView, emptyView;
    ProgressBar progressBar;
    TextView tempView, timeView, dateView,locationView2, summaryView, pressureView,
            windView, precipView, humidityView, appTempView, iconView, locationPin;
    TextView icon1, icon2, icon3, icon4, icon5;
    RootData rootData;
    List<WeatherList> dailyList;
    /*network state boolean*/
    private boolean connected;
    private WeatherAdapter mAdapter;
    CoordinatorLayout coordinatorLayout;
    String locationQuery;

    private static boolean PREFERENCES_HAS_CHANGED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationQuery = AppPrefs
                .getPreferredWeatherLocation(MainActivity.this);


        Typeface weatherFont = Typeface.createFromAsset(getAssets(),"font/weather.ttf");

        dailyList = new ArrayList<>();

        //textviews declaration
        iconView =  findViewById(R.id.weather_image);
        tempView =  findViewById(R.id.weather_temp);
        timeView =  findViewById(R.id.weather_time);
        dateView =  findViewById(R.id.weather_date);
        summaryView =  findViewById(R.id.weather_text);
        pressureView =  findViewById(R.id.pressure_number);
        windView = findViewById(R.id.wind_speed_number);
        precipView =  findViewById(R.id.precip_type_number);
        humidityView = findViewById(R.id.humidity_number);
        appTempView = findViewById(R.id.temp_number);
        coordinatorLayout = findViewById(R.id.main_container);
        locationView2 = findViewById(R.id.location1);
        locationPin = findViewById(R.id.locationPin);

        //weathericons
        icon1 =  findViewById(R.id.icon1);
        icon2 =  findViewById(R.id.icon2);
        icon3 = findViewById(R.id.icon3);
        icon4 = findViewById(R.id.icon4);
        icon5 = findViewById(R.id.icon5);

        //otherViews
        weatherView = findViewById(R.id.weatherView);
        emptyView = findViewById(R.id.emptyView);
        progressBar = findViewById(R.id.progressView);

        //icon fonts
        iconView.setTypeface(weatherFont);
        icon1.setTypeface(weatherFont);
        icon2.setTypeface(weatherFont);
        icon3.setTypeface(weatherFont);
        icon4.setTypeface(weatherFont);
        icon5.setTypeface(weatherFont);
        locationPin.setTypeface(weatherFont);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // set up recycler view
        recyclerView = findViewById(R.id.list_view);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.smoothScrollToPosition(0);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //bottom navigation
        BottomNavigationView bottomNavigationView =
                findViewById(R.id.bottom_navigation);

        setupSharedPreferences();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.update:
                                invalidateData();
                                loadWeatherDATA();
                                break;
                            case R.id.cities:
                                Intent intentCity = new Intent(MainActivity.this, CitiesActivity.class);
                                MainActivity.this.startActivity(intentCity);
                                break;
                            case R.id.share:
                                 shareTodayWeatherDataIntent();
                                return true;
                            case R.id.settings:
                                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                                MainActivity.this.startActivity(intent);
                                break;
                        }
                        return true;
                    }
                });
        connected();

       // PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
         }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }



    private void loadWeatherDATA() {
        try {
            Service apiService =
                    Client.getClient().create(Service.class);
            Call<RootData> call = apiService.getCurrentWeatherData(locationQuery, Url.UNITS, Url.COUNT, Url.LANG, Url.API_KEY);
            call.enqueue(new Callback<RootData>() {
                @Override
                public void onResponse(Call<RootData> call, Response<RootData> response) {
                    if (response.isSuccessful()) {
                        emptyView.setVisibility(View.GONE);
                        weatherView.setVisibility(View.VISIBLE);
                        rootData = response.body();
                        //Toast.makeText(MainActivity.this,"Report: " + response.body(), Toast.LENGTH_LONG).show();

                       dailyList = rootData.getWeatherLists();
                        mAdapter = new WeatherAdapter(getApplicationContext(), dailyList);
                        recyclerView.smoothScrollToPosition(0);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        showWeatherInfo();
                    }
                }


                @Override
                public void onFailure(Call<RootData> call, Throwable t) {
                    Log.d("Error: ", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    emptyView.setVisibility(View.VISIBLE);
                    // showing snack bar with response failure option
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Weather data loading failed, please refresh!", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("REFRESH", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // refresh is selected, refresh the app
                            loadWeatherDATA();
                        }
                    });
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    snackbar.show();
                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void connected(){
        connected = isNetworkConnected(getApplicationContext());
        if (!connected){
            // showing snack bar with network option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "You seem to be Offline, please check connection!", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // retry is selected, refresh the app
                    loadWeatherDATA();
                }
            });
            snackbar.setActionTextColor(Color.WHITE);

            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            snackbar.show();
        }else {
            loadWeatherDATA();
        }
    }


    public static boolean isNetworkConnected(Context context) {
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = true;
                    }
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        result = true;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }



    /*
    *Display developer info in textviews
    *
    */
    public void showWeatherInfo() {
        Log.e("DisplayError", "Error trying to display weather info.");

        tempView.setText(Utils.formatTemperature(getApplicationContext(),
                dailyList.get(0).getTemp().getMax()));

        Date dateObject = new Date(dailyList.get(0).getDt() * 1000L);
        String formattedDate = Utils.formatDate(dateObject);
        //Display the date of the current weather in that TextView
        dateView.setText(formattedDate);

        //String formattedTime = Utils.formatTime(dateObject);
        //timeView.setText(formattedTime);

        //set up system calender and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm a", Locale.getDefault());
        String time = dateFormat.format(Calendar.getInstance().getTime());
        timeView.setText(time);


        appTempView.setText(Utils.formatTemperature(getApplicationContext(),
                dailyList.get(0).getTemp().getMin()));

        long precip = Math.round(dailyList.get(0).getClouds());
        long pressuer = Math.round(dailyList.get(0).getPressure());
        long humid = Math.round(dailyList.get(0).getHumidity());
        long wind = Math.round(dailyList.get(0).getSpeed());

        //precipView.setText(String.format(R.string.format_percent, precip));
        precipView.setText(precip + "%");
        pressureView.setText(Utils.getFormattedPressure(getApplicationContext(), pressuer));
        humidityView.setText(humid + "%");
        //humidityView.setText(String.format(getString(R.string.format_percent), humid));
        //humidityView.setText(String.format("%.0f%%", humid * 100));
        windView.setText(Utils.getFormattedWind(getApplicationContext(), wind));

        summaryView.setText(Utils.capitalizeFirstLetter(dailyList.get(0).getWeather().get(0).getDescription()));
        //locationView.setText(weatherData.getTimezone().substring(7));
        locationView2.setText(rootData.getCity().getName());
        iconView.setText(Utils.getIconId(dailyList.get(0).getWeather().get(0).getMain(),
                dailyList.get(0).getSunrise(),
                dailyList.get(0).getSunset()));


        //change app background according to sunrise and sunset
        CoordinatorLayout coordinatorLayout = findViewById(R.id.main_container);
        long currentTime = new Date().getTime();
        double sunrise = dailyList.get(0).getSunrise();
        double sunset = dailyList.get(0).getSunset();

        //Toast.makeText(getApplicationContext(), "current time " + currentTime + " sunrise " + sunrise + "sunset " + sunset, Toast.LENGTH_LONG).show();

        if(currentTime>=sunrise && currentTime<sunset) {
            Drawable drawable = getResources().getDrawable(R.drawable.sun);
            coordinatorLayout.setBackground(drawable);
        }else{
            Drawable drawable = getResources().getDrawable(R.drawable.moon);
            coordinatorLayout.setBackground(drawable);
        }

    }

    //setup weather data share button
    private Intent shareTodayWeatherDataIntent(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String stringToShare = "Hello! The current weather data @ " + locationView2.getText().toString() + " on " + dateView.getText().toString() + " is High Temp: "
                + tempView.getText().toString() + ", Low Temp: " + appTempView.getText().toString() +
                "  with signs of " + summaryView.getText().toString() + "." + " #Azure";
        sharingIntent.putExtra(Intent.EXTRA_TEXT, stringToShare);
        startActivity(Intent.createChooser(sharingIntent, "Share Azure Weather Information via"));
        return sharingIntent;
    }

    private void invalidateData(){
        mAdapter.setDailyWeatherData(null);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAS_CHANGED = true;
    }

    @Override
    protected void onStart(){
        super.onStart();

        if (PREFERENCES_HAS_CHANGED){
            connected();
            PREFERENCES_HAS_CHANGED = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

}
