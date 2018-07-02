package com.example.android.bakemaster.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakemaster.R;

/**
 * In this class we create the activity that will hold the recipe ingredients fragment.
 */
public class RecipeIngredientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients);

        // Here we get the data transferred from RecipeDetailFragment and set the title.
        Intent intent = getIntent();
        setTitle(intent.getStringExtra(MainActivity.RECIPE_NAME_KEY));

        // We bundle the data that we need in the fragment.
        Bundle b = new Bundle();
        b.putParcelableArrayList(MainActivity.INGREDIENTS_KEY,
                intent.getParcelableArrayListExtra(MainActivity.INGREDIENTS_KEY));

        // We create a new fragment and we put the bundled data into it.
        Fragment recipeIngredientFragment = new RecipeIngredientFragment();
        recipeIngredientFragment.setArguments(b);

        // Here we make sure to pass the MasterListFragment that has the data in it.
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.ingredients_list_fragment, recipeIngredientFragment)
                .commit();

        //Here we provide navigation to the parent activity.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
