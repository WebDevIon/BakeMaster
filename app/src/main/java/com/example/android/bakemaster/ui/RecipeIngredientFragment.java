package com.example.android.bakemaster.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakemaster.R;
import com.example.android.bakemaster.model.Ingredient;
import com.example.android.bakemaster.model.IngredientAdapter;

import java.util.ArrayList;

public class RecipeIngredientFragment extends Fragment {

    // Default empty constructor.
    public RecipeIngredientFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients,
                container, false);

        // Here we initialize the RecyclerView and we set the layout manager.
        RecyclerView recyclerView = rootView.findViewById(R.id.ingredients_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // We check if the data is present and we get the bundle that was passed by the
        // RecipeIngredientActivity.
        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<Ingredient> ingredients =
                    bundle.getParcelableArrayList(MainActivity.INGREDIENTS_KEY);

            // Here we set the adapter for the ingredients RecyclerView
            IngredientAdapter adapter = new IngredientAdapter(ingredients, getContext());
            recyclerView.setAdapter(adapter);
        }

        return rootView;
    }
}
