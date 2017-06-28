package com.adebayoyeleye.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.adebayoyeleye.bakingapp.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Adebayo on 24/06/2017.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void clickRecipe_LaunchStepsActivity() {
        onView((withId(R.id.recyclerview_recipes)))
                .perform(click());
        onView((withId(R.id.fragment_master_list_container))).check(matches(isDisplayed()));
    }


}
