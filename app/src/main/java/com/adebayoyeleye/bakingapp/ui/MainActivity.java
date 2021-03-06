package com.adebayoyeleye.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adebayoyeleye.bakingapp.R;
import com.adebayoyeleye.bakingapp.objects.Recipe;
import com.adebayoyeleye.bakingapp.utilities.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Recipe[]>, RecipesAdapter.RecipesAdapterOnClickHandler {

    public static final int OBJECT_LOADER = 22;
    public static final String BUNDLE_EXTRA = "path";
    private static final String RECIPE_LIST_URL =
            "http://go.udacity.com/android-baking-app-json";
    private static final String LIST_STATE_KEY = "list state key";

    @BindView(R.id.recyclerview_recipes)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    Recipe[] recipes;
    private RecipesAdapter mRecipesAdapter;

    private GridLayoutManager layoutManager;
    private Parcelable mListState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        if (findViewById(R.id.tabView) != null) {

            layoutManager = new GridLayoutManager(this, 3);
        } else {
            layoutManager = new GridLayoutManager(this, 1);
        }

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecipesAdapter = new RecipesAdapter(this, this);
        mRecyclerView.setAdapter(mRecipesAdapter);

        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            recipes = (Recipe[]) savedInstanceState.getParcelableArray(Recipe.RECIPE_EXTRA);
            mRecipesAdapter.setResults(recipes);
            showRecipesDataView();
            if (mListState != null) {
                layoutManager.onRestoreInstanceState(mListState);
            }

        } else {
            loadRecipes(RECIPE_LIST_URL);
        }

    }

    void loadRecipes(String recipeListUrl) {
        Bundle loadBundle = new Bundle();
        loadBundle.putString(BUNDLE_EXTRA, recipeListUrl);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String[]> loader = loaderManager.getLoader(OBJECT_LOADER);
        if (loader == null) {
            loaderManager.initLoader(OBJECT_LOADER, loadBundle, this);
        } else {
            loaderManager.restartLoader(OBJECT_LOADER, loadBundle, this);
        }
    }

    @Override
    public Loader<Recipe[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Recipe[]>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public Recipe[] loadInBackground() {
                String urlString = args.getString(BUNDLE_EXTRA);
                URL url = NetworkUtils.buildUrl();
                Gson gson = new Gson();
                try {
                    String jsonResultString = NetworkUtils.getResponseFromHttpUrl(url);
                    recipes = gson.fromJson(jsonResultString, Recipe[].class);
                    return recipes;
                } catch (JsonSyntaxException | IOException e) {
                    e.printStackTrace();
                    showErrorMessage();
                    return null;
                }

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Recipe[]> loader, Recipe[] data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null && data.length != 0) {
            mRecipesAdapter.setResults(data);
            showRecipesDataView();
        } else {
            showErrorMessage();
        }
    }


    @Override
    public void onLoaderReset(Loader<Recipe[]> loader) {

    }

    private void showRecipesDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(Recipe recipeClicked, int position) {
        Context context = this;
        Class destinationClass = StepsActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Recipe.RECIPE_EXTRA, recipeClicked);
        intentToStartDetailActivity.putExtra(Recipe.RECIPE_EXTRA_INDEX, position);
        startActivity(intentToStartDetailActivity);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save list state
        mListState = layoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
        outState.putParcelableArray(Recipe.RECIPE_EXTRA, recipes);
    }
}
