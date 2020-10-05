package com.example.moviedatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.icu.util.ICUUncheckedIOException;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    final static String API_KEY = "2696829a81b1b5827d515ff121700838";

     ListFrag listFrag;
     FragmentManager fragmentManager;

     SearchView searchView;

     ImageView ivMovieIcon;

     ArrayAdapter<String> adapter;
     ListView searchList;
     ArrayList<String> list;
     SearchDB database;

     int currentPage;
     int totalPages;
     String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivMovieIcon = findViewById(R.id.ivMoveIcon);

        fragmentManager = getSupportFragmentManager();
        listFrag = (ListFrag) fragmentManager.findFragmentById(R.id.listFrag);
        fragmentManager.beginTransaction()
                .hide(listFrag)
                .commit();

        database = new SearchDB(this);
        searchList = findViewById(R.id.searchList);

        list = new ArrayList<>();
        database.open();
        list = database.getSearches();
        database.close();

        setAdapter();
        searchList.setVisibility(View.GONE);

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                APICall(s, "1");
                createScrollListener();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    searchList.setVisibility(View.VISIBLE);
                    ivMovieIcon.setVisibility(View.GONE);
                } else {
                    searchList.setVisibility(View.GONE);
                    ivMovieIcon.setVisibility(View.VISIBLE);
                }
            }
        });

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = adapter.getItem(position);
                APICall(item, "1");
                createScrollListener();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (searchView.getVisibility() == View.VISIBLE)
            super.onBackPressed();
        else {
            fragmentManager.beginTransaction()
                    .hide(listFrag)
                    .commit();
            searchView.setVisibility(View.VISIBLE);
            searchList.setVisibility(View.GONE);
            ivMovieIcon.setVisibility(View.VISIBLE);
            searchView.setQuery("", false);
        }
    }

    public void APICall(final String query, final String page) {
        searchQuery = query;
        MovieDBAPI request = MovieAPI.getClient().create(MovieDBAPI.class);
        Call<MovieResponse> call = request.getMovies(API_KEY, query, page);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body().getResults().size() == 0) {

                    AlertDialog alert = createAlertDialog("No results.").create();
                    alert.show();
                    return;
                }
                ArrayList<Movie> movies = (ArrayList<Movie>) response.body().getResults();
                totalPages = (int) response.body().getTotalPages();
                currentPage = (int) response.body().getPage();

                if (currentPage > 1)
                    listFrag.setAddRecyclerViewAdapter(movies);
                else
                    listFrag.setRecyclerViewAdapter(movies);

                hideFirstScreen();
                addToList(query);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                AlertDialog alert = createAlertDialog(t.getMessage()).create();
                alert.show();
            }
        });
    }

    public void addToList(String search) {
        boolean isInList = false;
        database.open();
        for (String listItem : list)
            if (listItem.toLowerCase().compareTo(search.toLowerCase()) == 0) {
                isInList = true;
                break;
            }

        if (!isInList) {
            if (list.size() >= 10) {
                database.deleteFirst();
                list.remove(list.size() - 1);
            }
            database.createEntry(search);
            list.add(0, search);
            setAdapter();
        }
    }

    public void hideFirstScreen() {
        searchView.setVisibility(View.GONE);
        searchList.setVisibility(View.GONE);
        ivMovieIcon.setVisibility(View.GONE);
        fragmentManager.beginTransaction()
                .show(listFrag)
                .commit();
    }

    public void createScrollListener(){

        listFrag.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    if (currentPage < totalPages){
                        currentPage++;
                        showToast("Page" + currentPage);
                        APICall(searchQuery, Integer.toString(currentPage));
                    }
                    else
                        showToast("No more pages!");
                }
            }
        });
    }

    public AlertDialog.Builder createAlertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder;
    }

    public void showToast(String string){
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    public void setAdapter() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        searchList.setAdapter(adapter);
    }
}