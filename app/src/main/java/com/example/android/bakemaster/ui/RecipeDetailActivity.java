package com.example.android.bakemaster.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakemaster.R;

/**
 * In this class we create the activity that will hold the recipe detail steps and ingredients.
 */
public class RecipeDetailActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Here we make sure to pass the MasterListFragment that we created in MainActivity.
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.master_list_fragment, MainActivity.mMasterListFragment)
                .commit();
    }
}
