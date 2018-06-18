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
import android.widget.Toast;

import com.example.android.bakemaster.R;
import com.example.android.bakemaster.model.Ingredient;
import com.example.android.bakemaster.model.Step;
import com.example.android.bakemaster.model.StepAdapter;

import java.util.ArrayList;

public class RecipeDetailFragment extends Fragment
        implements StepAdapter.StepAdapterOnClickHandler {

    private ArrayList<Step> steps;
    private boolean isTabletView;
    private String recipeName;

    public RecipeDetailFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_step,
                container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.steps_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Ingredient> ingredients = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            ingredients = bundle.getParcelableArrayList(MainActivity.INGREDIENTS_KEY);
            steps = bundle.getParcelableArrayList(MainActivity.STEPS_KEY);
            recipeName = bundle.getString(MainActivity.RECIPE_NAME_KEY);
            isTabletView = bundle.getBoolean(MainActivity.IS_TABLET_KEY);

            StepAdapter adapter = new StepAdapter(steps, getContext(), RecipeDetailFragment.this);
            recyclerView.setAdapter(adapter);
        }

        LinearLayout ingredientsLayout = rootView.findViewById(R.id.ingredients_layout);
        ingredientsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Aloha!", Toast.LENGTH_SHORT).show();
                //TODO: Send the recovered ArrayList of Ingredients to the IngredientListActivity.
            }
        });

        return rootView;
    }

    @Override
    public void onClick(Step step, int position) {
        if (!isTabletView) {
            Intent intent = new Intent(getContext(), RecipeStepDetailActivity.class);
            intent.putExtra(MainActivity.RECIPE_NAME_KEY, recipeName);
            intent.putExtra(MainActivity.STEPS_KEY, steps);
            intent.putExtra(MainActivity.POSITION_KEY, position);
            startActivity(intent);
        } else {
            Bundle b = new Bundle();
            b.putParcelable(MainActivity.STEP_KEY, step);

            Fragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(b);
            getFragmentManager().beginTransaction()
                    .add(R.id.recipe_step_detail_fragment, recipeStepDetailFragment)
                    .commit();
        }
    }
}
