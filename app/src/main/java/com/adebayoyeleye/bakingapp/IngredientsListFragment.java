package com.adebayoyeleye.bakingapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adebayo on 19/06/2017.
 */

public class IngredientsListFragment extends Fragment {

    @BindView(R.id.rv_ingredients)
    RecyclerView mIngredientsList;
    Recipe recipeClicked;
    Context context;
    /*
        @BindView(R.id.rv_steps)
        RecyclerView mStepsList;
    */
    private IngredientsAdapter mIngredientsAdapter;
    private StepsAdapter mStepsAdapter;

    public IngredientsListFragment() {
    }

    public void setRecipeClicked(Recipe recipeClicked) {
        this.recipeClicked = recipeClicked;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list_ingredients, container, false);
        ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager ingredientsLayoutManager = new LinearLayoutManager(context);
        RecyclerView.LayoutManager stepsLayoutManager = new LinearLayoutManager(context);
        mIngredientsAdapter = new IngredientsAdapter(recipeClicked.getIngredients());
        mIngredientsList.setLayoutManager(ingredientsLayoutManager);
        mIngredientsList.setAdapter(mIngredientsAdapter);
        mStepsAdapter = new StepsAdapter(recipeClicked.getSteps(), null);
/*
        mStepsList.setLayoutManager(stepsLayoutManager);
        mStepsList.setAdapter(mStepsAdapter);
*/

        return rootView;
    }
}