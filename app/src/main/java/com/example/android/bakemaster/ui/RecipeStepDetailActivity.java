package com.example.android.bakemaster.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakemaster.R;
import com.example.android.bakemaster.model.Step;

import java.util.ArrayList;

/**
 * In this class we create the activity that will hold the recipe step detail fragment.
 */
public class RecipeStepDetailActivity extends AppCompatActivity {

    private ArrayList<Step> steps = new ArrayList<>();
    private Integer position;
    private TextView nextButtonTv;
    private TextView previousButtonTv;
    private boolean fragmentWasAdded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        // Here we initialize the next and previous buttons
        nextButtonTv = findViewById(R.id.next_step_button);
        previousButtonTv = findViewById(R.id.previous_step_button);

        // We get the title, steps ArrayList and position of the step that was clicked.
        Intent intent = getIntent();
        setTitle(intent.getStringExtra(MainActivity.RECIPE_NAME_KEY));
        steps = intent.getParcelableArrayListExtra(MainActivity.STEPS_KEY);
        position = intent.getIntExtra(MainActivity.POSITION_KEY, MainActivity.NO_POSITION);

        // We create the fragment using the steps and the position that we got.
        createFragment(steps, position);

        // These click listeners are placed on the next and previous buttons
        nextButtonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here we check to see if the position is not the last one in the ArrayList
                // if it is not then we move to the next element and recreate the fragment.
                if (position != MainActivity.NO_POSITION && position < steps.size() - 1) {
                    position++;
                    createFragment(steps, position);
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
                    createFragment(steps, position);
                }
            }
        });
    }

    /**
     * This method is responsible for creating RecipeDetailFragments according to the steps and
     * the position that we pass to it. The method also hides the next or previous TextViews when
     * the element is at the last or first index in the ArrayList.
     * @param steps the ArrayList of Step objects.
     * @param position the position of the current Step object in the ArrayList.
     */
    public void createFragment(ArrayList<Step> steps, int position) {
        Bundle b = new Bundle();
        b.putParcelable(MainActivity.STEP_KEY, steps.get(position));

        // Here we check to see if this is the first initialization of the fragment.
        if (!fragmentWasAdded) {
            Fragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_step_detail_fragment, recipeStepDetailFragment)
                    .commit();
            fragmentWasAdded = true;
        // If it is already initialized then we replace the fragment with a new one.
        } else {
            Fragment newFragment = new RecipeStepDetailFragment();
            newFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_fragment, newFragment)
                    .commit();
        }

        // If the Step is the last one in the ArrayList then we set the visibility of the
        // next TextView to invisible, if it is the first one in the ArrayList then we set the
        // visibility of the previous TextView to invisible. If the Step object is in any other
        // position we set the visibility of the two TextViews to Visible.
        if (position == steps.size() - 1) {
            nextButtonTv.setVisibility(View.INVISIBLE);
        } else if (position == 0){
            previousButtonTv.setVisibility(View.INVISIBLE);
        } else {
            nextButtonTv.setVisibility(View.VISIBLE);
            previousButtonTv.setVisibility(View.VISIBLE);
        }
    }
}
