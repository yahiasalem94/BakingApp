package com.example.android.bakingapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.Adapters.RecipesAdapter;
import com.example.android.bakingapp.Models.RecipeResponse;
import com.example.android.bakingapp.Utils.ApiInterface;
import com.example.android.bakingapp.Utils.Constants;
import com.example.android.bakingapp.Utils.InternetConnectionReceiver;
import com.example.android.bakingapp.Utils.NetworkUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Recipes extends AppCompatActivity implements RecipesAdapter.RecipesAdapterOnClickHandler,
        InternetConnectionReceiver.NetworkStateReceiverListener {

    private static final String TAG = Recipes.class.getSimpleName();


    private RecyclerView recipesRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;

    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private RecipesAdapter recipesAdapter;

    private ApiInterface apiService;

    private ArrayList<RecipeResponse> recipesList;
    private boolean isConnected;
    private static float dpWidth;
    private DisplayMetrics displayMetrics;

    private static final float TABLET_MIN_WIDTH = 600;

    private static final String KEY_RECYCLER_STATE = "recycler_state";
    private Parcelable listState;
    private static Bundle mBundleRecyclerViewState;

    private InternetConnectionReceiver mInternetConnectionReciever;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipes);

        mInternetConnectionReciever = new InternetConnectionReceiver();
        mInternetConnectionReciever.addListener(this);
        registerReceiver(mInternetConnectionReciever,new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        recipesRecyclerView = findViewById(R.id.recipesRecyclerView);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        displayMetrics = this.getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        recipesAdapter = new RecipesAdapter(this);
        initializeRecyclerView();

        apiService = NetworkUtils.getRetrofitInstance().create(ApiInterface.class);
        loadData();

    }

    @Override
    public void onClick(int position) {

        Log.d(TAG, recipesList.get(position).getRecipeName());
        Intent intent = new Intent(Recipes.this, RecipeDetailsMainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RECIPE_NAME, recipesList.get(position).getRecipeName());
        bundle.putParcelableArrayList(Constants.STEPS_LIST, recipesList.get(position).getSteps());
        bundle.putParcelableArrayList(Constants.INGREDIENTS_LIST, recipesList.get(position).getIngredients());
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void networkAvailable(boolean isAvailable) {
        if (!isAvailable) {
            showErrorMessage();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause");
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        listState = recipesRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Log.d(TAG, "onResume");
        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recipesRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInternetConnectionReciever.removeListener(this);
        unregisterReceiver(mInternetConnectionReciever);
    }


    /* Local Functions */

    private void initializeRecyclerView() {

        if (dpWidth >= TABLET_MIN_WIDTH) {
            gridLayoutManager = new GridLayoutManager(this, calculateNoOfColumns());
            recipesRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            linearLayoutManager = new LinearLayoutManager(this);
            recipesRecyclerView.setLayoutManager(linearLayoutManager);
        }

        // Set data adapter.
        recipesRecyclerView.setAdapter(recipesAdapter);
    }

    private void showDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        recipesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        recipesRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private static int calculateNoOfColumns() {

        Log.d(TAG, dpWidth+"");
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    private void loadData() {

        showDataView();
        mLoadingIndicator.setVisibility(View.VISIBLE);

        Call<ArrayList<RecipeResponse>> call = apiService.getRecipes();

        call.enqueue(new Callback<ArrayList<RecipeResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<RecipeResponse>> call, Response<ArrayList<RecipeResponse>> response) {

                Log.d(TAG, response.body().get(0).getRecipeName());
                recipesList = response.body();
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                recipesAdapter.setRecipesData(recipesList);
                recipesRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }

            @Override
            public void onFailure(Call<ArrayList<RecipeResponse>> call, Throwable t) {

                Log.e(TAG, t.toString());
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                showErrorMessage();

            }
        });
    }
}
