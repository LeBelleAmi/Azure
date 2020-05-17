package com.lebelle.azure.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lebelle.azure.R;
import com.lebelle.azure.classes.WeatherList;
import com.lebelle.azure.controllers.DetailsActivity;
import com.lebelle.azure.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Omawumi Eyekpimi on 21-Nov-17.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private Context context;
    private List<WeatherList> dailyWeatherData;

    /**
     * Contructor to initialize context and list items.
     *
     * @param applicationContext  Context of the Activity on which RecyclerView is initialised
     * @param weatherArrayList List of POJO object that contains the data to update the rows
     */

    public WeatherAdapter (Context applicationContext, List<WeatherList> weatherArrayList) {
        this.context = applicationContext;
        this.dailyWeatherData = weatherArrayList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(view);
        Typeface weatherFont = Typeface.createFromAsset(weatherViewHolder.itemView.getContext().getAssets(),"font/weather.ttf");
        weatherViewHolder.weatherIcon.setTypeface(weatherFont);
        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(final WeatherViewHolder weatherViewHolder, int i) {
        WeatherList day = dailyWeatherData.get(i);
        Date dateObject = new Date(day.getDt() * 1000L);
        String formattedTime = formatDate(dateObject);
        //Display the date of the current weather in that TextView
        weatherViewHolder.date.setText(formattedTime);

        String formattedDay = formatTime(dateObject);
        weatherViewHolder.day.setText(formattedDay);

        weatherViewHolder.temp.setText(Utils.formatTemperature(context, day.getTemp().getMax()));
      weatherViewHolder.summary.setText(Utils.capitalizeFirstLetter(day.getWeather().get(0).getDescription()));
        weatherViewHolder.minTemp.setText( "~" + Utils.formatTemperature(context, day.getTemp().getMin()));
        weatherViewHolder.weatherIcon.setText(Utils.getIconId(day.getWeather().get(0).getMain(), day.getSunrise(), day.getSunset()));
        /*Glide.with(context)
                .load(Utils.getIconId(day.getIcon()))
                .asBitmap()
                .placeholder(R.drawable.tree)
                .error(R.drawable.tree)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(weatherViewHolder.weatherIcon);
*/
       weatherViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = weatherViewHolder.getAdapterPosition();
                WeatherList items = dailyWeatherData.get(pos);
                Bundle args = new Bundle();
                args.putString("date", String.valueOf(items.getDt()));
                args.putString("tempHigh", String.valueOf(items.getTemp().getMax()));
                args.putString("tempLow", String.valueOf(items.getTemp().getMin()));
                args.putString("humidity", String.valueOf(items.getHumidity()));
                args.putString("precipProb", String.valueOf(items.getClouds()));
                args.putString("windSpeed", String.valueOf(items.getSpeed()));
                args.putString("windDirection", String.valueOf(items.getDeg()));
                args.putString("pressure", String.valueOf(items.getPressure()));
                args.putString("icon", String.valueOf(items.getWeather().get(0).getMain()));
                args.putString("sunrise", String.valueOf(items.getSunrise()));
                args.putString("sunset", String.valueOf(items.getSunset()));
                args.putString("summary", String.valueOf(items.getWeather().get(0).getDescription()));


                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("items", args);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        if (dailyWeatherData == null) {
            return 0;
        } else {
            return dailyWeatherData.size();
        }
    }

    public void setDailyWeatherData(List<WeatherList> weatherData){
        dailyWeatherData = weatherData;
        notifyDataSetChanged();
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lv;
        TextView weatherIcon;
        TextView day;
        TextView date;
        TextView temp;
        TextView summary;
        TextView minTemp;


        //initialize views of the viewholder
        public WeatherViewHolder (View itemView) {
            super(itemView);
            lv = itemView.findViewById(R.id.text_container);
            weatherIcon = itemView.findViewById(R.id.list_icon);
            day = itemView.findViewById(R.id.list_day);
            date = itemView.findViewById(R.id.list_date);
            temp = itemView.findViewById(R.id.list_temp);
            summary = itemView.findViewById(R.id.list_summary);
            minTemp = itemView.findViewById(R.id.list_temp_min);

        }
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date timeObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        return timeFormat.format(timeObject);
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
        return dateFormat.format(dateObject);
    }

}

