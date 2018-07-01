package com.example.android.bakemaster.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakemaster.R;
import com.example.android.bakemaster.database.AppExecutors;
import com.example.android.bakemaster.database.RecipeDatabase;
import com.example.android.bakemaster.model.Recipe;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = ListRemoteViewsFactory.class.getSimpleName();
    private Context mContext;
    private RecipeDatabase mDb;
    private Recipe mRecipe;
    private int mAppWidgetId;
    private String mRecipeName;

    ListRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        mDb = RecipeDatabase.getInstance(context);
        mRecipeName = IngredientListWidgetConfigureActivity
                .loadRecipeName(mContext, mAppWidgetId);
        Log.d(LOG_TAG, "Recipe ingredients name: " + mRecipeName + " and id: " + mAppWidgetId);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Log.d(LOG_TAG, "Recipe data updated!");

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mRecipe = mDb.recipeDao().loadRecipeObject(mRecipeName);
            }
        });
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "Recipe widget deleted!");
        mRecipe = null;
        mDb = null;
        mRecipeName = null;
    }

    @Override
    public int getCount() {

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mRecipe != null) {
            Log.d(LOG_TAG, "Ingredient count: " + mRecipe.getIngredients().size());
            return mRecipe.getIngredients().size();
        } else {
            Log.d(LOG_TAG, "Ingredient count: 0");
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_item_widget);
        if (mRecipe != null) {
            Log.d(LOG_TAG, "Ingredient added.");
            rv.setTextViewText(R.id.widget_list_item, mRecipe.getIngredients()
                    .get(position).getIngredient());
        } else {
            rv.setTextViewText(R.id.widget_list_item, "No data");
            Log.d(LOG_TAG, "Ingredient missing.");
        }

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
