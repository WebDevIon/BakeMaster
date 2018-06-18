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

        nextButtonTv = findViewById(R.id.next_step_button);
        previousButtonTv = findViewById(R.id.previous_step_button);

        Intent intent = getIntent();
        setTitle(intent.getStringExtra(MainActivity.RECIPE_NAME_KEY));
        steps = intent.getParcelableArrayListExtra(MainActivity.STEPS_KEY);
        position = intent.getIntExtra(MainActivity.POSITION_KEY, MainActivity.NO_POSITION);

        createFragment(steps, position);

        nextButtonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != MainActivity.NO_POSITION && position < steps.size() - 1) {
                    position++;
                    createFragment(steps, position);
                }
            }
        });

        previousButtonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != MainActivity.NO_POSITION && position > 0) {
                    position--;
                    createFragment(steps, position);
                }
            }
        });
    }

    public void createFragment(ArrayList<Step> steps, int position) {
        Bundle b = new Bundle();
        b.putParcelable(MainActivity.STEP_KEY, steps.get(position));

        if (!fragmentWasAdded) {
            Fragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_step_detail_fragment, recipeStepDetailFragment)
                    .commit();
            fragmentWasAdded = true;
        } else {
            Fragment newFragment = new RecipeStepDetailFragment();
            newFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_fragment, newFragment)
                    .commit();
        }

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
