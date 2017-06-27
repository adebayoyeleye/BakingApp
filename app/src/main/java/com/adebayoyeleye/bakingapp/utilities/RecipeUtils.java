package com.adebayoyeleye.bakingapp.utilities;

import android.os.AsyncTask;

import com.adebayoyeleye.bakingapp.objects.Recipe;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Adebayo on 27/06/2017.
 */

public class RecipeUtils {
    public Recipe[] recipes;

    public Recipe[] loadRecipes() {
        new AClass().execute(NetworkUtils.buildUrl());
        return recipes;

    }

    private class AClass extends AsyncTask<URL, Void, Recipe[]> {
        @Override
        protected Recipe[] doInBackground(URL... params) {
            URL url = NetworkUtils.buildUrl();
            Gson gson = new Gson();
            try {
                String jsonResultString = NetworkUtils.getResponseFromHttpUrl(url);
                recipes = gson.fromJson(jsonResultString, Recipe[].class);
                return recipes;
            } catch (JsonSyntaxException | IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Recipe[] s) {
            super.onPostExecute(s);
        }
    }

}
