package com.example.android.bakemaster.ui;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.android.bakemaster.R;
import com.example.android.bakemaster.rest.OkHttpProvider;
import com.jakewharton.espresso.OkHttp3IdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityCardClickTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testIfCardClickIntentPassesCorrectData() {

        IdlingResource idlingResource = OkHttp3IdlingResource.create(
                "okhttp", OkHttpProvider.getOkHttpInstance());
        IdlingRegistry.getInstance().register(idlingResource);

        Intents.init();

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recipe_rv),
                        childAtPosition(
                                withId(android.R.id.content)
                        )));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        intended(allOf(hasComponent(RecipeDetailActivity.class.getName()),
                hasExtraWithKey(MainActivity.RECIPE_NAME_KEY),
                hasExtraWithKey(MainActivity.STEPS_KEY),
                hasExtraWithKey(MainActivity.INGREDIENTS_KEY)));

        Intents.release();
        IdlingRegistry.getInstance().unregister(idlingResource);

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + 0 + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(0));
            }
        };
    }
}
