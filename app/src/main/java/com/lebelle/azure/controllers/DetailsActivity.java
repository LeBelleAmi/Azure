package com.lebelle.azure.controllers;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.lebelle.azure.R;
import com.lebelle.azure.utilities.Utils;

import java.util.Date;

public class DetailsActivity extends AppCompatActivity {

    TextView temp1, temp2, humidity_text, precip_text, windSpeed_text, windDirection_text,
            pressure_text, summary_text, dateView,iconImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Typeface weatherFont = Typeface.createFromAsset(getAssets(),"font/weather.ttf");

        //textviews declaration
        temp1 = findViewById(R.id.weather_temp2);
        temp2 = findViewById(R.id.weather_temp3);
        humidity_text = findViewById(R.id.humidity_text);
        precip_text = findViewById(R.id.precipitation_text);
        windSpeed_text = findViewById(R.id.wind_speed_text);
        windDirection_text = findViewById(R.id.wind_direction_text);
        pressure_text = findViewById(R.id.pressure_text);
        summary_text = findViewById(R.id.weather_text2);
        dateView = findViewById(R.id.date_view);
        iconImage = findViewById(R.id.weather_image2);
        iconImage.setTypeface(weatherFont);

        //get items from the adapter intent and set in views
        Bundle data = getIntent().getBundleExtra("items");
        String date =  data.getString("date");
        String tempHigh = data.getString("tempHigh");
        String tempLow = data.getString("tempLow");
        String humidity = data.getString("humidity");
        String precipProb = data.getString("precipProb");
        String windSpeed = data.getString("windSpeed");
        String pressure = data.getString("pressure");
        String icon = data.getString("icon");
        String sunrise = data.getString("sunrise");
        String sunset = data.getString("sunset");
        String summary = data.getString("summary");
        String windDirection = data.getString("windDirection");

        //format date
        long dateTime = Long.parseLong(date);
        Date dateObject = new Date(dateTime * 1000L);
        String formattedDate = Utils.formatDate(dateObject);


        long highTemp = Math.round(Double.valueOf(tempHigh));
        long lowTemp = Math.round(Double.valueOf(tempLow));
        long humid = Math.round(Double.valueOf(humidity));
        long precip = Math.round(Double.valueOf(precipProb));
        long windSd = Math.round(Double.valueOf(windSpeed));
        long pressre = Math.round(Double.valueOf(pressure));
        long windDir = Math.round(Double.valueOf(windDirection));
        long sunrisee = data.getLong(sunrise);
        long sunsett = data.getLong(sunset);

        //setup toolbar
        Toolbar toolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        summary_text.setText(Utils.capitalizeFirstLetter(summary));
        temp1.setText(Utils.formatTemperature(getApplicationContext(), highTemp));
        temp2.setText(Utils.formatTemperature(getApplicationContext(), lowTemp));
        //humidity_text.setText(getString(R.string.format_percent, humid));
        humidity_text.setText((humid) + "%");
        pressure_text.setText(Utils.getFormattedPressure(getApplicationContext(), pressre));
        windSpeed_text.setText(Utils.getFormattedWind(getApplicationContext(), windSd));
        precip_text.setText((precip) + "%");
        //precip_text.setText(getString(R.string.format_percent, precip));
        windDirection_text.setText(Utils.getFormattedWind(getApplicationContext(), windSd, windDir));
        dateView.setText(formattedDate);
        iconImage.setText(Utils.getIconId(icon, sunrisee, sunsett));
    }


    //onCreate boolean for calender icon on appbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share1:
                shareWeatherDataIntent();
                return true;
            case R.id.settings:
                Intent intent = new Intent(DetailsActivity.this, SettingsActivity.class);
                DetailsActivity.this.startActivity(intent);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //setup weather data share button
    private Intent shareWeatherDataIntent(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String stringToShare = "Hello! The current weather data of Warri on " + dateView.getText().toString() + " is High Temp: "
                + temp1.getText().toString() + ", Low Temp: " + temp2.getText().toString() + "."+
                "  It's " + summary_text.getText().toString() + " #Azure";
        sharingIntent.putExtra(Intent.EXTRA_TEXT, stringToShare);
        startActivity(Intent.createChooser(sharingIntent, "Share Azure Weather Information via"));
        return sharingIntent;
    }

}
