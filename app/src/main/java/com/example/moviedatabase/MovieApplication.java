package com.example.moviedatabase;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieApplication extends Application {

    public static ArrayList<Movie> movies;

    @Override
    public void onCreate() {
        super.onCreate();

//        movies = new ArrayList<Movie>();
//        movies.add(new Movie("Batman", "2000", "Lorem Ipsum", ".."));
//        movies.add(new Movie("Batman", "2000", "Lorem Ipsum", ".."));
//        movies.add(new Movie("Superman", "2000", "Lorem Ipsum", ".."));
//        movies.add(new Movie("Spiderman", "2000", "Lorem Ipsum", ".."));
//        movies.add(new Movie("Jumanji", "2000", "Lorem Ipsum", ".."));
//        movies.add(new Movie("Batman", "2000", "Lorem Ipsum", ".."));
//        movies.add(new Movie("Batman", "2000", "Lorem Ipsum", ".."));
//        movies.add(new Movie("Superman", "2000", "Lorem Ipsum", ".."));
//        movies.add(new Movie("Spiderman", "2000", "Lorem Ipsum", ".."));
//        movies.add(new Movie("Jumanji", "2000", "Lorem Ipsum", ".."));
    }
}
