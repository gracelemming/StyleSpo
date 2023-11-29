package com.example.stylespo.view;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
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
import com.example.stylespo.view.HomepageFragment;
import com.example.stylespo.view.HomeActivity;
import com.example.stylespo.view.LoginAndSignUpActivity;
import com.example.stylespo.view.LoginFragment;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginSuccess {

    @Rule
    public ActivityScenarioRule<LoginAndSignUpActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginAndSignUpActivity.class);

    @Test
    public void loginSuccess() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.email),

                        isDisplayed()));
        appCompatEditText.perform(replaceText("g.lemming13@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password),

                        isDisplayed()));
        appCompatEditText2.perform(replaceText("password"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.log_in_button), withText("LOGIN"),

                        isDisplayed()));
        materialButton.perform(click());


        ViewInteraction imageView = onView(
                allOf(withId(R.id.profile_image_view),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));
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
