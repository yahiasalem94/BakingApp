package com.example.android.bakingapp.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.android.bakingapp.Models.RecipeIngredients;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Utils.SharedPreferenceUtil;

import java.util.ArrayList;

public class IngredientUpdateService extends IntentService {

    public static final String ACTION_UPDATE_RECIPE_WIDGETS = "com.example.android.mygarden.action.update_recipe_widgets";
    private static final String ADD_RECIPE = "addedIngredient";

    public IngredientUpdateService() {
        super("IngredientUpdateService");
    }

    public static void startActionUpdate(Context context) {
        Intent intent = new Intent(context, IngredientUpdateService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE_WIDGETS.equals(action)) {
                handleActionUpdateWidgets();
            }
        }
    }

    private void handleActionUpdateWidgets() {

       ArrayList<RecipeIngredients> recipeIngredients = SharedPreferenceUtil.getIngredientsFromSharedPrefsForKey(ADD_RECIPE, getApplicationContext());

       AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
       int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientWidgetProvider.class));

        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);

        IngredientWidgetProvider.updateWidget(this, appWidgetManager, recipeIngredients.get(0).getIngredient(),
                recipeIngredients.get(0).getQuantity(), recipeIngredients.get(0).getMeasure(), appWidgetIds);
    }
}
