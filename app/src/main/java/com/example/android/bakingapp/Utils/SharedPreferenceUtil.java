package com.example.android.bakingapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.bakingapp.Models.RecipeIngredients;
import com.example.android.bakingapp.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferenceUtil {

    public static ArrayList<RecipeIngredients> getIngredientsFromSharedPrefsForKey(String key, Context context)
    {
        ArrayList<RecipeIngredients> recipeIngredients;

        SharedPreferences prefs = context.getSharedPreferences("com.example.android.bakingapp.Utils", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        Type type = new TypeToken<ArrayList<RecipeIngredients>>() {}.getType();
        recipeIngredients = gson.fromJson(json, type);

        return recipeIngredients;
    }

    public static boolean setIngredientsToSharedPrefsForKey(String key, ArrayList<RecipeIngredients> ingredients, Context context)
    {
        boolean savedSuccessfully = false;

        SharedPreferences prefs = context.getSharedPreferences("com.example.android.bakingapp.Utils", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(ingredients);

        try
        {
            editor.putString(key, json);
            editor.apply();
            savedSuccessfully = true;
        }
        catch (Exception e)
        {
            savedSuccessfully = false;
        }

        return savedSuccessfully;
    }

    public static void clearAllIngredients(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("com.example.android.bakingapp.Utils", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}