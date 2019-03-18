package com.lebelle.azure;

import android.content.Context;

import com.lebelle.azure.data.AppPrefs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Omawumi Eyekpimi on 21-Nov-17.
 */

public class Utils {

    public static int getIconId(String icon, double sunrise, double sunset){
        int iconId = R.string.weather_sunny;
        //int backgroundId = R.drawable.kites_day;
        if (icon.equals("clear")){
            long currentTime = new Date().getTime();
            if (currentTime>=sunrise && currentTime<sunset){
                iconId = R.string.weather_sunny;
                //backgroundId = R.drawable.kites_day;
            }else {
                iconId = R.string.weather_clear_night;
                //backgroundId = R.drawable.kites_night;
            }
        }else if (icon.equals("clear-day")){
            iconId = R.string.weather_sunny;
            //backgroundId = R.drawable.kites_day;
        } else if (icon.equals("clear-night")){
            iconId = R.string.weather_clear_night;
            //backgroundId = R.drawable.kites_night;
        }
        else if (icon.equals("rain")){
            iconId = R.string.weather_rainy;
        }
        else if (icon.equals("snow")){
            iconId = R.string.weather_snowy;
        }
        else if (icon.equals("sleet")){
            iconId = R.string.weather_sleet;
        }
        else if (icon.equals("wind")){
            iconId = R.string.weather_windy;
        }
        else if (icon.equals("fog")){
            iconId = R.string.weather_foggy;
        }
        else if (icon.equals("cloudy")){
            iconId = R.string.weather_cloudy;
        }
        else if (icon.equals("partly-cloudy-day")){
            iconId = R.string.weather_cloudy_day;
        }
        else if (icon.equals("partly-cloudy-night")){
            iconId = R.string.weather_cloudy_night;
        }else if (icon.equals("hail")){
            iconId = R.string.weather_cloudy_night;
        }else if (icon.equals("thunderstorm")){
        iconId = R.string.weather_cloudy_night;
        }else if (icon.equals("tornado")){
        iconId = R.string.weather_cloudy_night;}
            return iconId;
    }
    /**
     * This method will convert a temperature from Fahrenheit to Celsius.
     *
     * Temperature in degrees Celsius(°C)
     *
     * @return Temperature in degrees Fahrenheit (°F)
     */
    public static double convertFahToCel(double fah){
        return ((fah - 32) * 5/9);
    }

    /**
     * Temperature data is stored in Fahrenheit by our app. Depending on the user's preference,
     * the app may need to display the temperature in Celsius. This method will perform that
     * temperature conversion if necessary. It will also format the temperature so that no
     * decimal points show. Temperatures will be formatted to the following form: "21°"
     *
     * @param context     Android Context to access preferences and resources
     * @param temperature Temperature in degrees Celsius (°C)
     *
     * @return Formatted temperature String in the following form:
     * "21°"
     */
    public static String formatTemperature(Context context, double temperature) {

        int temperatureFormatResourceId = R.string.format_temperature;

        if (!AppPrefs.isImperial(context)) {
            temperature = convertFahToCel(temperature);
        }


        /* For presentation, assume the user doesn't care about tenths of a degree. */
        return String.format(context.getString(temperatureFormatResourceId), temperature);
    }

/*details page date conversion utils*/
    public static String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, LLL dd yyyy", Locale.getDefault());
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    public static String formatTime(Date timeObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a", Locale.getDefault());
        return timeFormat.format(timeObject);
    }



    /**
     * This method uses the wind direction in degrees to determine compass direction as a
     * String. (eg NW) The method will return the wind String in the following form: "2 km/h SW"
     *
     * @param context   Android Context to access preferences and resources
     * @param windSpeed Wind speed in kilometers / hour
     * @param degrees   Degrees as measured on a compass, NOT temperature degrees!
     *                  See https://www.mathsisfun.com/geometry/degrees.html
     * @return Wind String in the following form: "2 km/h SW"
     */
    public static String getFormattedWind(Context context, float windSpeed, float degrees) {
        int windFormat = R.string.format_wind_mph;

        if (!AppPrefs.isImperial(context)) {
            windFormat = R.string.format_wind_kmh;
            windSpeed = 1.60934f * windSpeed;
        }

        /*
         * You know what's fun? Writing really long if/else statements with tons of possible
         * conditions. Seriously, try it!
         */
        String direction = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "North";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "North East";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "East";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "South East";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "South";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "South West";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "West";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "North West";
        }

        return String.format(context.getString(windFormat), windSpeed, direction);
    }

    public static String getFormattedPressure(Context context, float pressure) {
        int pressureFormat = R.string.format_pressure_mb;

        if (!AppPrefs.isImperial(context)) {
            pressureFormat = R.string.format_pressure_hpa;
        }
        /* For presentation, assume the user doesn't care about tenths of a degree. */
        return String.format(context.getString(pressureFormat), pressure);
    }

    public static String getFormattedWind(Context context, float wind) {
        int windFormat = R.string.format_wind_mph1;

        if (!AppPrefs.isImperial(context)) {
            windFormat = R.string.format_wind_kmh1;
            wind = 1.60934f * wind;
        }
        /* For presentation, assume the user doesn't care about tenths of a degree. */
        return String.format(context.getString(windFormat), wind);
    }


}
