package com.example.android.bakemaster.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakemaster.R;
import com.example.android.bakemaster.model.Ingredient;
import com.example.android.bakemaster.model.Recipe;
import com.example.android.bakemaster.model.Step;

import java.util.ArrayList;

/**
 * In this class we create the activity that will hold the recipe detail fragment.
 */
public class RecipeDetailActivity extends AppCompatActivity{

    private boolean isTablet;
    private static Integer position;
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private TextView nextButtonTv;
    private TextView previousButtonTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Here we initialize the next and previous buttons
        nextButtonTv = findViewById(R.id.next_recipe_button);
        previousButtonTv = findViewById(R.id.previous_recipe_button);

        // Here we check to see if the device is a tablet or a phone.
        if (findViewById(R.id.tablet_layout) != null) {
            isTablet = true;
        }

        // Here we get the data transferred from MainActivity
        Intent intent = getIntent();
        recipes = intent.getParcelableArrayListExtra(MainActivity.RECIPES_KEY);
        if (savedInstanceState == null) {
            position = intent.getIntExtra(MainActivity.POSITION_KEY, MainActivity.NO_POSITION);
        }

        createFragment(recipes, position);

        nextButtonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here we check to see if the position is not the last one in the ArrayList
                // if it is not then we move to the next element and recreate the fragment.
                if (position != MainActivity.NO_POSITION && position < recipes.size() - 1) {
                    position++;
                    createFragment(recipes, position);
                }
            }
        });

        previousButtonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here we check to see if the position is not the first one in the ArrayList
                // if it is not then we move to the previous element and recreate the fragment.
                if (position != MainActivity.NO_POSITION && position > 0) {
                    position--;
                    createFragment(recipes, position);
                }
            }
        });
    }

    /**
     * This method is responsible for creating RecipeDetailFragments according to the steps and
     * the position that we pass to it. The method also hides the next or previous TextViews when
     * the element is at the last or first index in the ArrayList.
     * @param recipes the ArrayList of Step objects.
     * @param position the position of the current Step object in the ArrayList.
     */
    public void createFragment(ArrayList<Recipe> recipes, int position) {
        String recipeName = recipes.get(position).getName();
        ArrayList<Step> steps = (ArrayList<Step>) recipes.get(position).getSteps();
        ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>)
                recipes.get(position).getIngredients();

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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_detail_fragment, recipeDetailFragment)
                .commit();

        // If the Recipe is the last one in the ArrayList then we set the visibility of the
        // next TextView to invisible, if it is the first one in the ArrayList then we set the
        // visibility of the previous TextView to invisible. If the Step object is in any other
        // position we set the visibility of the two TextViews to Visible.
        if (position == recipes.size() - 1) {
            nextButtonTv.setVisibility(View.INVISIBLE);
        } else if (position == 0){
            previousButtonTv.setVisibility(View.INVISIBLE);
        } else {
            nextButtonTv.setVisibility(View.VISIBLE);
            previousButtonTv.setVisibility(View.VISIBLE);
        }
    }
}
