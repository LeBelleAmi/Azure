package com.lebelle.azure;

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

import com.lebelle.azure.classes.Datum__;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Omawumi Eyekpimi on 21-Nov-17.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private Context context;
    private List<Datum__> dailyWeatherData;

    /**
     * Contructor to initialize context and list items.
     *
     * @param applicationContext  Context of the Activity on which RecyclerView is initialised
     * @param weatherArrayList List of POJO object that contains the data to update the rows
     */

    public WeatherAdapter (Context applicationContext, List<Datum__> weatherArrayList) {
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
        Datum__ day = dailyWeatherData.get(i);
        Date dateObject = new Date(day.getTime() * 1000L);
        String formattedTime = formatDate(dateObject);
        //Display the date of the current weather in that TextView
        weatherViewHolder.date.setText(formattedTime);

        String formattedDay = formatTime(dateObject);
        weatherViewHolder.day.setText(formattedDay);

        weatherViewHolder.temp.setText(Utils.formatTemperature(context, day.getTemperatureHigh()));
        weatherViewHolder.summary.setText(day.getSummary());
        weatherViewHolder.minTemp.setText( "~" + Utils.formatTemperature(context, day.getTemperatureLow()));
        weatherViewHolder.weatherIcon.setText(Utils.getIconId(day.getIcon(), day.getSunriseTime(), day.getSunsetTime()));
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
                Datum__ items = dailyWeatherData.get(pos);
                Bundle args = new Bundle();
                args.putString("date", dailyWeatherData.get(pos).getTime().toString());
                args.putString("tempHigh", dailyWeatherData.get(pos).getTemperatureHigh().toString());
                args.putString("tempLow", dailyWeatherData.get(pos).getTemperatureLow().toString());
                args.putString("humidity", dailyWeatherData.get(pos).getHumidity().toString());
                args.putString("precipProb", dailyWeatherData.get(pos).getPrecipProbability().toString());
                args.putString("windSpeed", dailyWeatherData.get(pos).getWindSpeed().toString());
                args.putString("windDirection", dailyWeatherData.get(pos).getWindBearing().toString());
                args.putString("pressure", dailyWeatherData.get(pos).getPressure().toString());
                args.putString("icon", dailyWeatherData.get(pos).getIcon());
                args.putString("sunrise", dailyWeatherData.get(pos).getSunriseTime().toString());
                args.putString("sunset", dailyWeatherData.get(pos).getSunsetTime().toString());
                args.putString("summary", dailyWeatherData.get(pos).getSummary());


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

    public void setDailyWeatherData(List<Datum__> weatherData){
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
            lv = (LinearLayout) itemView.findViewById(R.id.text_container);
            weatherIcon = (TextView) itemView.findViewById(R.id.list_icon);
            day = (TextView) itemView.findViewById(R.id.list_day);
            date = (TextView) itemView.findViewById(R.id.list_date);
            temp = (TextView) itemView.findViewById(R.id.list_temp);
            summary = (TextView) itemView.findViewById(R.id.list_summary);
            minTemp = (TextView) itemView.findViewById(R.id.list_temp_min);

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

