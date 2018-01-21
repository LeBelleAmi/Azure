package com.lebelle.azure.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.lebelle.azure.data.Contract.WeatherEntry;

/**
 * Created by HP on 30-Dec-17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DBHelper.class.getSimpleName();
    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "weather.db";
    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link DBHelper}. * * @param context of the app
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the employees table
        String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " +
                WeatherEntry.TABLE_NAME + "(" +
                WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WeatherEntry.COLUMN_LOCATION + " TEXT NOT NULL, "
                + WeatherEntry.COLUMN_DATE + " REAL NOT NULL, "
                + WeatherEntry.COLUMN_ICON + " TEXT NOT NULL, "
                + WeatherEntry.COLUMN_SUMMARY + " TEXT NOT NULL, "
                + WeatherEntry.COLUMN_TEMP + " REAL NOT NULL, "
                + WeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, "
                + WeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, "
                + WeatherEntry.COLUMN_APP_TEMP + " REAL NOT NULL, "
                + WeatherEntry.COLUMN_PRECIP_TYPE + " REAL NOT NULL, "
                + WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, "
                + WeatherEntry.COLUMN_WIND_DIRECTION + " REAL NOT NULL, "
                + WeatherEntry.COLUMN_TIME + " REAL NOT NULL, " + ");";
        //Execute the SQL statement
        db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }
    /** * This is called when the database needs to be upgraded. */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
        db.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        onCreate(db);
    }
}

