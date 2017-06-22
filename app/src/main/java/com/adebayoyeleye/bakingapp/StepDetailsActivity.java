package com.adebayoyeleye.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class StepDetailsActivity extends AppCompatActivity {

    private Step stepClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Step.STEP_EXTRA)) {

                stepClicked = intentThatStartedThisActivity.getParcelableExtra(Step.STEP_EXTRA);
                VideoFragment videoFragment = new VideoFragment();
                videoFragment.setStep(stepClicked);
                videoFragment.setContext(this);
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.fragment_video_container, videoFragment)
                        .commit();
            }

        }

    }

}
