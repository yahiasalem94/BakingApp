package com.example.android.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.Models.RecipeData;
import com.example.android.bakingapp.Utils.ApiInterface;
import com.example.android.bakingapp.Utils.NetworkUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipesFragment extends Fragment  implements RecipesAdapter.RecipesAdapterOnClickHandler  {

    private static final String TAG = RecipesFragment.class.getSimpleName();

    private RecyclerView recipesRecyclerView;
    private LinearLayoutManager layoutManager;
    private RecipesAdapter recipesAdapter;

    private ApiInterface apiService;

    private ArrayList<RecipeData> recipesList;

    public RecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipesAdapter = new RecipesAdapter(this);
        apiService = NetworkUtils.getRetrofitInstance().create(ApiInterface.class);
        loadData();
    }

    private void loadData() {
        Call<ArrayList<RecipeData>> call = apiService.getRecipes();

        call.enqueue(new Callback<ArrayList<RecipeData>>() {
            @Override
            public void onResponse(Call<ArrayList<RecipeData>> call, Response<ArrayList<RecipeData>> response) {

                recipesList = response.body();
//                mLoadingIndicator.setVisibility(View.INVISIBLE);
                recipesAdapter.setMoviesData(recipesList);
//                mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }

            @Override
            public void onFailure(Call<ArrayList<RecipeData>> call, Throwable t) {

                Log.e(TAG, t.toString());
//                mLoadingIndicator.setVisibility(View.INVISIBLE);
//
//                if ( t instanceof IOException) {
//                    isConnected = false;
//                }
//
//                showErrorMessage(isConnected);

            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the fragment's layout
        return inflater.inflate(R.layout.fragment_recipes, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Create the recyclerview.
        recipesRecyclerView = view.findViewById(R.id.recipesRecyclerView);
        layoutManager = new LinearLayoutManager(view.getContext());
        recipesRecyclerView.setLayoutManager(layoutManager);

        // Set data adapter.
        recipesRecyclerView.setAdapter(recipesAdapter);
    }

    @Override
    public void onClick(int position) {

    }
}
