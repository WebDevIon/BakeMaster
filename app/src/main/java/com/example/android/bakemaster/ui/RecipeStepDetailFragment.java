package com.example.android.bakemaster.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakemaster.R;
import com.example.android.bakemaster.model.Step;

/**
 * This is the class responsible for the creation of the Fragment that contains
 * the list recipe step details.
 */
public class RecipeStepDetailFragment extends Fragment {

    public RecipeStepDetailFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail,
                container, false);

        // Here we get the step passed by the RecipeStepDetailActivity and we set the
        // data to each view.
        Bundle bundle = getArguments();
        if (bundle != null) {
            // TODO: Add the correct video and thumbnail to the ExoPlayer.
            Step step = bundle.getParcelable(MainActivity.STEP_KEY);
            TextView recipeStepInstruction = rootView.findViewById(R.id.recipe_step_instruction_tv);
            recipeStepInstruction.setText(step.getDescription());
        }

        return rootView;
    }
}
