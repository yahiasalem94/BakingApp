package com.example.android.bakingapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.R;

public class RecipeDetailsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public final TextView stepName;
    public final TextView description;
    private final RecipeDetailsAdapter.RecipeDetailsAdapterOnClickHandler mClickHandler;

    public RecipeDetailsAdapterViewHolder(View view, RecipeDetailsAdapter.RecipeDetailsAdapterOnClickHandler clickHandler) {
        super(view);
        stepName = view.findViewById(R.id.step_short_descrip);
        description = view.findViewById(R.id.description);
        this.mClickHandler = clickHandler;
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mClickHandler.onClick(getAdapterPosition());
    }
}