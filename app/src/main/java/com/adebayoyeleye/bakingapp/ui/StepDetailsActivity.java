package com.adebayoyeleye.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.adebayoyeleye.bakingapp.R;
import com.adebayoyeleye.bakingapp.objects.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsActivity extends AppCompatActivity {

    @BindView(R.id.btn_nxt)
    Button mNextButton;
    @BindView(R.id.btn_prev)
    Button mPreviousButton;
    private List<Step> steps;
    private int stepIndex;
    private Step stepClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Step.STEP_EXTRA)) {

                steps = intentThatStartedThisActivity.getParcelableArrayListExtra(Step.STEP_EXTRA);
                stepIndex = intentThatStartedThisActivity.getIntExtra(Step.INDEX_EXTRA, 0);
                stepClicked = steps.get(stepIndex);
                VideoFragment videoFragment = new VideoFragment();
                videoFragment.setStep(stepClicked);
                videoFragment.setContext(this);
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.fragment_video_container, videoFragment)
                        .commit();
            }

        }
        if (stepIndex == 0) {
            mPreviousButton.setVisibility(View.GONE);
        } else if (stepIndex >= (steps.size() - 1)) {
            mNextButton.setVisibility(View.GONE);
        }

    }

    void previousStep(View view) {
        Bundle b = new Bundle();
        stepIndex--;
        b.putParcelableArrayList(Step.STEP_EXTRA, (ArrayList<? extends Parcelable>) steps);
        b.putInt(Step.INDEX_EXTRA, stepIndex);
        final Intent intent = new Intent(this, StepDetailsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    void nextStep(View view) {
        Bundle b = new Bundle();
        stepIndex++;
        b.putParcelableArrayList(Step.STEP_EXTRA, (ArrayList<? extends Parcelable>) steps);
        b.putInt(Step.INDEX_EXTRA, stepIndex);
        final Intent intent = new Intent(this, StepDetailsActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }
}
