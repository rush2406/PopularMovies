package com.example.rusha.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by rusha on 6/16/2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.rusha.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH = "movies";

    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH)
                .build();

        public static final String TABLE_NAME = "movies";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_COUNT = "votercount";
        public static final String COLUMN_RATE = "rate";
        public static final String COLUMN_RELEASE = "release";
        public static final String COLUMN_OVER = "overview";
        public static final String COLUMN_MOVIE_ID = "movieId";
    }

}
