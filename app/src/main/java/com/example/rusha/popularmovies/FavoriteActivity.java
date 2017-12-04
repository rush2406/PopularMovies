package com.example.rusha.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.example.rusha.popularmovies.data.MovieContract;

public class FavoriteActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, FavoriteMovieAdapter.ListItemHandlers {
    private FavoriteMovieAdapter adapter;
    public static int position;

    String[] PROJECTION = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_IMAGE,
            MovieContract.MovieEntry.COLUMN_OVER,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_RELEASE,
            MovieContract.MovieEntry.COLUMN_COUNT,
            MovieContract.MovieEntry.COLUMN_RATE
    };

    private RecyclerView favgrid;
    private static final int LOADER_ID = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        favgrid = (RecyclerView) findViewById(R.id.gridfav);
        favgrid.setLayoutManager(new GridLayoutManager(this, 2));
        favgrid.setHasFixedSize(true);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CursorLoader(this,
                MovieContract.MovieEntry.CONTENT_URI,
                PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        adapter = new FavoriteMovieAdapter(this, this);
        favgrid.setAdapter(adapter);
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home)
            NavUtils.navigateUpFromSameTask(this);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ListItemClick(Movies m) {
        Intent i = new Intent(FavoriteActivity.this, DetailActivity.class);
        position = m.getPos();
        Log.v(FavoriteActivity.class.getSimpleName(), "position " + position);
        i.putExtra(getString(R.string.movie), m);
        i.putExtra(getString(R.string.passed), getString(R.string.yes));
        i.putExtra(getString(R.string.position), String.valueOf(DetailActivity.pos));
        startActivity(i);
    }
}
