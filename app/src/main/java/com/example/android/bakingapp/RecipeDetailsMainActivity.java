package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.bakingapp.Models.RecipeIngredients;
import com.example.android.bakingapp.Models.RecipeSteps;
import com.example.android.bakingapp.Utils.Constants;

import java.util.ArrayList;

public class RecipeDetailsMainActivity extends AppCompatActivity {

    private static final String TAG = RecipeDetailsMainActivity.class.getSimpleName();




    private Bundle recipeBundle;
    private ArrayList<RecipeSteps> recipesSteps;
    private ArrayList<RecipeIngredients> recipeIngredients;
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(Constants.TAG_RECIPE_STEP_FRAGMENT);

        if(getIntent().hasExtra(Constants.STEPS_LIST) && getIntent().hasExtra(Constants.INGREDIENTS_LIST)
                && getIntent().hasExtra(Constants.RECIPE_NAME)) {

            recipeBundle = getIntent().getExtras();
            recipeName = getIntent().getStringExtra(Constants.RECIPE_NAME);
            recipesSteps = recipeBundle.getParcelableArrayList(Constants.STEPS_LIST);
            recipeIngredients = recipeBundle.getParcelableArrayList(Constants.INGREDIENTS_LIST);
        }

        if (isTablet) {

            RecipeDetails recipeDetailsFragment = (RecipeDetails) getSupportFragmentManager().findFragmentById(R.id.master_list_fragment);
            recipeDetailsFragment.setData(recipesSteps, recipeIngredients);

            if (savedInstanceState == null) {
                Log.d(TAG, "savedInstanceNull");
                RecipeStep recipeStepFragment = new RecipeStep();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.STEPS_LIST, recipesSteps);
                bundle.putInt(Constants.RECIPE_STEP_POSITION, 0);
                recipeStepFragment.setArguments(bundle);
                // Begin the transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // Replace the contents of the container with the new fragment
                ft.replace(R.id.placeholder, recipeStepFragment, Constants.TAG_RECIPE_STEP_FRAGMENT).commit();
            } else {
                RecipeStep recipeStep = (RecipeStep) getSupportFragmentManager().findFragmentByTag(Constants.TAG_RECIPE_STEP_FRAGMENT);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // Replace the contents of the container with the new fragment
                ft.replace(R.id.placeholder, recipeStep).commit();
            }
        } else if (fragment != null) {
                RecipeStep recipeStepFragment = (RecipeStep) fragment;
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // Replace the contents of the container with the new fragment
                ft.replace(R.id.placeholder, recipeStepFragment).commit();
        } else {
            RecipeDetails recipeDetailsFragment = new RecipeDetails();
            recipeBundle.putString(Constants.RECIPE_NAME, recipeName);
            recipeDetailsFragment.setArguments(recipeBundle);
            // Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
            ft.replace(R.id.placeholder, recipeDetailsFragment).commit();
        }

    }


}
