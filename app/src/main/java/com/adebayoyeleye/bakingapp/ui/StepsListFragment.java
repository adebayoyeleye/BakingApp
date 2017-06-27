package com.adebayoyeleye.bakingapp.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adebayoyeleye.bakingapp.R;
import com.adebayoyeleye.bakingapp.objects.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adebayo on 19/06/2017.
 */

public class StepsListFragment extends Fragment {

    /*
        @BindView(R.id.rv_ingredients)
        RecyclerView mIngredientsList;
    */
    @BindView(R.id.rv_steps)
    RecyclerView mStepsList;
    Recipe recipeClicked;
    Context context;

    private IngredientsAdapter mIngredientsAdapter;
    private StepsAdapter mStepsAdapter;
    private StepsAdapter.StepsAdapterOnClickHandler mClickHandler;

    public StepsListFragment() {
    }

    public void setRecipeClicked(Recipe recipeClicked) {
        this.recipeClicked = recipeClicked;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setClickHandler(StepsAdapter.StepsAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list_steps, container, false);
        ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager stepsLayoutManager = new LinearLayoutManager(context);
        mStepsAdapter = new StepsAdapter(context, recipeClicked.getSteps(), mClickHandler);
        mStepsList.setLayoutManager(stepsLayoutManager);
        mStepsList.setAdapter(mStepsAdapter);

        return rootView;
    }

}
