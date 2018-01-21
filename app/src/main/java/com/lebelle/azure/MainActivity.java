package com.lebelle.azure;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.lebelle.azure.api.Client;
import com.lebelle.azure.api.Service;
import com.lebelle.azure.classes.Currently;
import com.lebelle.azure.classes.Datum__;
import com.lebelle.azure.classes.WeatherData;
import com.lebelle.azure.classes.WeatherLocation;
import com.lebelle.azure.data.AppPrefs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    /*recycler view layout manager*/
    LinearLayoutManager llm;
    /*recycler view*/
    RecyclerView recyclerView;
    ProgressDialog pd;
    TextView tempView, timeView, dateView, locationView, summaryView, pressureView, windView, precipView, humidityView, appTempView, iconView;
    TextView icon1, icon2, icon3, icon4, icon5;
    List<Datum__> dailyList;
    /*network state boolean*/
    private boolean connected;
    WeatherData weatherData;
    Currently currentData;
    private WeatherAdapter mAdapter;
    CoordinatorLayout coordinatorLayout;
    //date picker ints
    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;

    double latitude;
    double longitude;

    private static boolean PREFERENCES_HAS_CHANGED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface weatherFont = Typeface.createFromAsset(getAssets(),"font/weather.ttf");

        dailyList = new ArrayList<>();

        final Calendar calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);

        //default coords
         latitude = 5.5544;
         longitude = 5.7932;

        //textviews declaration
        iconView = (TextView) findViewById(R.id.weather_image);
        tempView = (TextView) findViewById(R.id.weather_temp);
        timeView = (TextView) findViewById(R.id.weather_time);
        dateView = (TextView) findViewById(R.id.weather_date);
        summaryView = (TextView) findViewById(R.id.weather_text);
        pressureView = (TextView) findViewById(R.id.pressure_number);
        windView = (TextView) findViewById(R.id.wind_speed_number);
        precipView = (TextView) findViewById(R.id.precip_type_number);
        locationView = (TextView) findViewById(R.id.location);
        humidityView = (TextView) findViewById(R.id.humidity_number);
        appTempView = (TextView) findViewById(R.id.temp_number);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_container);

        //weathericons
        icon1 = (TextView) findViewById(R.id.icon1);
        icon2 = (TextView) findViewById(R.id.icon2);
        icon3 = (TextView) findViewById(R.id.icon3);
        icon4 = (TextView) findViewById(R.id.icon4);
        icon5 = (TextView) findViewById(R.id.icon5);


        //icon fonts
        iconView.setTypeface(weatherFont);
        icon1.setTypeface(weatherFont);
        icon2.setTypeface(weatherFont);
        icon3.setTypeface(weatherFont);
        icon4.setTypeface(weatherFont);
        icon5.setTypeface(weatherFont);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.update:
                                invalidateData();
                                loadWeatherDATA();
                                break;
                            case R.id.cities:
                                Toast.makeText(MainActivity.this, "Cities", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.settings:
                                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                                MainActivity.this.startActivity(intent);
                                break;
                        }
                        return true;
                    }
                });
        connected();
        initViews();

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
         }

    private void loadWeatherDATA() {
        try {
            pd = new ProgressDialog(this);
            pd.setMessage("Loading Weather Data...");
            pd.setCancelable(false);
            pd.show();

            WeatherLocation location = new WeatherLocation(getApplicationContext());

            if (location.getLocation() != null){

                //latitude, longitude = location.getLatitudeandLongitude();

            Service apiService =
                    Client.getClient().create(Service.class);
            Call<WeatherData> call = apiService.getCurrentWeatherData(latitude, longitude);
            call.enqueue(new Callback<WeatherData>() {
                @Override
                public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                    if (response.isSuccessful()) {
                        weatherData = response.body();
                        pd.hide();

                        List<Datum__> dailyData = weatherData.getDaily().getData();
                        mAdapter = new WeatherAdapter(getApplicationContext(), dailyData.subList(1, dailyData.size()));
                        recyclerView.smoothScrollToPosition(0);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                        currentData = weatherData.getCurrently();
                        showWeatherInfo();
                    }
                }


                @Override
                public void onFailure(Call<WeatherData> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                    //emptyState.setVisibility(View.VISIBLE);
                    pd.hide();
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
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();

                }
            });
            }else {
                com.lebelle.azure.Dialog.addLocationDialog(getApplicationContext());
            }

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void connected(){
        connected = checkInternetConnection();
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
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }else {
            loadWeatherDATA();
        }
    }

    /*
    * check if the app has network connection*/
    public boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo connectedNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isActive = (connectedNetwork != null && connectedNetwork.isConnectedOrConnecting());
        return isActive;
    }



    public void initViews() {
        // set up recycler view
        recyclerView = (RecyclerView) findViewById(R.id.list_view);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.smoothScrollToPosition(0);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    /*
    *Display developer info in textviews
    *
    */
    public void showWeatherInfo() {
        Log.e("DisplayError", "Error trying to display weather info.");
        tempView.setText(Utils.formatTemperature(getApplicationContext(),
                weatherData.getDaily().getData().get(0).getTemperatureMax()));

        Date dateObject = new Date(currentData.getTime() * 1000L);
        String formattedDate = Utils.formatDate(dateObject);
        //Display the date of the current weather in that TextView
        dateView.setText(formattedDate);

        String formattedTime = Utils.formatTime(dateObject);
        timeView.setText(formattedTime);

        appTempView.setText(Utils.formatTemperature(getApplicationContext(),
                weatherData.getDaily().getData().get(0).getTemperatureMin()));

        long precip = Math.round(currentData.getPrecipProbability());
        long pressuer = Math.round(currentData.getPressure());
        long humid = Math.round(currentData.getHumidity());
        long wind = Math.round(currentData.getWindSpeed());

        precipView.setText(getString(R.string.format_percent, precip));
        pressureView.setText(Utils.getFormattedPressure(getApplicationContext(), pressuer));
        humidityView.setText(getString(R.string.format_percent, humid));
        windView.setText(Utils.getFormattedWind(getApplicationContext(), wind));

        summaryView.setText(currentData.getSummary());
        locationView.setText(weatherData.getTimezone().substring(7));
        iconView.setText(Utils.getIconId(currentData.getIcon(),
                weatherData.getDaily().getData().get(0).getSunriseTime(),
                weatherData.getDaily().getData().get(0).getSunsetTime()));
        /*Glide.with(this)
                .load(Utils.getIconId(currentData.getIcon()))
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.weather_sunny)
                .into(iconView);
*/
    }

    //onCreate boolean for calender icon on appbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calender:
                // Associate calender configuration here
                showDatePickerDialog();
                return true;
            case R.id.share:
                shareTodayWeatherDataIntent();
                return true;
            case R.id.map:
                openMap();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDatePickerDialog() {
        showDialog(DIALOG_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if (id == DIALOG_ID)
            return  new DatePickerDialog(this, datePickerListener, year_x, month_x, day_x);
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year_x = i;
            month_x = i1 + 1;
            day_x = i2;
            Toast.makeText(MainActivity.this, year_x + "/" + month_x + "/" + day_x, Toast.LENGTH_SHORT).show();
        }
    };

    //setup weather data share button
    private Intent shareTodayWeatherDataIntent(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String stringToShare = "Hello! The current weather @ " + locationView.getText().toString() + " on " + dateView.getText().toString() + " is High Temp: "
                + tempView.getText().toString() + ", Low Temp: " + appTempView.getText().toString() + "." +
                "  It's " + summaryView.getText().toString() + "." + " #Azure";
        sharingIntent.putExtra(Intent.EXTRA_TEXT, stringToShare);
        startActivity(Intent.createChooser(sharingIntent, "Share Azure Weather Information via"));
        return sharingIntent;
    }

    private void invalidateData(){
        mAdapter.setDailyWeatherData(null);
    }

    private void openMap() {
        String addressString = AppPrefs.getPreferredWeatherLocationFromSharedPrefs("", this);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("geo")
                .path("0,0")
                .query(addressString);
        Uri addressUri = builder.build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(addressUri);
        if (intent.resolveActivity(getPackageManager()) != null)
        {startActivity(intent);}
    }

    @Override
    protected void onStart(){
        super.onStart();

        if (PREFERENCES_HAS_CHANGED){
            connected();
            PREFERENCES_HAS_CHANGED = false;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAS_CHANGED = true;
    }

}
