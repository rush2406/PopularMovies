package com.example.rusha.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rusha.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rusha on 6/17/2017.
 */

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.MovieHolder> {
    private Context mContext;
    private Cursor mCursor;
    private ListItemHandlers listItemHandlers;
    private ArrayList<Movies> fav = new ArrayList<>();


    public FavoriteMovieAdapter(Context context, ListItemHandlers list) {
        mContext = context;
        listItemHandlers = list;
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.movie_item, parent, false);
        FavoriteMovieAdapter.MovieHolder m = new FavoriteMovieAdapter.MovieHolder(v);
        return m;
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {

        mCursor.moveToPosition(position);
        String title = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
        int actualId = mCursor.getInt(mCursor.getColumnIndex(MovieContract.MovieEntry._ID));
        String votercount = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_COUNT));
        String voterrate = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATE));
        String release = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
        int id = mCursor.getInt(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
        String overview = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVER));
        String image = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE));
        Movies m = new Movies(id, title, image, votercount, voterrate, release, overview, actualId);
        fav.add(m);
        try {
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185" + image).into(holder.image);

        } catch (NullPointerException n) {
            Log.e(FavoriteMovieAdapter.class.getSimpleName(), "Error");

        }

    }

    @Override
    public int getItemCount() {
        if (mCursor == null)
            return 0;
        return mCursor.getCount();
    }

    public interface ListItemHandlers {


        void ListItemClick(Movies m);

    }

    class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;

        public MovieHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.movie_image);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Movies m = fav.get(pos);
            listItemHandlers.ListItemClick(m);
        }
    }
}
