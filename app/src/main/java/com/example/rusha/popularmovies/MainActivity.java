package com.example.rusha.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rusha.popularmovies.data.MovieContract;
import com.example.rusha.popularmovies.utilities.NetworkUtility;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemHandler,
        LoaderCallbacks<ArrayList<Movies>>, SharedPreferences.OnSharedPreferenceChangeListener {
    private String API_STRING;
    private MovieAdapter adapter;
    private RecyclerView grid;
    private TextView EmptyText;
    private ProgressBar progressBar;
    private static final int LOADER_ID = 3;
    public static ArrayList<String> titles = new ArrayList<>();
    String[] Projection = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grid = (RecyclerView) findViewById(R.id.grid);
        grid.setLayoutManager(new GridLayoutManager(this, 2));
        grid.setHasFixedSize(true);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        EmptyText = (TextView) findViewById(R.id.empty);
        setupAPI();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void setupAPI() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value = preferences.getString(getString(R.string.sorting), getString(R.string.popularity));
        if (value.equals(getString(R.string.popularity)))
            API_STRING = "https://api.themoviedb.org/3/movie/popular?api_key=7af039a2f1db2dbe51c2c40bbf7c50b3";
        else
            API_STRING = "https://api.themoviedb.org/3/movie/top_rated?api_key=7af039a2f1db2dbe51c2c40bbf7c50b3";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void ListItemClick(Movies m, int pos) {
        Intent i = new Intent(MainActivity.this, DetailActivity.class);
        i.putExtra(getString(R.string.movie), m);
        i.putExtra(getString(R.string.passed), " ");
        i.putExtra(getString(R.string.position), String.valueOf(pos));
        startActivity(i);
    }

    @Override
    public Loader<ArrayList<Movies>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movies>>(this) {
            ArrayList<Movies> movies = null;

            @Override
            protected void onStartLoading() {
                if (movies != null)
                    deliverResult(movies);
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    EmptyText.setVisibility(View.INVISIBLE);
                    grid.setVisibility(View.INVISIBLE);
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Movies> loadInBackground() {

                Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        Projection,
                        null,
                        null,
                        null);
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
                    Log.v(MainActivity.class.getSimpleName(), "Rushali " + name);
                    titles.add(name);
                }
                cursor.close();
                String url = API_STRING;
                return NetworkUtility.fetchMoviesData(url);
            }

            @Override
            public void deliverResult(ArrayList<Movies> data) {
                movies = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movies>> loader, ArrayList<Movies> data) {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            EmptyText.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            grid.setVisibility(View.VISIBLE);
            adapter = new MovieAdapter(MainActivity.this, data, MainActivity.this);
            grid.setAdapter(adapter);
        } else {
            EmptyText.setVisibility(View.VISIBLE);
            EmptyText.setText(getString(R.string.con));
            progressBar.setVisibility(View.INVISIBLE);
            grid.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public void onLoaderReset(Loader<ArrayList<Movies>> loader) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.settings:
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                break;

            case R.id.favorite:
                Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent);
                break;

        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.sorting))) {
            String value = sharedPreferences.getString(getString(R.string.sorting), getString(R.string.popularity));
            if (value.equals(getString(R.string.popularity)))
                API_STRING = "https://api.themoviedb.org/3/movie/popular?api_key=7af039a2f1db2dbe51c2c40bbf7c50b3";
            else
                API_STRING = "https://api.themoviedb.org/3/movie/top_rated?api_key=7af039a2f1db2dbe51c2c40bbf7c50b3";
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }

    }
}
