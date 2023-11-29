package com.example.stylespo.view;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.stylespo.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MakePostTest {

    @Rule
    public ActivityScenarioRule<LoginAndSignUpActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginAndSignUpActivity.class);

    @Test
    public void makePostTest() {
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.ProfileFragment), withContentDescription("Profile"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationView),
                                        0),
                                3),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.drop_down_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.homepage_frag_container),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(android.R.id.title), withText("Settings"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("Settings")));

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.AddFragment), withContentDescription("Make a post"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationView),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.addTag),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.homepage_frag_container),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("dog"), closeSoftKeyboard());

        ViewInteraction editText = onView(
                allOf(withId(R.id.addTag), withText("dog"),
                        withParent(withParent(withId(R.id.homepage_frag_container))),
                        isDisplayed()));
        editText.check(matches(isDisplayed()));

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.postButton), withText("Post"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.homepage_frag_container),
                                        0),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction bottomNavigationItemView3 = onView(
                allOf(withId(R.id.ProfileFragment), withContentDescription("Profile"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationView),
                                        0),
                                3),
                        isDisplayed()));
        bottomNavigationItemView3.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
