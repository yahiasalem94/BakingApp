package com.example.android.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.Models.RecipeResponse;
import com.example.android.bakingapp.R;

import java.util.ArrayList;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapterViewHolder> {

    private ArrayList<RecipeResponse> mRecipeData;
    private final RecipesAdapterOnClickHandler mClickHandler;
    int[] myImageList = new int[]{R.drawable.nutella_pie, R.drawable.brownies, R.drawable.yellow_cake, R.drawable.cheese_cake};

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
        adapterViewHolder.recipePhoto.setImageResource(myImageList[position]);
    }

    @Override
    public int getItemCount() {
        if (null == mRecipeData) return 0;
        return mRecipeData.size();
    }

    public void setRecipesData(ArrayList<RecipeResponse> recipeData) {
        this.mRecipeData = recipeData;
        notifyDataSetChanged();
    }
}