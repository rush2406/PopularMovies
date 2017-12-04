package com.example.rusha.popularmovies;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rusha.popularmovies.data.MovieContract;
import com.example.rusha.popularmovies.utilities.ReviewUtility;
import com.example.rusha.popularmovies.utilities.TrailerUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rusha on 6/14/2017.
 */

public class DetailActivity extends AppCompatActivity {

    @InjectView(R.id.title1)
    TextView title;
    @InjectView(R.id.votercount)
    TextView votercount;
    @InjectView(R.id.voterrate)
    TextView voterrate;
    @InjectView(R.id.overview)
    TextView overview;
    @InjectView(R.id.release)
    TextView release;
    @InjectView(R.id.detail_image)
    ImageView image;
    @InjectView(R.id.trailer1)
    LinearLayout trailer1;
    @InjectView(R.id.trailer2)
    LinearLayout trailer2;
    @InjectView(R.id.trailer3)
    LinearLayout trailer3;
    @InjectView(R.id.mark)
    Button mark;
    @InjectView(R.id.del)
    Button delete;

    private int id;
    private int trailerId;
    private Movies movies;
    public static int pos;
    private static final String LOG = DetailActivity.class.getSimpleName();

    @TargetApi(23)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        ButterKnife.inject(this);
        Intent i = getIntent();
        String index = i.getStringExtra(getString(R.string.position));
        pos = Integer.parseInt(index);

        String passed = i.getStringExtra(getString(R.string.passed));
        if (passed.equals(getString(R.string.yes))) {
            mark.setVisibility(View.GONE);
            delete.setVisibility(View.VISIBLE);
            movies = i.getParcelableExtra(getString(R.string.movie));
        } else
            movies = MovieAdapter.arrayList.get(pos);
        int flag = 0;
        String s = "start";
        for (int x = 0; x < MainActivity.titles.size(); x++) {
            Log.v(LOG, s + MainActivity.titles.get(x));
        }

