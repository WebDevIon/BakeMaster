package com.example.android.bakemaster.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recipe_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

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

    @Override
    public void onClick(Recipe recipe) {
        Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show();
    }
}
