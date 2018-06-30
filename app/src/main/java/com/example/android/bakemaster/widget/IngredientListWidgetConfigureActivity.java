package com.example.android.bakemaster.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.bakemaster.R;
import com.example.android.bakemaster.model.Ingredient;
import com.example.android.bakemaster.model.Recipe;
import com.example.android.bakemaster.rest.ApiClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The configuration screen for the {@link IngredientListWidget IngredientListWidget} AppWidget.
 */
public class IngredientListWidgetConfigureActivity extends Activity {

    private static final String LOG_TAG = IngredientListWidgetConfigureActivity.class.getSimpleName();
    private static final String PREFS_NAME = "com.example.android.bakemaster.widget.IngredientListWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    ListView mRecipeListView;
    List<Recipe> mRecipes = new ArrayList<>();

    AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Context context = IngredientListWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            List<Ingredient> ingredients = mRecipes.get(position).getIngredients();
            Set<String> widgetText = new HashSet<>();
            for (Ingredient ingredient : ingredients) {
                widgetText.add(ingredient.getIngredient());
            }
            saveTitlePref(context, mAppWidgetId, widgetText);

            // Here we add the ingredient list of objects that we pass in the intent.
            ArrayList<Ingredient> ingredientList=
                    (ArrayList<Ingredient>) mRecipes.get(position).getIngredients();

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            IngredientListWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public IngredientListWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, Set<String> text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        //prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.putStringSet(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static Set<String> loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Set<String> titleValue = prefs.getStringSet(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            Set<String> baseline = new HashSet<String>();
            baseline.add(context.getString(R.string.appwidget_text));
            return baseline;
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.ingredient_list_widget_configure);
        mRecipeListView = findViewById(R.id.recipe_list_widget_config_lv);

        ApiClient.getRecipes().enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call,
                                   @NonNull Response<List<Recipe>> response) {
                mRecipes = response.body();
                ArrayList<String> recipeNames = new ArrayList<>();
                if (!mRecipes.isEmpty()){
                    for (Recipe recipe : mRecipes) {
                        recipeNames.add(recipe.getName());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            getApplicationContext(), R.layout.list_item_widget, recipeNames);
                    mRecipeListView.setAdapter(arrayAdapter);
                    mRecipeListView.setOnItemClickListener(mOnClickListener);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Log.e(LOG_TAG, t.toString());
            }
        });

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        //mAppWidgetText.setText(loadTitlePref(IngredientListWidgetConfigureActivity.this, mAppWidgetId));
    }
}

