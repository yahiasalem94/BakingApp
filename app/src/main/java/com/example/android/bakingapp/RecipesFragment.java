package com.example.android.bakingapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.Adapters.RecipesAdapter;
import com.example.android.bakingapp.Models.RecipeResponse;
import com.example.android.bakingapp.Utils.ApiInterface;
import com.example.android.bakingapp.Utils.NetworkUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipesFragment extends Fragment  implements RecipesAdapter.RecipesAdapterOnClickHandler  {

    private static final String TAG = RecipesFragment.class.getSimpleName();


    private RecyclerView recipesRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;

    private LinearLayoutManager layoutManager;
    private RecipesAdapter recipesAdapter;

    private ApiInterface apiService;

    private ArrayList<RecipeResponse> recipesList;
    private boolean isConnected;

    private static final String STEPS_LIST = "stepsList";
    private static final String INGREDIENTS_LIST = "ingredientsList";

    public RecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipesAdapter = new RecipesAdapter(this);
        apiService = NetworkUtils.getRetrofitInstance().create(ApiInterface.class);
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);

        mLoadingIndicator = rootView.findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = rootView.findViewById(R.id.tv_error_message_display);
        // Inflate the fragment's layout
        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Create the recyclerview.
        recipesRecyclerView = view.findViewById(R.id.recipesRecyclerView);
        layoutManager = new LinearLayoutManager(view.getContext());
        recipesRecyclerView.setLayoutManager(layoutManager);

        // Set data adapter.
        recipesRecyclerView.setAdapter(recipesAdapter);
    }

    @Override
    public void onClick(int position) {
        Log.d(TAG, recipesList.get(position).getRecipeName());
        RecipeDetails fragment = new RecipeDetails();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEPS_LIST, recipesList.get(position).getSteps());
        bundle.putParcelableArrayList(INGREDIENTS_LIST, recipesList.get(position).getIngredients());
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.placeholder, fragment);
        transaction.commit();
    }

    public static void onConnectionChange(boolean connected) {
        Log.i(TAG, "Connection is" + " " + connected);
    }
    /* Local Functions */
    private void showDataView() {
        /* First, make sure the error is invisible */
      //  mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        //recipesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {

        /* First, hide the currently visible data */
        recipesRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void loadData() {

        showDataView();
//        mLoadingIndicator.setVisibility(View.VISIBLE);

        Call<ArrayList<RecipeResponse>> call = apiService.getRecipes();

        call.enqueue(new Callback<ArrayList<RecipeResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<RecipeResponse>> call, Response<ArrayList<RecipeResponse>> response) {

                recipesList = response.body();
//                mLoadingIndicator.setVisibility(View.INVISIBLE);
                recipesAdapter.setMoviesData(recipesList);
//                recipesRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }

            @Override
            public void onFailure(Call<ArrayList<RecipeResponse>> call, Throwable t) {

                Log.e(TAG, t.toString());
//                mLoadingIndicator.setVisibility(View.INVISIBLE);
                showErrorMessage();

            }
        });
    }
}
