package com.example.android.bakingapp.Models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class RecipeData implements Serializable {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String recipeName;
    @SerializedName("ingredients")
    private ArrayList<Ingredients> ingredients;


    public RecipeData() {
        /* Empty Constructor */
    }
    public RecipeData(Integer id, String recipeName, ArrayList<Ingredients> ingredients) {
        this.id = id;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public ArrayList<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

}
