package com.example.stylespo;

import androidx.test.rule.ActivityTestRule;

import com.example.stylespo.view.LoginAndSignUpActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.fragment.app.testing.FragmentScenario.launchInContainer;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertNotNull;

public class LoginFragmentSuccess {

    @Rule
    public ActivityTestRule<LoginAndSignUpActivity> activityRule =
            new ActivityTestRule<>(LoginAndSignUpActivity.class);

    @Before
    public void setup() {
        String email = "g.lemming13@gmail.com";
        String password = "password";
        onView(withId(R.id.email))
                .perform(replaceText(email), closeSoftKeyboard());

        onView(withId(R.id.password))
                .perform(replaceText(password), closeSoftKeyboard());

        onView(withId(R.id.log_in_button))
                .perform(click());

    }

    @Test
    public void testHomepage() {
        onView((withId(R.id.activity_home)))
                .check(matches(isDisplayed()));

    }


}
