package com.example.android.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class RecipesTest {

    public static final String RECIPE_NAME = "Cheesecake";

    @Rule
    public ActivityTestRule<Recipes> mActivityTestRule = new ActivityTestRule<>(Recipes.class);

    @Test
    public void clickRecipeRecyclerViewItem_OpensOrderActivity() {

        onView(ViewMatchers.withId(R.id.recipesRecyclerView)).inRoot(RootMatchers.withDecorView(Matchers
                .is(mActivityTestRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        // Checks that the OrderActivity opens with the correct tea name displayed
        onView(withId(R.id.recipeName)).check(matches(withText(RECIPE_NAME)));

    }



}
