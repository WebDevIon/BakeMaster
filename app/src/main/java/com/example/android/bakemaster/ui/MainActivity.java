package com.example.android.bakemaster.ui;

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
import android.widget.Toast;

import com.example.android.bakemaster.R;
import com.example.android.bakemaster.model.Recipe;
import com.example.android.bakemaster.model.RecipeAdapter;
import com.example.android.bakemaster.rest.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        RecipeAdapter.RecipeAdapterOnClickHandler{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private List<Recipe> mRecipes = new ArrayList<>();
    private static float sScreenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recipe_rv);

        // Here we make sure that we calculate the screen width only once when the app is launched.
        if (sScreenWidth == 0.0f) {
            sScreenWidth = getScreenWidth();
        }

        // Here we check to see if the screen is wider than 600dp, if it is we will display the
        // cards on a 3 column grid.
        if (sScreenWidth < 600f) {
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
                        MainActivity.this);
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
        Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method is used to return the screen density in dp.
     * @return the screen density.
     */
    private float getScreenWidth() {
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;
        return dpWidth;
    }
}
