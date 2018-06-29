package com.example.android.bakemaster.model;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.bakemaster.R;
import com.example.android.bakemaster.database.AppExecutors;
import com.example.android.bakemaster.database.RecipeDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * This is a custom Adapter which is responsible for loading the recipe cards in
 * the Main Activity and it also has the interface which is implemented and
 * overwritten in Main Activity to handle the object clicks.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final static String LOG_TAG = RecipeAdapter.class.getSimpleName();
    private final Context context;
    private final List<Recipe> recipes;
    private final LifecycleOwner lifecycleOwner;
    private RecipeDatabase db;
    final private RecipeAdapterOnClickHandler clickHandler;

    /**
     * This is the interface responsible for handling the object clicks.
     */
    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    /**
     * This is the constructor for the RecipeAdapter
     * @param recipes the List of Recipe objects received from the server.
     * @param context the context of the application.
     * @param clickHandler the click handler.
     */
    public RecipeAdapter(List<Recipe> recipes, Context context,
                         RecipeAdapterOnClickHandler clickHandler, LifecycleOwner lifecycleOwner) {
        this.recipes = recipes;
        this.context = context;
        this.clickHandler = clickHandler;
        this.lifecycleOwner = lifecycleOwner;
        db = RecipeDatabase.getInstance(context);

    }

    /**
     * Inner class used for caching the child view of the RecipeAdapter. Here we placed the
     * onClickListener because here it has access both to the adapter and the views.
     */
    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView recipeImage;
        final TextView recipeName;
        final TextView servingsNumber;
        final ToggleButton favoriteToggleButton;

        RecipeViewHolder(View itemView) {
            super(itemView);

            recipeImage = itemView.findViewById(R.id.recipe_image_iv);
            recipeName = itemView.findViewById(R.id.recipe_name_tv);
            servingsNumber = itemView.findViewById(R.id.recipe_servings_value_tv);
            favoriteToggleButton = itemView.findViewById(R.id.favorite_tb);
            itemView.setOnClickListener(this);
        }

        /**
         * Here we override the onClick method. When a child is clicked we fetch the recipe
         * object and then we call the clickHandler registered with this adapter and pass
         * in the recipe object.
         * @param view the view that was clicked.
         */
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Recipe recipe = recipes.get(position);
            clickHandler.onClick(recipe);
        }
    }

    /**
     * This method creates each ViewHolder.
     * @param parent the ViewGroup that contains these ViewHolders.
     * @param viewType the int which define which kind of viewType we want to populate.
     * @return the RecipeViewHolder that holds the View for each grid item.
     */
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.card_baking_recipe;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new RecipeViewHolder(view);
    }

    /**
     * In this method we update the contents of the ViewHolder for that given position.
     * @param holder the ViewHolder that will have it's contents updated.
     * @param position the position within the adapter.
     */
    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int position) {
        // The Picasso library is used to set the content of the ImageView.
        final Recipe recipe = recipes.get(position);
        int placeholderImage = randomizePlaceholderImage();
        if (recipe.getImage().isEmpty()) {
            Picasso.get()
                    .load(placeholderImage)
                    .placeholder(placeholderImage)
                    .error(placeholderImage)
                    .fit()
                    .centerCrop()
                    .into(holder.recipeImage);
        } else {
            Picasso.get()
                    .load(recipe.getImage())
                    .placeholder(placeholderImage)
                    .error(placeholderImage)
                    .fit()
                    .centerCrop()
                    .into(holder.recipeImage);
        }

        // Here we set the name and the number of servings of each recipe.
        holder.recipeName.setText(recipe.getName());
        holder.servingsNumber.setText(recipe.getServingsToString());

        // Check the db to see if the recipe is saved, if it is we set the favorite toggle button
        // drawable to ic_favorite, if it is not we set it to ic_favorite_border.
        retrieveRecipe(recipe.getName(), holder);

        holder.favoriteToggleButton
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    saveRecipeInDatabase(recipe);
                    checkFavButton(holder);
                } else {
                    removeRecipeFromDatabase(recipe);
                    uncheckFavButton(holder);
                }
            }
        });
    }

    /**
     * This item returns the number of items to display.
     * @return the number of items available in our recipe grid.
     */
    @Override
    public int getItemCount() {
        return recipes.size();
    }

    /**
     * This method is used to randomize the placeholder image which will be displayed for each recipe.
     * @return the resource id.
     */
    private int randomizePlaceholderImage(){
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(4);
        switch (randomNumber){
            case 0: return R.drawable.recipe_placeholder_image;
            case 1: return R.drawable.recipe_placeholder_image_1;
            case 2: return R.drawable.recipe_placeholder_image_2;
            case 3: return R.drawable.recipe_placeholder_image_3;
            default: return R.drawable.recipe_placeholder_image;
        }
    }

    /**
     * With this method we save the recipe in the database.
     * @param recipe the recipe object that we save in the database.
     */
    private void saveRecipeInDatabase(final Recipe recipe) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.recipeDao().insertTask(recipe);
                Log.d(LOG_TAG, "Added recipe.");
            }
        });
        Toast.makeText(context, "Added to favorites!", Toast.LENGTH_SHORT).show();
    }

    /**
     * With this method we delete the recipe from the database.
     * @param recipe the recipe object that we delete from the database.
     */
    private void removeRecipeFromDatabase(final Recipe recipe) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.recipeDao().deleteTask(recipe);
                Log.d(LOG_TAG, "Removed recipe.");
            }
        });
        Toast.makeText(context, "Removed from favorites!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Method used to retrieve recipes from the database.
     * @param recipeName the name of the recipe that we want to retrieve.
     * @param holder the ViewHolder that is holding the retrieved recipe.
     */
    private void retrieveRecipe(String recipeName, final RecipeViewHolder holder) {
        Log.d(LOG_TAG, "Retrieved recipe.");
        final LiveData<Recipe> recipe = db.recipeDao().loadRecipe(recipeName);
        recipe.observe(lifecycleOwner, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipeEntry) {
                if (recipeEntry != null) {
                    checkFavButton(holder);
                } else {
                    uncheckFavButton(holder);
                }
            }
        });
    }

    /**
     * Method used to check the toggle button when it is clicked.
     * @param holder the ViewHolder that has the clicked button.
     */
    private void checkFavButton(RecipeViewHolder holder) {
        holder.favoriteToggleButton.setChecked(true);
        holder.favoriteToggleButton.setBackgroundDrawable(ContextCompat.getDrawable
                (context, R.drawable.ic_favorite));
    }

    /**
     * Method used to uncheck the toggle button when it is clicked.
     * @param holder the ViewHolder that has the clicked button.
     */
    private void uncheckFavButton(RecipeViewHolder holder) {
        holder.favoriteToggleButton.setChecked(false);
        holder.favoriteToggleButton.setBackgroundDrawable(ContextCompat.getDrawable
                (context, R.drawable.ic_favorite_border));
    }
}
