package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.Models.RecipeData;

import java.util.ArrayList;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapterViewHolder> {

    private ArrayList<RecipeData> mRecipeData;
    private final RecipesAdapterOnClickHandler mClickHandler;


    public interface RecipesAdapterOnClickHandler {
        void onClick(int position);
    }


    public RecipesAdapter(RecipesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_row, viewGroup, false);
        return new RecipesAdapterViewHolder(view, mClickHandler);
    }

    @Override
    public void onBindViewHolder(RecipesAdapterViewHolder adapterViewHolder, int position) {
        adapterViewHolder.recipeName.setText(mRecipeData.get(position).getRecipeName());
    }

    @Override
    public int getItemCount() {
        if (null == mRecipeData) return 0;
        return mRecipeData.size();
    }

    public void setMoviesData(ArrayList<RecipeData> recipeData) {
        this.mRecipeData = recipeData;
        notifyDataSetChanged();
    }
}