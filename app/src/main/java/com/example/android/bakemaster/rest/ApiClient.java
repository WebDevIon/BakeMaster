package com.example.android.bakemaster.rest;

import com.example.android.bakemaster.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String TAG = ApiClient.class.getSimpleName();
    private static final String BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private static Retrofit retrofit = null;

    /**
     * Method used to create the Retrofit object.
     * @return the Retrofit object.
     */
    private static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * Method used to return the Call<MovieResponse> object used in the enqueue method.
     * @return the Call<List<Recipe>> object.
     */
    public static Call<List<Recipe>> getRecipes() {
        BakingMasterApi api = getClient().create(BakingMasterApi.class);
        return api.getRecipes();
    }
}
