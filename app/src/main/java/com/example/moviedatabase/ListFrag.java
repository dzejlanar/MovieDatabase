package com.example.moviedatabase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFrag extends Fragment {

     RecyclerView recyclerView;
     RecyclerView.Adapter adapter;
     RecyclerView.LayoutManager layoutManager;
     View view;
     ArrayList<Movie> list;

    public ListFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_list, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.list = new ArrayList<>();

        recyclerView = view.findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MovieAdapter(this.getContext(), list);
        recyclerView.setAdapter(adapter);

    }

    public void setRecyclerViewAdapter(ArrayList<Movie> list) {
        this.list.clear();
        this.list.addAll(list);
        adapter = new MovieAdapter(this.getContext(), this.list);
        recyclerView.setAdapter(adapter);
    }

    public void setAddRecyclerViewAdapter(ArrayList<Movie> list){
        this.list.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public RecyclerView getRecyclerView(){
        return this.recyclerView;
    }
}