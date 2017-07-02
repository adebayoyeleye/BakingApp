package com.adebayoyeleye.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.adebayoyeleye.bakingapp.BakingAppWidgetProvider;
import com.adebayoyeleye.bakingapp.R;
import com.adebayoyeleye.bakingapp.objects.Recipe;
import com.adebayoyeleye.bakingapp.objects.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.SharedPreferences.Editor;

public class StepsActivity extends AppCompatActivity implements StepsAdapter.StepsAdapterOnClickHandler {

    public static final String PREFS_KEY = "baking app preferences";
    Recipe recipeClicked;
    @BindView(R.id.the_scrollview)
    NestedScrollView mScrollView;
    @BindView(R.id.fragment_master_list_container)
    FrameLayout ingredientListContainer;
    @BindView(R.id.btn_desired_recipe)
    ToggleButton desiredRecipeButton;
    @BindView(R.id.btn_toggle_ingredients)
    ToggleButton toggleIngredientsButton;
    private boolean mTwoPane;
    private SharedPreferences prefs;
    private int recipeIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        ButterKnife.bind(this);

        if (findViewById(R.id.two_pane_divider) != null) {
            mTwoPane = true;
            VideoFragment videoFragment = new VideoFragment();
//            videoFragment.setStep(stepClicked);
            videoFragment.setContext(this);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.fragment_video_container, videoFragment)
                    .commit();
        } else {
            mTwoPane = false;
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Recipe.RECIPE_EXTRA)) {
                recipeClicked = savedInstanceState
                        .getParcelable(Recipe.RECIPE_EXTRA);
                final boolean ingredientShown = savedInstanceState.getBoolean(getResources().getString(R.string.ingredients_shown_bundle_key), false);
                //                toggleIngredients(toggleIngredientsButton);
                if (ingredientShown) {
                    ingredientListContainer.setVisibility(View.VISIBLE);
                }

                final int[] position = savedInstanceState.getIntArray(getString(R.string.scroll_position_key));
                if (position != null)
                    mScrollView.post(new Runnable() {
                        public void run() {
                            mScrollView.scrollTo(position[0], position[1]);
                        }
                    });
            }
        } else {

            Intent intentThatStartedThisActivity = getIntent();

            if ((intentThatStartedThisActivity != null) && (intentThatStartedThisActivity.hasExtra(Recipe.RECIPE_EXTRA))) {
                recipeClicked = intentThatStartedThisActivity.getParcelableExtra(Recipe.RECIPE_EXTRA);
                recipeIndex = intentThatStartedThisActivity.getIntExtra(Recipe.RECIPE_EXTRA_INDEX, 0);
            }
        }

        prefs = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        setRecipeButtonText();

        IngredientsListFragment ingredientsListFragment = new IngredientsListFragment();
        ingredientsListFragment.setIngredientsList(recipeClicked.getIngredients());
        ingredientsListFragment.setContext(this);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.fragment_master_list_container, ingredientsListFragment)
                .commit();


        StepsListFragment stepsListFragment = new StepsListFragment();
        stepsListFragment.setStepsList(recipeClicked.getSteps());
        stepsListFragment.setContext(this);
        stepsListFragment.setClickHandler(this);

        fragmentManager.beginTransaction()
                .add(R.id.fragment_steps_list_container, stepsListFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Recipe.RECIPE_EXTRA, recipeClicked);
        outState.putIntArray(getString(R.string.scroll_position_key),
                new int[]{mScrollView.getScrollX(), mScrollView.getScrollY()});
        outState.putBoolean(getString(R.string.ingredients_shown_bundle_key), toggleIngredientsButton.isChecked());

    }

    @Override
    public void onClick(Step stepClicked) {

        if (mTwoPane) {
            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setStep(stepClicked);
            videoFragment.setContext(this);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_video_container, videoFragment)
                    .commit();

        } else {
            // Put this information in a Bundle and attach it to an Intent that will launch a StepDetailsActivity
            Bundle b = new Bundle();
            List<Step> steps = recipeClicked.getSteps();
            int stepIndex = steps.indexOf(stepClicked);
            b.putParcelableArrayList(Step.STEP_EXTRA, (ArrayList<? extends Parcelable>) steps);
            b.putInt(Step.INDEX_EXTRA, stepIndex);


            // Attach the Bundle to an intent
            final Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtras(b);

            startActivity(intent);
        }

    }

    public void toggleIngredients(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();
        if (on) {
            // show ingredients
            ingredientListContainer.setVisibility(View.VISIBLE);
        } else {
            // hide ingredients
            ingredientListContainer.setVisibility(View.GONE);
        }
    }

    private void setRecipeButtonText() {
        int prefIndex = prefs.getInt(Recipe.RECIPE_DESIRED_INDEX, 0);
        if (recipeIndex == prefIndex) {
            desiredRecipeButton.setChecked(true);
        }
    }

    public void setDesiredRecipe(View view) {
        Editor editor = prefs.edit();
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();
        if (on) {
            //store recipe index
            editor.putInt(Recipe.RECIPE_DESIRED_INDEX, recipeIndex);
            editor.apply();
        }
        setRecipeButtonText();
        updateWidget();
    }

    void updateWidget() {
        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, BakingAppWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
    }
}
