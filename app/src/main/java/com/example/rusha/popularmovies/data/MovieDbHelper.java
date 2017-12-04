package com.example.rusha.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rusha on 6/16/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_COUNT + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RATE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER DEFAULT 0, " +
                MovieContract.MovieEntry.COLUMN_OVER + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
