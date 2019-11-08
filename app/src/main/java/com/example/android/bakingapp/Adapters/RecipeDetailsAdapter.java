package com.example.android.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.Models.RecipeSteps;
import com.example.android.bakingapp.R;

import java.util.ArrayList;


public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapterViewHolder> {

    private ArrayList<RecipeSteps> mRecipeSteps;
    private final RecipeDetailsAdapterOnClickHandler mClickHandler;


    public interface RecipeDetailsAdapterOnClickHandler {
        void onClick(int position);
    }


    public RecipeDetailsAdapter(RecipeDetailsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public RecipeDetailsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_steps_row, viewGroup, false);
        return new RecipeDetailsAdapterViewHolder(view, mClickHandler);
    }

    @Override
    public void onBindViewHolder(RecipeDetailsAdapterViewHolder adapterViewHolder, int position) {
        adapterViewHolder.stepName.setText(mRecipeSteps.get(position).getShortDescription());
        adapterViewHolder.description.setText(mRecipeSteps.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        if (null == mRecipeSteps) return 0;
        return mRecipeSteps.size();
    }

    public void setRecipeSteps(ArrayList<RecipeSteps> recipeSteps) {
        mRecipeSteps = recipeSteps;
        notifyDataSetChanged();
    }
}