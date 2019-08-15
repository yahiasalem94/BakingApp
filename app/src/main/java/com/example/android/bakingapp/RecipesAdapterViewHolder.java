package com.example.android.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public final TextView recipeName;
    private final RecipesAdapter.RecipesAdapterOnClickHandler mClickHandler;

    public RecipesAdapterViewHolder(View view, RecipesAdapter.RecipesAdapterOnClickHandler clickHandler) {
        super(view);
        recipeName = view.findViewById(R.id.recipe_name);
        this.mClickHandler = clickHandler;
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mClickHandler.onClick(getAdapterPosition());
    }
}