package com.adebayoyeleye.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;

import com.adebayoyeleye.bakingapp.R;
import com.adebayoyeleye.bakingapp.objects.Recipe;
import com.adebayoyeleye.bakingapp.objects.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsActivity extends AppCompatActivity implements StepsAdapter.StepsAdapterOnClickHandler {

    Recipe recipeClicked;
    @BindView(R.id.the_scrollview)
    NestedScrollView mScrollView;
    private boolean mTwoPane;

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
        outState.putIntArray(getString(R.string.scroll_position_key),
                new int[]{mScrollView.getScrollX(), mScrollView.getScrollY()});

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
