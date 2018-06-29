package com.example.android.bakemaster.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.example.android.bakemaster.model.Recipe;

/**
 * This class is responsible for creating the Room Database.
 */
@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
@TypeConverters({IngredientTypeConverter.class, StepTypeConverter.class})
public abstract class RecipeDatabase extends RoomDatabase {

    private static final String LOG_TAG = RecipeDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "recipe_database";
    private static RecipeDatabase sInstance;

    // Here we make sure we only have one instance of the Database.
    public static RecipeDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        RecipeDatabase.class, RecipeDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract RecipeDao recipeDao();
}
