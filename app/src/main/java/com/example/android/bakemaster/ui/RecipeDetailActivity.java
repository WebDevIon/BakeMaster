package com.example.android.bakemaster.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakemaster.R;
import com.example.android.bakemaster.model.Ingredient;
import com.example.android.bakemaster.model.Step;

import java.util.ArrayList;

/**
 * In this class we create the activity that will hold the recipe detail steps and ingredients.
 */
public class RecipeDetailActivity extends AppCompatActivity{

    private boolean isTablet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (findViewById(R.id.tablet_layout) != null) {
            isTablet = true;
        }

        Intent intent = getIntent();
        String recipeName = intent.getStringExtra(MainActivity.RECIPE_NAME_KEY);
        ArrayList<Step> steps = intent.getParcelableArrayListExtra(MainActivity.STEPS_KEY);
        ArrayList<Ingredient> ingredients =
                intent.getParcelableArrayListExtra(MainActivity.INGREDIENTS_KEY);

        Bundle b = new Bundle();
        b.putString(MainActivity.RECIPE_NAME_KEY, recipeName);
        b.putParcelableArrayList(MainActivity.STEPS_KEY, steps);
        b.putParcelableArrayList(MainActivity.INGREDIENTS_KEY, ingredients);
        b.putBoolean(MainActivity.IS_TABLET_KEY, isTablet);

        // Here we set the title of the activity accordingly to the recipe name.
        setTitle(recipeName);

        Fragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(b);

        // Here we make sure to pass the MasterListFragment that we created in MainActivity.
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.master_list_fragment, recipeDetailFragment)
                .commit();
    }
}
