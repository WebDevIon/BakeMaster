package com.example.android.bakemaster.database;

import android.arch.persistence.room.TypeConverter;

import com.example.android.bakemaster.model.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * This class is used to convert the List of Ingredient objects to a String of data in
 * order to be stored and retrieve from the database.
 */
public class IngredientTypeConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Ingredient> stringToIngredients(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Ingredient>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ingredientsListToString(List<Ingredient> ingredients) {
        return gson.toJson(ingredients);
    }
}
