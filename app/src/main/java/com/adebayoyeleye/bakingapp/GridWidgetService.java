package com.adebayoyeleye.bakingapp;

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.adebayoyeleye.bakingapp.objects.Ingredient;
import com.adebayoyeleye.bakingapp.objects.Recipe;
import com.adebayoyeleye.bakingapp.utilities.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;

    Recipe[] mRecipes;
    private List<Ingredient> desiredIngredients;
    private Ingredient ingredient;
    private Recipe desiredRecipe;

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;

    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        URL url = NetworkUtils.buildUrl();
        Gson gson = new Gson();
        try {
            String jsonResultString = NetworkUtils.getResponseFromHttpUrl(url);
            mRecipes = gson.fromJson(jsonResultString, Recipe[].class);
            desiredRecipe = mRecipes[0];
            desiredIngredients = desiredRecipe.getIngredients();
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        if (desiredIngredients == null) return 0;
        return desiredIngredients.size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (desiredIngredients == null || desiredIngredients.size() == 0) return null;

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_app_widget_provider);

        ingredient = desiredIngredients.get(position);
        String ingredientString = ingredient.getQuantity() + ingredient.getMeasure() + " of " + ingredient.getIngredient();
        views.setTextViewText(R.id.appwidget_text, ingredientString);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, BakingAppWidgetProvider.class));


        // Fill in the onClick PendingIntent Template
        Bundle extras = new Bundle();
        extras.putParcelable(Recipe.RECIPE_EXTRA, desiredRecipe);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.appwidget_text, fillInIntent);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

