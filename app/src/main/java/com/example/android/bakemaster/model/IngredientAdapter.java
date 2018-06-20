package com.example.android.bakemaster.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakemaster.R;

import java.util.List;

/**
 * This is a custom Adapter which is responsible for loading the ingredients in
 * the Ingredient Detail Activity.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final static String LOG_TAG = IngredientAdapter.class.getSimpleName();
    private final Context context;
    private final List<Ingredient> ingredients;

    /**
     * This is the constructor for the IngredientAdapter
     * @param ingredients the List of Ingredient objects received from the server.
     * @param context the context of the application.
     */
    public IngredientAdapter(List<Ingredient> ingredients, Context context) {
        this.ingredients = ingredients;
        this.context = context;
    }

    /**
     * Inner class used for caching the child view of the IngredientAdapter.
     */
    class IngredientViewHolder extends RecyclerView.ViewHolder {

        final TextView ingredientName;
        final TextView ingredientUnit;
        final TextView ingredientQuantity;

        IngredientViewHolder(View itemView) {
            super(itemView);

            ingredientName = itemView.findViewById(R.id.ingredient_name_tv);
            ingredientUnit = itemView.findViewById(R.id.ingredient_unit_tv);
            ingredientQuantity = itemView.findViewById(R.id.ingredient_qty_tv);
        }

    }

    /**
     * This method creates each ViewHolder.
     * @param parent the ViewGroup that contains these ViewHolders.
     * @param viewType the int which define which kind of viewType we want to populate.
     * @return the IngredientViewHolder that holds the View for each list item.
     */
    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.list_item_ingredient;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new IngredientViewHolder(view);
    }

    /**
     * In this method we update the contents of the ViewHolder for that given position.
     * @param holder the ViewHolder that will have it's contents updated.
     * @param position the position within the adapter.
     */
    @Override
    public void onBindViewHolder(@NonNull final IngredientViewHolder holder, int position) {
        holder.ingredientName.setText(ingredients.get(position).getIngredient());
        holder.ingredientUnit.setText(ingredients.get(position).getMeasure());
        holder.ingredientQuantity.setText(ingredients.get(position).getQuantity().toString());
    }

    /**
     * This item returns the number of items to display.
     * @return the number of items available in our Ingredient grid.
     */
    @Override
    public int getItemCount() {
        return ingredients.size();
    }

}
