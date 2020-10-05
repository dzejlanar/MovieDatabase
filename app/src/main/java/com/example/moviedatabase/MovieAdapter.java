package com.example.moviedatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    ArrayList<Movie> movies;

    public MovieAdapter(Context context, ArrayList<Movie> list) {
        movies = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivMoviePoster;
        TextView tvMovieName, tvReleaseDate, tvOverview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivMoviePoster = itemView.findViewById(R.id.ivMoviePoster);
            tvMovieName = itemView.findViewById(R.id.tvMovieName);
            tvReleaseDate = itemView.findViewById(R.id.tvReleaseDate);
            tvOverview = itemView.findViewById(R.id.tvOverview);
        }
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(movies.get(position));
        holder.tvMovieName.setText(movies.get(position).getTitle());
        holder.tvReleaseDate.setText("(" + movies.get(position).getRelease_date() + ")");
        holder.tvOverview.setText(movies.get(position).getOverview());
        Picasso.get().load("https://image.tmdb.org/t/p/w92" + movies.get(position).getPoster_path()).into(holder.ivMoviePoster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


}
