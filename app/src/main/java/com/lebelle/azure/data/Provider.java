package com.lebelle.azure.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lebelle.azure.Utils;
import com.lebelle.azure.data.Contract.WeatherEntry;

/**
 * Created by HP on 15-Jan-18.
 *
 */

/**
 * {@link ContentProvider} for Employee Database app.
 */

public class Provider extends ContentProvider {
     /**
         * Tag for the log messages
         */
        public static final String LOG_TAG = Provider.class.getSimpleName();

        private static final int WEATHER = 100;
        private static final int WEATHER_WITH_DATE = 101;

        private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        static {
            sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_WEATHER, WEATHER);
            sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_WEATHER + "/#",WEATHER_WITH_DATE);
        }


        /** Database Helper Object*/
        private DBHelper mDbHelper;

        /**
         * Initialize the provider and the database helper object.
         */
        @Override
        public boolean onCreate() {
            mDbHelper = new DBHelper(getContext());
            return true;
        }


    /**
         * Perform a bulk insert of weather for the given URI. Use the given projection, selection, selection arguments, and sort order.
         */
        /*@Override
        public int bulkInsert (@NonNull Uri uri, @NonNull ContentValues[] values ){
           final SQLiteDatabase database = mDbHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            switch (match){
                case WEATHER:
                    database.beginTransaction();
                    int rowsInserted = 0;
                    try {
                        for (ContentValues value : values){
                            long weatherDate = value.getAsLong(WeatherEntry.COLUMN_DATE);
                            if (!Utils.formatDate(weatherDate)){
                                throw new IllegalArgumentException("Date is not long");
                            }
                            long _id = database.insert(WeatherEntry.TABLE_NAME, null, value);
                            if (_id != -1){
                                rowsInserted++;
                            }
                        }
                        database.setTransactionSuccessful();
                    }finally {
                        database.endTransaction();
                    }

                    if (rowsInserted > 0){
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                    return rowsInserted;

                    default:
                        return super.bulkInsert(uri, values);
                    }

            }
*/
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


}
