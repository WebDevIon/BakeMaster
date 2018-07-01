package com.example.android.bakemaster.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.bakemaster.model.Recipe;

import java.util.List;

/**
 * This interface is used to create the CRUD operations on the Database.
 */
@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipes ORDER BY id")
    LiveData<List<Recipe>> loadAllRecipes();

    @Query("SELECT * FROM recipes WHERE name LIKE :recipeName")
    LiveData<Recipe> loadRecipe(String recipeName);

    @Query("SELECT * FROM recipes WHERE name LIKE :recipeName")
    Recipe loadRecipeObject(String recipeName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Recipe recipe);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Recipe recipe);

    @Delete
    void deleteTask(Recipe recipe);

}
