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
 * This is a custom Adapter which is responsible for loading the steps in
 * the Step Detail Activity and it also has the interface which is implemented and
 * overwritten in Step Detail Activity to handle the object clicks.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private final static String LOG_TAG = StepAdapter.class.getSimpleName();
    private final Context context;
    private final List<Step> steps;
    final private StepAdapterOnClickHandler clickHandler;

    /**
     * This is the interface responsible for handling the object clicks.
     */
    public interface StepAdapterOnClickHandler {
        void onClick(Step step);
    }

    /**
     * This is the constructor for the StepAdapter
     * @param steps the List of Step objects received from the server.
     * @param context the context of the application.
     * @param clickHandler the click handler.
     */
    public StepAdapter(List<Step> steps, Context context,
                         StepAdapterOnClickHandler clickHandler) {
        this.steps = steps;
        this.context = context;
        this.clickHandler = clickHandler;
    }

    /**
     * Inner class used for caching the child view of the StepAdapter. Here we placed the
     * onClickListener because here it has access both to the adapter and the views.
     */
    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView stepShortDescription;

        StepViewHolder(View itemView) {
            super(itemView);

            stepShortDescription = itemView.findViewById(R.id.step_short_description_tv);
            itemView.setOnClickListener(this);
        }

        /**
         * Here we override the onClick method. When a child is clicked we fetch the Step
         * object and then we call the clickHandler registered with this adapter and pass
         * in the Step object.
         * @param view the view that was clicked.
         */
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Step step = steps.get(position);
            clickHandler.onClick(step);
        }
    }

    /**
     * This method creates each ViewHolder.
     * @param parent the ViewGroup that contains these ViewHolders.
     * @param viewType the int which define which kind of viewType we want to populate.
     * @return the StepViewHolder that holds the View for each grid item.
     */
    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.list_item_step;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new StepViewHolder(view);
    }

    /**
     * In this method we update the contents of the ViewHolder for that given position.
     * @param holder the ViewHolder that will have it's contents updated.
     * @param position the position within the adapter.
     */
    @Override
    public void onBindViewHolder(@NonNull final StepViewHolder holder, int position) {
        holder.stepShortDescription.setText(steps.get(position).getShortDescription());
    }

    /**
     * This item returns the number of items to display.
     * @return the number of items available in our Step grid.
     */
    @Override
    public int getItemCount() {
        return steps.size();
    }

}
