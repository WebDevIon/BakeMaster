package com.example.android.bakemaster.rest;

import com.example.android.bakemaster.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * This interface is responsible for holding the endpoints and the details of the parameters
 * and the request method for the Retrofit.
 */
public interface BakingMasterApi {
    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
