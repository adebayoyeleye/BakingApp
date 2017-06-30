package com.adebayoyeleye.bakingapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adebayoyeleye.bakingapp.R;
import com.adebayoyeleye.bakingapp.objects.Recipe;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adebayo on 18/06/2017.
 */

class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {
    private final RecipesAdapterOnClickHandler mClickHandler;
    private Recipe[] results;
    private Context context;


    RecipesAdapter(Context c, RecipesAdapterOnClickHandler clickHandler) {
        context = c;
        mClickHandler = clickHandler;
    }

    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesAdapterViewHolder holder, int position) {
        Recipe recipe = results[position];
        String name = recipe.getName();
        String imagePath = recipe.getImage();

        holder.mRecipeName.setText(name);
        Picasso.with(context)
                .load(imagePath)
                .placeholder(R.drawable.ic_do_not_disturb)
                .error(R.color.colorAccent)
                .into(holder.mRecipeImage);

    }

    @Override
    public int getItemCount() {
        return results == null ? 0 : results.length;
    }

    void setResults(Recipe[] results) {
        this.results = results;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onClick messages.
     */
    interface RecipesAdapterOnClickHandler {
        void onClick(Recipe recipeClicked);
    }

    class RecipesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_name)
        TextView mRecipeName;
        @BindView(R.id.iv_recipe_image)
        ImageView mRecipeImage;

        RecipesAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipeClicked = results[adapterPosition];

            mClickHandler.onClick(recipeClicked);
        }
    }
}

