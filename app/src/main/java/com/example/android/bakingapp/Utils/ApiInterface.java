package com.example.android.bakingapp.Utils;

import com.example.android.bakingapp.Models.RecipeResponse;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiInterface {

    @GET("59121517_baking/baking.json")
    Call<ArrayList<RecipeResponse>> getRecipes();
}