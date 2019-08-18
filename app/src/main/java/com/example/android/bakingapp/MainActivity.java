package com.example.android.bakingapp;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.bakingapp.Models.RecipeIngredients;
import com.example.android.bakingapp.Models.RecipeSteps;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG_RECIPE_STEP_FRAGMENT = "RecipeStep";
    private static final String STEPS_LIST = "stepsList";
    private static final String INGREDIENTS_LIST = "ingredientsList";
    private static final String RECIPE_STEPS = "recipeStep";
    private static final String RECIPE_STEP_POSITION = "recipeStepPosition";


    private Bundle recipeBundle;
    private ArrayList<RecipeSteps> recipesSteps;
    private ArrayList<RecipeIngredients> recipeIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent().hasExtra(STEPS_LIST) && getIntent().hasExtra(INGREDIENTS_LIST)) {
            recipeBundle = getIntent().getExtras();
            recipesSteps = recipeBundle.getParcelableArrayList(STEPS_LIST);
            recipeIngredients = recipeBundle.getParcelableArrayList(INGREDIENTS_LIST);
        }

        if (findViewById(R.id.master_list_fragment) != null) {

            RecipeDetails recipeDetailsFragment = (RecipeDetails) getSupportFragmentManager().findFragmentById(R.id.master_list_fragment);
            recipeDetailsFragment.setData(recipesSteps, recipeIngredients);

            if (savedInstanceState == null) {
                Log.d(TAG, "savedInstanceNull");
                RecipeStep recipeStepFragment = new RecipeStep();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(RECIPE_STEPS, recipesSteps);
                bundle.putInt(RECIPE_STEP_POSITION, 0);
                recipeStepFragment.setArguments(bundle);
                // Begin the transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // Replace the contents of the container with the new fragment
                ft.replace(R.id.placeholder, recipeStepFragment, TAG_RECIPE_STEP_FRAGMENT).commit();
            } else {
                RecipeStep fragment = (RecipeStep) getSupportFragmentManager().findFragmentByTag(TAG_RECIPE_STEP_FRAGMENT);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // Replace the contents of the container with the new fragment
                ft.replace(R.id.placeholder, fragment).commit();
            }
        } else {
            RecipeDetails fragment = new RecipeDetails();
            fragment.setArguments(recipeBundle);
            // Begin the transaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
            ft.replace(R.id.placeholder, fragment).commit();
        }

    }


}
