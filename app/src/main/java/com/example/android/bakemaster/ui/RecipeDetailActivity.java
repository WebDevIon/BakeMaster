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
 * In this class we create the activity that will hold the recipe detail fragment.
 */
public class RecipeDetailActivity extends AppCompatActivity{

    private boolean isTablet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Here we check to see if the device is a tablet or a phone.
        if (findViewById(R.id.tablet_layout) != null) {
            isTablet = true;
        }

        // Here we get the data transferred from MainActivity
        Intent intent = getIntent();
        String recipeName = intent.getStringExtra(MainActivity.RECIPE_NAME_KEY);
        ArrayList<Step> steps = intent.getParcelableArrayListExtra(MainActivity.STEPS_KEY);
        ArrayList<Ingredient> ingredients =
                intent.getParcelableArrayListExtra(MainActivity.INGREDIENTS_KEY);

        // We bundle the data that we need in the fragment.
        Bundle b = new Bundle();
        b.putString(MainActivity.RECIPE_NAME_KEY, recipeName);
        b.putParcelableArrayList(MainActivity.STEPS_KEY, steps);
        b.putParcelableArrayList(MainActivity.INGREDIENTS_KEY, ingredients);
        b.putBoolean(MainActivity.IS_TABLET_KEY, isTablet);

        // Here we set the title of the activity accordingly to the recipe name.
        setTitle(recipeName);

        // We create a new fragment and we put the bundled data into it.
        Fragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(b);

        // Here we make sure to pass the MasterListFragment that has the data in it.
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_detail_fragment, recipeDetailFragment)
                .commit();
    }
}