        for (int x = 0; x < MainActivity.titles.size(); x++) {
            if (movies.getTitle().equalsIgnoreCase(MainActivity.titles.get(x))) {
                flag = 1;
                Log.v(LOG, "Check = " + movies.getTitle());
                mark.setBackgroundColor(getColor(R.color.pink));
                mark.setText(getString(R.string.added));
                break;
            }
        }
        if (flag == 0) {
            mark.setText(getString(R.string.add));
            mark.setBackgroundColor(getColor(R.color.grey));
        }
        id = movies.getId();
        Log.v(LOG, "Tinku " + id);
        setTitle(movies.getTitle());
        if (movies != null) {
            title.setText(getString(R.string.title) + movies.getTitle());
            voterrate.setText(getString(R.string.rating) + movies.getVoterrate());
            votercount.setText(getString(R.string.count) + movies.getVotercount());
            overview.setText(getString(R.string.over) + movies.getOverview());
            release.setText(getString(R.string.release) + movies.getRelease());
            trailer1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    trailerId = 0;
                    ConnectivityManager cm =
                            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
                    if (isConnected) {
                        new TrailerTask().execute("https://api.themoviedb.org/3/movie/" + id + "/videos" +
                                "?api_key=7af039a2f1db2dbe51c2c40bbf7c50b3");
                    } else
                        Toast.makeText(getApplicationContext(), getString(R.string.con), Toast.LENGTH_SHORT).show();

                }
            });
            trailer2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    trailerId = 1;
                    ConnectivityManager cm =
                            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
                    if (isConnected) {
                        new TrailerTask().execute("https://api.themoviedb.org/3/movie/" + id + "/videos" +
                                "?api_key=7af039a2f1db2dbe51c2c40bbf7c50b3");
                    } else
                        Toast.makeText(getApplicationContext(), getString(R.string.con), Toast.LENGTH_SHORT).show();
                }
            });
            trailer3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    trailerId = 2;
                    ConnectivityManager cm =
                            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
                    if (isConnected) {
                        new TrailerTask().execute("https://api.themoviedb.org/3/movie/" + id + "/videos" +
                                "?api_key=7af039a2f1db2dbe51c2c40bbf7c50b3");
                    } else
                        Toast.makeText(getApplicationContext(), getString(R.string.con), Toast.LENGTH_SHORT).show();
                }
            });
            if (movies.getImage() != null)
                Picasso.with(DetailActivity.this).load("http://image.tmdb.org/t/p/w500" + movies.getImage()).into(image);
            else
                image.setImageResource(R.drawable.noimage);
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.data), Toast.LENGTH_SHORT).show();
    }


    public class TrailerTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {

            return TrailerUtility.fetchData(strings[0]);

        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {

            String url = "https://www.youtube.com/watch?v=";
            if (strings.size() <= trailerId)
                Toast.makeText(getApplicationContext(), getString(R.string.notrailer), Toast.LENGTH_SHORT).show();
            else {
                url = url + strings.get(trailerId);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        }
    }


    public class ReviewTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String value = ReviewUtility.fetchData(strings[0]);
            if (value.length() == 0)
                return "";
            else
                return value;
        }

        @Override
        protected void onPostExecute(String strings) {

            if (strings.length() == 0)
                Toast.makeText(getApplicationContext(), getString(R.string.noreview), Toast.LENGTH_SHORT).show();
            else {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(strings));
                startActivity(i);
            }
        }
    }


    public void review(View view) {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            new ReviewTask().execute("https://api.themoviedb.org/3/movie/" + id + "/reviews" +
                    "?api_key=7af039a2f1db2dbe51c2c40bbf7c50b3");
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.con), Toast.LENGTH_SHORT).show();
    }

    @TargetApi(23)
    public void favorite(View view) {

        int flag = 0;
        for (int i = 0; i < MainActivity.titles.size(); i++) {
            if (movies.getTitle().equals(MainActivity.titles.get(i))) {
                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            mark.setBackgroundColor(getColor(R.color.pink));
            mark.setText(getString(R.string.added));
            MainActivity.titles.add(movies.getTitle());
            mark.setSaveEnabled(true);
            movies.setValue(true);
            MovieAdapter.arrayList.set(pos, movies);
            ContentValues values = new ContentValues();
            values.put(MovieContract.MovieEntry.COLUMN_TITLE, movies.getTitle());
            values.put(MovieContract.MovieEntry.COLUMN_COUNT, movies.getVotercount());
            values.put(MovieContract.MovieEntry.COLUMN_RATE, movies.getVoterrate());
            values.put(MovieContract.MovieEntry.COLUMN_IMAGE, movies.getImage());
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movies.getId());
            values.put(MovieContract.MovieEntry.COLUMN_OVER, movies.getOverview());
            values.put(MovieContract.MovieEntry.COLUMN_RELEASE, movies.getRelease());

            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
            if (uri == null)
                Toast.makeText(getApplicationContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), getString(R.string.added), Toast.LENGTH_SHORT).show();
        }

    }

    public void delete(View view) {
        String uri = MovieContract.MovieEntry.CONTENT_URI.toString();
        uri = uri + "/" + FavoriteActivity.position;
        Log.v(LOG, "Uri = " + uri);

        movies.setValue(false);
        MovieAdapter.arrayList.set(pos, movies);
        for (int i = 0; i < MainActivity.titles.size(); i++) {
            if (MainActivity.titles.get(i).equals(movies.getTitle()))
                MainActivity.titles.remove(i);

        }
        String s = "delete";
        for (int i = 0; i < MainActivity.titles.size(); i++) {
            Log.v(LOG, s + MainActivity.titles.get(i));
        }

        int rows = getContentResolver().delete(Uri.parse(uri), null, null);
        if (rows < 0)
            Toast.makeText(getApplicationContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), getString(R.string.delete), Toast.LENGTH_SHORT).show();
        finish();
    }

}
