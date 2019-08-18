package com.example.android.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.Models.RecipeIngredients;
import com.example.android.bakingapp.Models.RecipeSteps;
import com.example.android.bakingapp.R;

import java.util.ArrayList;


public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapterViewHolder> {

    private ArrayList<RecipeIngredients> mRecipeIngredients;

    public RecipeIngredientsAdapter() {

    }

    @Override
    public RecipeIngredientsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_ingredients_row, viewGroup, false);
        return new RecipeIngredientsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeIngredientsAdapterViewHolder adapterViewHolder, int position) {
        adapterViewHolder.Ingredient.setText(mRecipeIngredients.get(position).getIngredient());
        adapterViewHolder.quantity.setText(Double.toString(mRecipeIngredients.get(position).getQuantity()));
        adapterViewHolder.measure.setText(mRecipeIngredients.get(position).getMeasure());
    }

    @Override
    public int getItemCount() {
        if (null == mRecipeIngredients) return 0;
        return mRecipeIngredients.size();
    }

    public void setRecipeIngredients(ArrayList<RecipeIngredients> recipeIngredients) {
        mRecipeIngredients = recipeIngredients;
        notifyDataSetChanged();
    }
}