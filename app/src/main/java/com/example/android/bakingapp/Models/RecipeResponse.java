package com.example.android.bakingapp.Models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class RecipeResponse implements Serializable {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String recipeName;
    @SerializedName("ingredients")
    private ArrayList<RecipeIngredients> ingredients;
    @SerializedName("steps")
    private ArrayList<RecipeSteps> steps;


    public RecipeResponse() {
        /* Empty Constructor */
    }
    public RecipeResponse(Integer id, String recipeName, ArrayList<RecipeIngredients> ingredients) {
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

    public ArrayList<RecipeIngredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<RecipeIngredients> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<RecipeSteps> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<RecipeSteps> steps) {
        this.steps = steps;
    }

}
