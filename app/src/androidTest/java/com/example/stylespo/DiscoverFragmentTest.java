package com.example.stylespo;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.stylespo.view.DiscoverFragment;
import com.example.stylespo.view.LoginAndSignUpActivity;

import org.hamcrest.Matchers;
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
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertNotNull;
import static kotlinx.coroutines.flow.FlowKt.withIndex;

public class DiscoverFragmentTest {

    @Rule
    public ActivityTestRule<LoginAndSignUpActivity> activityRule =
            new ActivityTestRule<>(LoginAndSignUpActivity.class);

    @Before
    public void setup() {
        // Launch the DiscoverFragment in a container
        // Type the email in the email field
        String email = "g.lemming13@gmail.com";
        String password = "password";
        onView(withId(R.id.email))
                .perform(replaceText(email), closeSoftKeyboard());

        // Type the password in the password field
        onView(withId(R.id.password))
                .perform(replaceText(password), closeSoftKeyboard());

        // Click on the login button
        onView(withId(R.id.log_in_button))
                .perform(click());

        // Ensure that the login is successful (you might need to add additional checks)
        onView((withId(R.id.activity_home)))
                .check(matches(isDisplayed()));
       // FragmentScenario.launchInContainer(DiscoverFragment.class);

    }

    @Test
    public void testDiscoverFragment() {
        // Type "floor" in the search view
        FragmentScenario<DiscoverFragment> scenario = FragmentScenario.launchInContainer(DiscoverFragment.class);

        onView(withId(R.id.searchView))
                .perform(replaceText("floor"), closeSoftKeyboard());

        // Click on the search button
        onView(withContentDescription("Search"))
                .perform(click());

        // Verify that the image view is displayed
        onView(withId(R.id.image))
                .check(matches(isDisplayed()));
        scenario.close();
    }
}
