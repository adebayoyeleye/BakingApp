package com.adebayoyeleye.bakingapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adebayoyeleye.bakingapp.R;
import com.adebayoyeleye.bakingapp.objects.Step;
import com.squareup.picasso.Picasso;

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
    private Context context;


    StepsAdapter(Context c, List<Step> steps, StepsAdapterOnClickHandler clickHandler) {
        context = c;
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
        String stepImageUrl = step.getThumbnailURL();

        holder.mStepTextView.setText(shortDescription);
        if (stepImageUrl != null && !(TextUtils.isEmpty(stepImageUrl))) {
            Picasso.with(context)
                    .load(stepImageUrl)
                    .placeholder(R.drawable.ic_do_not_disturb)
                    .error(R.color.colorPrimary)
                    .into(holder.mStepImage);
        }

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

        @BindView(R.id.iv_thumbnail_image)
        public ImageView mStepImage;
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

