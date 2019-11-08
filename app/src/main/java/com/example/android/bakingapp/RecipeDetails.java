package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.android.bakingapp.Adapters.RecipeDetailsAdapter;
import com.example.android.bakingapp.Adapters.RecipeIngredientsAdapter;
import com.example.android.bakingapp.Models.RecipeIngredients;
import com.example.android.bakingapp.Models.RecipeSteps;
import com.example.android.bakingapp.Utils.Constants;
import com.example.android.bakingapp.Utils.SharedPreferenceUtil;
import com.example.android.bakingapp.Widget.IngredientUpdateService;

import java.util.ArrayList;

public class RecipeDetails extends Fragment implements RecipeDetailsAdapter.RecipeDetailsAdapterOnClickHandler {

    private static final String TAG = RecipeDetails.class.getSimpleName();

    private RecyclerView recipesStepsRecyclerView;
    private LinearLayoutManager layoutManagerSteps;
    private RecipeDetailsAdapter recipesStepsAdapter;

    private ArrayList<RecipeSteps> recipesSteps;
    private ArrayList<RecipeIngredients> recipeIngredients;
    private String mRecipeName;
    private ArrayList<RecipeIngredients> savedIngredients = null;

    boolean isSaved = false;

    public RecipeDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipesStepsAdapter = new RecipeDetailsAdapter(this);

        recipesSteps = ((RecipeDetailsMainActivity) getActivity()).recipesSteps;
        recipeIngredients = ((RecipeDetailsMainActivity) getActivity()).recipeIngredients;
         mRecipeName = ((RecipeDetailsMainActivity) getActivity()).recipeName;

        savedIngredients = SharedPreferenceUtil.getIngredientsFromSharedPrefsForKey(Constants.ADDED_INGREDIENT, getActivity().getApplicationContext());
        if (savedIngredients != null) {
            if (savedIngredients.get(0).getIngredient().equals(recipeIngredients.get(0).getIngredient())) {
                isSaved = true;
            }
        }


        recipesStepsAdapter.setRecipeSteps(recipesSteps);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment's layout
        return inflater.inflate(R.layout.fragment_recipe_details, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Create the recyclerview.
        recipesStepsRecyclerView = view.findViewById(R.id.recipesDetailsRecyclerView);
        layoutManagerSteps = new LinearLayoutManager(view.getContext());
        recipesStepsRecyclerView.setLayoutManager(layoutManagerSteps);

        // Set data adapter.
        recipesStepsRecyclerView.setAdapter(recipesStepsAdapter);
    }

    @Override
    public void onClick(int position) {

        Log.d(TAG, recipesSteps.get(position).getDescription());
        RecipeStep fragment = new RecipeStep();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.RECIPE_STEP_POSITION, position);
        bundle.putParcelableArrayList(Constants.STEPS_LIST, recipesSteps);
        Intent intent = new Intent(getActivity(), RecipeStep.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
