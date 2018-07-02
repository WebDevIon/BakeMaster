package com.example.android.bakemaster.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android.bakemaster.R;
import com.example.android.bakemaster.model.Ingredient;
import com.example.android.bakemaster.model.Step;
import com.example.android.bakemaster.model.StepAdapter;

import java.util.ArrayList;

/**
 * This is the class responsible for the creation of the Fragment that contains the list recipe
 * steps and the ingredients TextView.
 */
public class RecipeDetailFragment extends Fragment
        implements StepAdapter.StepAdapterOnClickHandler {

    private ArrayList<Step> steps;
    private ArrayList<Ingredient> ingredients;
    private boolean isTabletView;
    private String recipeName;

    // Default empty constructor.
    public RecipeDetailFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_step,
                container, false);

        // Here we initialize the RecyclerView and we set the layout manager.
        RecyclerView recyclerView = rootView.findViewById(R.id.steps_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // We check if the data is present and we get the bundle that was passed by the
        // RecipeDetailActivity.
        Bundle bundle = getArguments();
        if (bundle != null) {
            ingredients = bundle.getParcelableArrayList(MainActivity.INGREDIENTS_KEY);
            steps = bundle.getParcelableArrayList(MainActivity.STEPS_KEY);
            recipeName = bundle.getString(MainActivity.RECIPE_NAME_KEY);
            isTabletView = bundle.getBoolean(MainActivity.IS_TABLET_KEY);

            // Here we set the adapter for the steps RecyclerView
            StepAdapter adapter = new StepAdapter(steps, getContext(),
                    RecipeDetailFragment.this);
            recyclerView.setAdapter(adapter);
        }

        // We set a click listener on the ingredients TextView that will open the IngredientsActivity
        LinearLayout ingredientsLayout = rootView.findViewById(R.id.ingredients_layout);
        ingredientsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the device is a phone we put the extra data needed by the RecipeIngredientActivity
                // in the intent and we start the intent.
                if (!isTabletView) {
                    Intent intent = new Intent(getContext(), RecipeIngredientActivity.class);
                    intent.putExtra(MainActivity.INGREDIENTS_KEY, ingredients);
                    intent.putExtra(MainActivity.RECIPE_NAME_KEY, recipeName);
                    startActivity(intent);
                } else {
                    // If the device is a tablet we put the extra data we needed by the
                    // RecipeIngredientFragment in a bundle and we create a new fragment.
                    Bundle b = new Bundle();
                    b.putParcelableArrayList(MainActivity.INGREDIENTS_KEY, ingredients);
                    Fragment recipeIngredientFragment = new RecipeIngredientFragment();
                    recipeIngredientFragment.setArguments(b);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.recipe_step_detail_fragment, recipeIngredientFragment)
                            .commit();
                }
            }
        });

        return rootView;
    }

    /**
     * This method handles clicks on the steps of the recipe presented in the RecyclerView.
     * Here we check to see if the device is a tablet or a phone to decide what the app should
     * do when the steps are clicked. In the case of the phone the RecipeStepDetailActivity is
     * launched and in the case of the tablet the RecipeStepDetailFragment which contains step
     * details is shown next to the existing fragment.
     * @param step the step that was clicked.
     * @param position the position of the clicked step in the ArrayList of Step objects.
     */
    @Override
    public void onClick(Step step, int position) {
        if (!isTabletView) {
            // If the device is a phone we put the extra data needed by the RecipeStepDetailActivity
            // in the intent and we start the intent.
            Intent intent = new Intent(getContext(), RecipeStepDetailActivity.class);
            intent.putExtra(MainActivity.RECIPE_NAME_KEY, recipeName);
            intent.putExtra(MainActivity.STEPS_KEY, steps);
            intent.putExtra(MainActivity.POSITION_KEY, position);
            startActivity(intent);
        } else {
            RecipeStepDetailFragment.upWasClicked = RecipeStepDetailActivity.NO_VALUE;
            // If the device is a tablet we put the extra data we needed by the
            // RecipeStepDetailFragment in a bundle and we create a new fragment.
            Bundle b = new Bundle();
            b.putParcelable(MainActivity.STEP_KEY, step);
            Fragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(b);
            getFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_fragment, recipeStepDetailFragment)
                    .commit();
        }
    }
}
