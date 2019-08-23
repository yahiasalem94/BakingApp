package com.example.android.bakingapp;

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

import com.example.android.bakingapp.Adapters.RecipeDetailsAdapter;
import com.example.android.bakingapp.Adapters.RecipeIngredientsAdapter;
import com.example.android.bakingapp.Models.RecipeIngredients;
import com.example.android.bakingapp.Models.RecipeSteps;
import com.example.android.bakingapp.Utils.SharedPreferenceUtil;
import com.example.android.bakingapp.Widget.IngredientUpdateService;

import java.util.ArrayList;

public class RecipeDetails extends Fragment implements RecipeDetailsAdapter.RecipeDetailsAdapterOnClickHandler {

    private static final String TAG = RecipeDetails.class.getSimpleName();
    private static final String TAG_RECIPE_STEP_FRAGMENT = "RecipeStep";
    private static final String ADD_RECIPE = "addedIngredient";

    private RecyclerView recipesStepsRecyclerView;
    private LinearLayoutManager layoutManagerSteps;
    private RecipeDetailsAdapter recipesStepsAdapter;

    private RecyclerView recipesIngredientRecyclerView;
    private LinearLayoutManager layoutManagerIngredients;
    private RecipeIngredientsAdapter recipeIngredientsAdapter;

    private RadioButton addButton;

    private ArrayList<RecipeSteps> recipesSteps;
    private ArrayList<RecipeIngredients> recipeIngredients;
    private ArrayList<RecipeIngredients> savedIngredients;

    boolean isSaved = false;

    private static final String STEPS_LIST = "stepsList";
    private static final String INGREDIENTS_LIST = "ingredientsList";
    private static final String RECIPE_STEPS = "recipeStep";
    private static final String RECIPE_STEP_POSITION = "recipeStepPosition";

    public RecipeDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipesStepsAdapter = new RecipeDetailsAdapter(this);
        recipeIngredientsAdapter = new RecipeIngredientsAdapter();

        if (getArguments() != null) {

            if (getArguments().containsKey(STEPS_LIST)) {
                recipesSteps = getArguments().getParcelableArrayList(STEPS_LIST);
            }

            if (getArguments().containsKey(INGREDIENTS_LIST)) {
                recipeIngredients = getArguments().getParcelableArrayList(INGREDIENTS_LIST);
            }
        }

        savedIngredients = SharedPreferenceUtil.getIngredientsFromSharedPrefsForKey(ADD_RECIPE, getActivity().getApplicationContext());
        if (savedIngredients != null) {
            if (savedIngredients.get(0).getIngredient().equals(recipeIngredients.get(0).getIngredient())) {
                isSaved = true;
            }
        }


        recipesStepsAdapter.setRecipeSteps(recipesSteps);
        recipeIngredientsAdapter.setRecipeIngredients(recipeIngredients);
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

        recipesIngredientRecyclerView = view.findViewById(R.id.recipesIngredientsRecyclerView);
        layoutManagerIngredients = new LinearLayoutManager(view.getContext());
        recipesIngredientRecyclerView.setLayoutManager(layoutManagerIngredients);

        addButton = view.findViewById(R.id.add_button);
        addButton.setChecked(isSaved);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();

                if (checked) {
                    SharedPreferenceUtil.clearAllIngredients(getActivity().getApplicationContext());
                    SharedPreferenceUtil.setIngredientsToSharedPrefsForKey(ADD_RECIPE, recipeIngredients, getActivity().getApplicationContext());
                    IngredientUpdateService.startActionWaterPlant(getActivity().getApplicationContext());
                }
            }
        });

        // Set data adapter.
        recipesStepsRecyclerView.setAdapter(recipesStepsAdapter);
        recipesIngredientRecyclerView.setAdapter(recipeIngredientsAdapter);
    }

    @Override
    public void onClick(int position) {

        Log.d(TAG, recipesSteps.get(position).getDescription());
        RecipeStep fragment = new RecipeStep();
        Bundle bundle = new Bundle();
        bundle.putInt(RECIPE_STEP_POSITION, position);
        bundle.putParcelableArrayList(RECIPE_STEPS, recipesSteps);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.placeholder, fragment, TAG_RECIPE_STEP_FRAGMENT);
        transaction.commit();
    }

    public void setData(ArrayList<RecipeSteps> recipesSteps, ArrayList<RecipeIngredients> recipeIngredients) {
        this.recipesSteps = recipesSteps;
        this.recipeIngredients = recipeIngredients;

        recipesStepsAdapter.setRecipeSteps(recipesSteps);
        recipeIngredientsAdapter.setRecipeIngredients(recipeIngredients);
    }
}
