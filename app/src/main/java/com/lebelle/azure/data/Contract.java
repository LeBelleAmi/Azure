package com.lebelle.azure.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by HP on 30-Dec-17.
 */

public class Contract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private Contract() {
    }

    public static final String CONTENT_AUTHORITY = "com.lebelle.weatherdatabase";

    //use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact the content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //possible path
    public static final String PATH_WEATHER = "weather-path";


    /**
     * Inner class that defines constant values for the weather database table.
     * Each entry in the table represents a single day weather.
     */
    public static final class WeatherEntry implements BaseColumns {

        /**
         * Name of database table for employees
         */
        public final static String TABLE_NAME = "weather";

        /**
         * Unique ID number for the weather (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * weather day.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_LOCATION = "location";
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_ICON = "icon";
        public final static String COLUMN_SUMMARY = "summary";
        public final static String COLUMN_TEMP = "temp";
        public final static String COLUMN_HUMIDITY = "humidity";
        public final static String COLUMN_PRESSURE = "pressure";
        public final static String COLUMN_APP_TEMP = "app temp";
        public final static String COLUMN_PRECIP_TYPE = "precip type";
        public final static String COLUMN_WIND_SPEED = "wind speed";
        public final static String COLUMN_WIND_DIRECTION = "wind direction";
        public final static String COLUMN_TIME = "time";
    }
}
