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
import com.adebayoyeleye.bakingapp.objects.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adebayo on 19/06/2017.
 */

public class IngredientsListFragment extends Fragment {

    @BindView(R.id.rv_ingredients)
    RecyclerView mIngredientsList;
    List<Ingredient> ingredientsList;
    Context context;

    private IngredientsAdapter mIngredientsAdapter;

    public IngredientsListFragment() {
    }

    public void setIngredientsList(List<Ingredient> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list_ingredients, container, false);
        ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager ingredientsLayoutManager = new LinearLayoutManager(context);
        mIngredientsAdapter = new IngredientsAdapter(ingredientsList);
        mIngredientsList.setLayoutManager(ingredientsLayoutManager);
        mIngredientsList.setAdapter(mIngredientsAdapter);

        return rootView;
    }
}
