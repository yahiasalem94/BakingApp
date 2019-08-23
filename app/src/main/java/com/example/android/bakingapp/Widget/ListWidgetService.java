package com.example.android.bakingapp.Widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.Models.RecipeIngredients;
import com.example.android.bakingapp.MyApplication;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Utils.SharedPreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class ListWidgetService extends RemoteViewsService {



    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }


}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String ADD_RECIPE = "addedIngredient";

    Context mContext;
    ArrayList<RecipeIngredients> recipeIngredients;

    public ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        recipeIngredients = SharedPreferenceUtil.getIngredientsFromSharedPrefsForKey(ADD_RECIPE, mContext);

    }

    @Override
    public void onDestroy() {
        recipeIngredients = null;
    }

    @Override
    public int getCount() {
        if (recipeIngredients == null) return 0;
        return recipeIngredients.size();
    }


    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (recipeIngredients == null || recipeIngredients.size() == 0) return null;

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_widget);

        views.setTextViewText(R.id.textViewIngredient, recipeIngredients.get(position).getIngredient());
        views.setTextViewText(R.id.textViewQuantity, Double.toString(recipeIngredients.get(position).getQuantity()));
        views.setTextViewText(R.id.textViewIngredient, recipeIngredients.get(position).getMeasure());

        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
//        Bundle extras = new Bundle();
//        extras.putLong(PlantDetailActivity.EXTRA_PLANT_ID, plantId);
//        Intent fillInIntent = new Intent();
//        fillInIntent.putExtras(extras);
//        views.setOnClickFillInIntent(R.id.widget_plant_image, fillInIntent);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

