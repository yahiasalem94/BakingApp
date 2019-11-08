package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.bakingapp.Adapters.PageAdapter;
import com.example.android.bakingapp.Models.RecipeIngredients;
import com.example.android.bakingapp.Models.RecipeSteps;
import com.example.android.bakingapp.Utils.Constants;

import java.util.ArrayList;

public class RecipeDetailsMainActivity extends AppCompatActivity {

    private static final String TAG = RecipeDetailsMainActivity.class.getSimpleName();


    public Bundle recipeBundle;
    public ArrayList<RecipeSteps> recipesSteps;
    public ArrayList<RecipeIngredients> recipeIngredients;
    public String recipeName;

    private PageAdapter pageAdapter;

    /*Views*/
    public Toolbar toolbar;
    private TabLayout tabLayout;
    private TabItem tabIngredients;
    private TabItem tabSteps;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tablayout);
        tabIngredients = findViewById(R.id.tabIngredients);
        tabSteps = findViewById(R.id.tabSteps);
        viewPager = findViewById(R.id.viewPager);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));


//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentByTag(Constants.TAG_RECIPE_STEP_FRAGMENT);
//
        if (getIntent().hasExtra(Constants.STEPS_LIST) && getIntent().hasExtra(Constants.INGREDIENTS_LIST)
                && getIntent().hasExtra(Constants.RECIPE_NAME)) {

            recipeBundle = getIntent().getExtras();
            recipeName = getIntent().getStringExtra(Constants.RECIPE_NAME);
            recipesSteps = recipeBundle.getParcelableArrayList(Constants.STEPS_LIST);
            recipeIngredients = recipeBundle.getParcelableArrayList(Constants.INGREDIENTS_LIST);
        }
//
//        if (isTablet) {
//
//            RecipeDetails recipeDetailsFragment = (RecipeDetails) getSupportFragmentManager().findFragmentById(R.id.master_list_fragment);
//            recipeDetailsFragment.setData(recipesSteps, recipeIngredients);
//
//            if (savedInstanceState == null) {
//                Log.d(TAG, "savedInstanceNull");
//                RecipeStep recipeStepFragment = new RecipeStep();
//                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList(Constants.STEPS_LIST, recipesSteps);
//                bundle.putInt(Constants.RECIPE_STEP_POSITION, 0);
//                recipeStepFragment.setArguments(bundle);
//                // Begin the transaction
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                // Replace the contents of the container with the new fragment
//                ft.replace(R.id.placeholder, recipeStepFragment, Constants.TAG_RECIPE_STEP_FRAGMENT).commit();
//            } else {
//                RecipeStep recipeStep = (RecipeStep) getSupportFragmentManager().findFragmentByTag(Constants.TAG_RECIPE_STEP_FRAGMENT);
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                // Replace the contents of the container with the new fragment
//                ft.replace(R.id.placeholder, recipeStep).commit();
//            }
//        } else if (fragment != null) {
//                RecipeStep recipeStepFragment = (RecipeStep) fragment;
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                // Replace the contents of the container with the new fragment
//                ft.replace(R.id.placeholder, recipeStepFragment).commit();
//        } else {
//            RecipeDetails recipeDetailsFragment = new RecipeDetails();
//            recipeBundle.putString(Constants.RECIPE_NAME, recipeName);
//            recipeDetailsFragment.setArguments(recipeBundle);
//            // Begin the transaction
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            // Replace the contents of the container with the new fragment
//            ft.replace(R.id.placeholder, recipeDetailsFragment).commit();
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Toast.makeText(this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
