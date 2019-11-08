package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

public class IngredientsFragment extends Fragment implements RecipeDetailsAdapter.RecipeDetailsAdapterOnClickHandler {

    private static final String TAG = IngredientsFragment.class.getSimpleName();

    private RecyclerView recipesIngredientRecyclerView;
    private LinearLayoutManager layoutManagerIngredients;
    private RecipeIngredientsAdapter recipeIngredientsAdapter;

    private View mRootview;
    private RadioButton addButton;
    private TextView recipeName;
    private Toolbar toolbar;
    private ArrayList<RecipeSteps> recipesSteps;
    private ArrayList<RecipeIngredients> recipeIngredients;
    private String mRecipeName;
    private ArrayList<RecipeIngredients> savedIngredients = null;

    boolean isSaved = false;

    public IngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeIngredientsAdapter = new RecipeIngredientsAdapter();

        toolbar = ((RecipeDetailsMainActivity) getActivity()).toolbar;


        recipesSteps = ((RecipeDetailsMainActivity) getActivity()).recipesSteps;//getArguments().getParcelableArrayList(Constants.STEPS_LIST);
        recipeIngredients = ((RecipeDetailsMainActivity) getActivity()).recipeIngredients;//getArguments().getParcelableArrayList(Constants.INGREDIENTS_LIST);
        mRecipeName = ((RecipeDetailsMainActivity) getActivity()).recipeName;//getArguments().getString(Constants.RECIPE_NAME);

        savedIngredients = SharedPreferenceUtil.getIngredientsFromSharedPrefsForKey(Constants.ADDED_INGREDIENT, getActivity().getApplicationContext());
        if (savedIngredients != null) {
            if (savedIngredients.get(0).getIngredient().equals(recipeIngredients.get(0).getIngredient())) {
                isSaved = true;
            }
        }

        recipeIngredientsAdapter.setRecipeIngredients(recipeIngredients);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment's layout
        mRootview = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        recipesIngredientRecyclerView = mRootview.findViewById(R.id.recipesIngredientsRecyclerView);
        addButton = mRootview.findViewById(R.id.add_button);

        toolbar.setTitle(mRecipeName);

        addButton.setChecked(isSaved);

        setupRecyclerView();

        return mRootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();

                if (checked) {
                    SharedPreferenceUtil.clearAll(getActivity().getApplicationContext());
                    SharedPreferenceUtil.setRecipeNameToSharedPrefsForKey(Constants.ADDED_RECIPE_NAME, mRecipeName, getActivity().getApplicationContext());
                    SharedPreferenceUtil.setIngredientsToSharedPrefsForKey(Constants.ADDED_INGREDIENT, recipeIngredients, getActivity().getApplicationContext());
                    SharedPreferenceUtil.setRecipeStepsToSharedPrefsForKey(Constants.ADDED_STEPS, recipesSteps, getActivity().getApplicationContext());
                    IngredientUpdateService.startActionUpdate(getActivity().getApplicationContext());
                }
            }
        });

    }

    @Override
    public void onClick(int position) {

        /*Log.d(TAG, recipesSteps.get(position).getDescription());
        RecipeStep fragment = new RecipeStep();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.RECIPE_STEP_POSITION, position);
        bundle.putParcelableArrayList(Constants.STEPS_LIST, recipesSteps);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.placeholder, fragment, Constants.TAG_RECIPE_STEP_FRAGMENT);
        transaction.commit();*/
    }

    private void setupRecyclerView() {
        layoutManagerIngredients = new LinearLayoutManager(getContext());
        recipesIngredientRecyclerView.setLayoutManager(layoutManagerIngredients);
        recipesIngredientRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // Set data adapter.
        recipesIngredientRecyclerView.setAdapter(recipeIngredientsAdapter);
    }

//    public void setData(ArrayList<RecipeSteps> recipesSteps, ArrayList<RecipeIngredients> recipeIngredients) {
//        this.recipesSteps = recipesSteps;
//        this.recipeIngredients = recipeIngredients;
//
////        recipesStepsAdapter.setRecipeSteps(recipesSteps);
//        recipeIngredientsAdapter.setRecipeIngredients(recipeIngredients);
//    }
}
