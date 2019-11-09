package com.example.android.bakingapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.bakingapp.Models.RecipeIngredients;
import com.example.android.bakingapp.Models.RecipeSteps;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferenceUtil {

    public static ArrayList<RecipeIngredients> getIngredientsFromSharedPrefsForKey(String key, Context context)
    {
        ArrayList<RecipeIngredients> recipeIngredients;

        SharedPreferences prefs = context.getSharedPreferences("com.example.android.bakingapp.Utils", Context.MODE_PRIVATE);
        String json = prefs.getString(key, "");
        Type type = new TypeToken<ArrayList<RecipeIngredients>>() {}.getType();

        recipeIngredients = (ArrayList) Serializer.deSerializeList(json,type);

        return recipeIngredients;
    }

    public static ArrayList<RecipeSteps> getRecipeStepsFromSharedPrefsForKey(String key, Context context)
    {
        ArrayList<RecipeSteps> recipeSteps;

        SharedPreferences prefs = context.getSharedPreferences("com.example.android.bakingapp.Utils", Context.MODE_PRIVATE);
        String json = prefs.getString(key, "");
        Type type = new TypeToken<ArrayList<RecipeSteps>>() {}.getType();
        recipeSteps = (ArrayList) Serializer.deSerializeList(json,type);

        return recipeSteps;
    }

    public static String getRecipeNameFromSharedPrefsForKey(String key, Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("com.example.android.bakingapp.Utils", Context.MODE_PRIVATE);
        String name = prefs.getString(key, "");

        return name;
    }

    public static boolean setIngredientsToSharedPrefsForKey(String key, ArrayList<RecipeIngredients> ingredients, Context context)
    {
        boolean savedSuccessfully = false;

        SharedPreferences prefs = context.getSharedPreferences("com.example.android.bakingapp.Utils", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String json = Serializer.serialize(ingredients);

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

    public static boolean removeIngredientsToSharedPrefsForKey(String key, Context context)
    {
        boolean removedSuccessfully = false;

        SharedPreferences prefs = context.getSharedPreferences("com.example.android.bakingapp.Utils", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        try
        {
            editor.remove(key);
            editor.apply();
            removedSuccessfully = true;
        }
        catch (Exception e)
        {
            removedSuccessfully = false;
        }

        return removedSuccessfully;
    }

    public static boolean setRecipeStepsToSharedPrefsForKey(String key, ArrayList<RecipeSteps> steps, Context context)
    {
        boolean savedSuccessfully = false;

        SharedPreferences prefs = context.getSharedPreferences("com.example.android.bakingapp.Utils", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String json = Serializer.serialize(steps);

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

    public static boolean removeRecipeStepsToSharedPrefsForKey(String key, Context context)
    {
        boolean removedSuccessfully = false;

        SharedPreferences prefs = context.getSharedPreferences("com.example.android.bakingapp.Utils", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        try
        {
            editor.remove(key);
            editor.apply();
            removedSuccessfully = true;
        }
        catch (Exception e)
        {
            removedSuccessfully = false;
        }

        return removedSuccessfully;
    }

    public static boolean setRecipeNameToSharedPrefsForKey(String key, String recipeName, Context context)
    {
        boolean savedSuccessfully = false;

        SharedPreferences prefs = context.getSharedPreferences("com.example.android.bakingapp.Utils", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        try
        {
            editor.putString(key, recipeName);
            editor.apply();
            savedSuccessfully = true;
        }
        catch (Exception e)
        {
            savedSuccessfully = false;
        }

        return savedSuccessfully;
    }

    public static boolean removeRecipeNameToSharedPrefsForKey(String key, Context context)
    {
        boolean removedSuccessfully = false;

        SharedPreferences prefs = context.getSharedPreferences("com.example.android.bakingapp.Utils", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        try
        {
            editor.remove(key);
            editor.apply();
            removedSuccessfully = true;
        }
        catch (Exception e)
        {
            removedSuccessfully = false;
        }

        return removedSuccessfully;
    }

    public static void clearAll(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("com.example.android.bakingapp.Utils", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}