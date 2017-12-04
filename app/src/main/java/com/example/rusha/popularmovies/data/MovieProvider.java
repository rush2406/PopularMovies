package com.example.rusha.popularmovies.data;

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
import android.widget.Toast;

/**
 * Created by rusha on 6/17/2017.
 */

public class MovieProvider extends ContentProvider {

    private static final int MOVIES = 100;
    private static final int MOVIE_ID = 101;
    private static final String LOG_TAG = MovieProvider.class.getSimpleName();
    private static UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH, MOVIES);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH + "/#", MOVIE_ID);
        return uriMatcher;
    }

    private MovieDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDbHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
                if (id < 0) {
                    Toast.makeText(getContext(), "Insert Failed", Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, "Insert failed");
                } else
                    getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
            default:
                throw new UnsupportedOperationException("Unknown Uri = " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor c;
        switch (match) {
            case MOVIES:
                c = db.query(MovieContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri = " + uri);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rows;
        switch (match) {
            case MOVIE_ID:
                s = MovieContract.MovieEntry._ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rows = db.delete(MovieContract.MovieEntry.TABLE_NAME, s, strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri = " + uri);
        }
        if (rows > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rows;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return MovieContract.CONTENT_LIST_TYPE;
            case MOVIE_ID:
                return MovieContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }

    }
}
