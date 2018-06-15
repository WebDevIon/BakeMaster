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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.bakemaster.R;
import com.example.android.bakemaster.model.Ingredient;
import com.example.android.bakemaster.model.Step;
import com.example.android.bakemaster.model.StepAdapter;

import java.util.ArrayList;

public class MasterListFragment extends Fragment
        implements StepAdapter.StepAdapterOnClickHandler{

    public MasterListFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list,
                container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.steps_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Ingredient> ingredients = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<Step> steps = bundle.getParcelableArrayList(MainActivity.STEPS_KEY);
            ingredients = bundle.getParcelableArrayList(MainActivity.INGREDIENTS_KEY);
            StepAdapter adapter = new StepAdapter(steps, getContext(), MasterListFragment.this);
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
    public void onClick(Step step) {
        Toast.makeText(getContext(), "Hello again!", Toast.LENGTH_SHORT).show();
    }
}
