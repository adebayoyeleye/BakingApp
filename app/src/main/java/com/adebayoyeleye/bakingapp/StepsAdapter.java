package com.adebayoyeleye.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adebayo on 19/06/2017.
 */

class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterViewHolder> {

    private final StepsAdapter.StepsAdapterOnClickHandler mClickHandler;
    private List<Step> steps = new ArrayList<Step>();


    StepsAdapter(List<Step> steps, StepsAdapterOnClickHandler clickHandler) {
        this.steps = steps;
        mClickHandler = clickHandler;
    }

    @Override
    public StepsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.steps_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new StepsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsAdapterViewHolder holder, int position) {
        Step step = steps.get(position);
        String shortDescription = step.getShortDescription();

        holder.mStepTextView.setText(shortDescription);
    }

    @Override
    public int getItemCount() {
        return steps == null ? 0 : steps.size();
    }

    void setSteps(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onClick messages.
     */
    interface StepsAdapterOnClickHandler {
        void onClick(Step stepClicked);
    }

    class StepsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_step_short_description)
        TextView mStepTextView;

        StepsAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Step stepClicked = steps.get(adapterPosition);

            mClickHandler.onClick(stepClicked);
        }
    }
}

