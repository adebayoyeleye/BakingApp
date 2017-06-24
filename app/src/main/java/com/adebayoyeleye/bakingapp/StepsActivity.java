package com.adebayoyeleye.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

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

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Recipe.RECIPE_EXTRA)) {

                recipeClicked = intentThatStartedThisActivity.getParcelableExtra(Recipe.RECIPE_EXTRA);

                IngredientsListFragment ingredientsListFragment = new IngredientsListFragment();
                ingredientsListFragment.setRecipeClicked(recipeClicked);
                ingredientsListFragment.setContext(this);
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.fragment_master_list_container, ingredientsListFragment)
                        .commit();


                StepsListFragment stepsListFragment = new StepsListFragment();
                stepsListFragment.setRecipeClicked(recipeClicked);
                stepsListFragment.setContext(this);
                stepsListFragment.setClickHandler(this);

                fragmentManager.beginTransaction()
                        .add(R.id.fragment_steps_list_container, stepsListFragment)
                        .commit();

            }

        }

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
            b.putParcelable(Step.STEP_EXTRA, stepClicked);

            // Attach the Bundle to an intent
            final Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtras(b);

            startActivity(intent);
        }

    }
}
