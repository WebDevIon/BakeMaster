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
import com.example.android.bakemaster.database.AppExecutors;
import com.example.android.bakemaster.database.RecipeDatabase;
import com.example.android.bakemaster.model.Recipe;
import com.example.android.bakemaster.rest.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The configuration screen for the {@link IngredientListWidget IngredientListWidget} AppWidget.
 */
public class IngredientListWidgetConfigureActivity extends Activity {

    private static final String LOG_TAG = IngredientListWidgetConfigureActivity.class.getSimpleName();
    private static final String PREFS_NAME = "com.example.android.bakemaster.widget.IngredientListWidget";
    private static final String PREF_RECIPE_NAME_PREFIX_KEY = "appwidget_recipe_name_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    ListView mRecipeListView;
    List<Recipe> mRecipes = new ArrayList<>();
    private RecipeDatabase mDb;

    AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            final Context context = IngredientListWidgetConfigureActivity.this;

            // When the view is clicked, store the string locally
            final String recipeName = mRecipes.get(position).getName();
            saveRecipeData(context, mAppWidgetId, recipeName);

            // The recipe that was clicked will be stored in the database.
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (mDb.recipeDao().loadRecipeObject(recipeName) == null) {
                        mDb.recipeDao().insertTask(mRecipes.get(position));
                        Log.d(LOG_TAG, "Added recipe.");
                    } else {
                        Log.d(LOG_TAG, "Existing recipe.");
                    }
                }
            });

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
    static void saveRecipeData(Context context, int appWidgetId, String name) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_RECIPE_NAME_PREFIX_KEY + appWidgetId, name);
        prefs.apply();
    }

    static String loadRecipeName(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String recipeName = prefs.getString(PREF_RECIPE_NAME_PREFIX_KEY + appWidgetId, null);
        if (recipeName != null) {
            return recipeName;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteRecipePref(Context context, int appWidgetId) {
        Log.d(LOG_TAG, "Recipe with id: " + appWidgetId + " deleted!");
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_RECIPE_NAME_PREFIX_KEY + appWidgetId);
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
        mDb = RecipeDatabase.getInstance(getApplicationContext());

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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
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

    }

}

