package com.adebayoyeleye.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class StepsActivity extends AppCompatActivity {

    Recipe recipeClicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

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

                fragmentManager.beginTransaction()
                        .add(R.id.fragment_steps_list_container, stepsListFragment)
                        .commit();

            }

        }

    }
}
