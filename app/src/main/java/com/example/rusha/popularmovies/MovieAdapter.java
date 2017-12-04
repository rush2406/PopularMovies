package com.example.rusha.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rusha on 6/14/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {
    private Context mContext;
    public static ArrayList<Movies> arrayList;
    final private ListItemHandler mClickListener;

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context context, ArrayList<Movies> movies, ListItemHandler listItemHandler) {
        this.mContext = context;
        this.arrayList = movies;
        this.mClickListener = listItemHandler;
    }


    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.movie_item, parent, false);
        MovieHolder m = new MovieHolder(v);
        return m;
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {

        Movies movieItem = arrayList.get(position);
        try {
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185" + movieItem.getImage()).into(holder.image);

        } catch (NullPointerException n) {
            Log.e(LOG_TAG, "Null value");
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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
            Movies m = arrayList.get(pos);
            mClickListener.ListItemClick(m, pos);
        }
    }

    public interface ListItemHandler {


        void ListItemClick(Movies m, int position);

    }
}
