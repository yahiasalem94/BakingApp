package com.example.android.bakingapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.R;

public class RecipeIngredientsAdapterViewHolder extends RecyclerView.ViewHolder {

    public final TextView Ingredient;
    public final TextView quantity;
    public final TextView measure;

    public RecipeIngredientsAdapterViewHolder(View view) {
        super(view);

        Ingredient = view.findViewById(R.id.textViewIngredient);
        quantity = view.findViewById(R.id.textViewQuantity);
        measure = view.findViewById(R.id.textViewMeasure);
    }
}