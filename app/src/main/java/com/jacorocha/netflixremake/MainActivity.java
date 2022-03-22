package com.jacorocha.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jacorocha.netflixremake.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainAdapter mainadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_main);

        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 30; i++ ){
            Movie movie = new Movie();
            movie.setCoverUrl("abc" + i);
            movies.add(movie);
        }

        mainadapter = new MainAdapter(movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(mainadapter);
    }
    private static class MovieHolder extends RecyclerView.ViewHolder {
        final TextView textViewUrl;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
           textViewUrl =  itemView.findViewById(R.id.text_view_url);
        }
    }

    private class MainAdapter extends RecyclerView.Adapter<MovieHolder>{

        private final List<Movie> movies;

        public MainAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(getLayoutInflater().inflate(R.layout.movie_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie movie = movies.get(position);
            holder.textViewUrl.setText(movie.getCoverUrl());

        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }
}