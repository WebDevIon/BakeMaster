package com.example.android.bakemaster.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.bakemaster.database.RecipeDatabase;

public class MainViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MainViewModel.class.getSimpleName();
    private RecipeDatabase database;

    public MainViewModel(@NonNull Application application) {
        super(application);

        Log.d(LOG_TAG, "Actively retrieving the tasks from the DataBase");
        database = RecipeDatabase.getInstance(application);
    }

    public LiveData<Recipe> getRecipe(String recipeName) {
        return database.recipeDao().loadRecipe(recipeName);
    }

}
