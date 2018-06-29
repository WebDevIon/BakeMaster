package com.example.android.bakemaster.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.example.android.bakemaster.R;
import com.example.android.bakemaster.model.Ingredient;
import com.example.android.bakemaster.model.Recipe;
import com.example.android.bakemaster.model.RecipeAdapter;
import com.example.android.bakemaster.model.Step;
import com.example.android.bakemaster.rest.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        RecipeAdapter.RecipeAdapterOnClickHandler{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String POSITION_KEY = "Step position";
    public static final String STEP_KEY = "Step detail";
    public static final String STEPS_KEY = "Steps";
    public static final String INGREDIENTS_KEY = "Ingredients";
    public static final String RECIPE_NAME_KEY = "Recipe name";
    public static final String IS_TABLET_KEY = "Tablet device";
    static final int NO_POSITION = -1;
    private RecyclerView mRecyclerView;
    private List<Recipe> mRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recipe_rv);

        // Here we check to see if the screen is wider than 600dp, if it is we will display the
        // cards on a 3 column grid.
        if (getScreenWidth() < 600f) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
        } else {
            GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),
                    3, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
        }

        ApiClient.getRecipes().enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call,
                                   @NonNull Response<List<Recipe>> response) {
                mRecipes = response.body();
                RecipeAdapter adapter = new RecipeAdapter(mRecipes, getApplicationContext(),
                        MainActivity.this, MainActivity.this);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Log.e(LOG_TAG, t.toString());
            }
        });
    }

    /**
     * Here we handle clicks on the recipe cards.
     * @param recipe the recipe that was clicked.
     */
    @Override
    public void onClick(Recipe recipe) {
        // Here we create the ArrayList to store the Step and Ingredient objects.
        ArrayList<Step> steps = (ArrayList<Step>) recipe.getSteps();
        ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) recipe.getIngredients();

        // We start a new intent to the RecipeDetailActivity and send the recipe data to it.
        Intent intent = new Intent(getApplicationContext(), RecipeDetailActivity.class);
        intent.putExtra(RECIPE_NAME_KEY, recipe.getName());
        intent.putExtra(STEPS_KEY, steps);
        intent.putExtra(INGREDIENTS_KEY, ingredients);
        startActivity(intent);
    }

    /**
     * This method is used to return the shortest screen side density in dp.
     * @return the screen density.
     */
    private float getScreenWidth() {
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        float dpHeight = outMetrics.heightPixels / density;
        return Math.min(dpWidth, dpHeight);
    }
}
