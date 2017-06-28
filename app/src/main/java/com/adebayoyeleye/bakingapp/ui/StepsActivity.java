package com.adebayoyeleye.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.adebayoyeleye.bakingapp.R;
import com.adebayoyeleye.bakingapp.objects.Recipe;
import com.adebayoyeleye.bakingapp.objects.Step;

import java.util.ArrayList;
import java.util.List;

public class StepsActivity extends AppCompatActivity implements StepsAdapter.StepsAdapterOnClickHandler {

    Recipe recipeClicked;

    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

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
            }
        } else {

            Intent intentThatStartedThisActivity = getIntent();

            if ((intentThatStartedThisActivity != null) && (intentThatStartedThisActivity.hasExtra(Recipe.RECIPE_EXTRA))) {
                recipeClicked = intentThatStartedThisActivity.getParcelableExtra(Recipe.RECIPE_EXTRA);
            }
        }

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
}
