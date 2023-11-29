package com.example.stylespo;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.stylespo.view.LoginAndSignUpActivity;
import com.example.stylespo.view.LoginFragment;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private LoginAndSignUpActivity mLASUA;
    private LoginFragment mLoginFrag;

    private Button mLoginButton;
    private CountingIdlingResource countingIdlingResource = new CountingIdlingResource("LoginFragmentTest");


    @Rule
    public ActivityScenarioRule<LoginAndSignUpActivity> mActivityRule =
            new ActivityScenarioRule<>(LoginAndSignUpActivity.class);

    @Before
    public void setUp() {
        // Launch the LoginFragment in the fragment scenario
        mActivityRule.getScenario().onActivity(activity -> {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            mLoginFrag = (LoginFragment) fragmentManager.findFragmentById(R.id.login_frag_container);
            if (mLoginFrag != null) {
                View fragView = mLoginFrag.getView();
                if (fragView != null) {
                    mLoginButton = fragView.findViewById(R.id.log_in_button);
                }
            }
        });
        IdlingRegistry.getInstance().register(countingIdlingResource);


    }



    @Test
    public void testPreconditions() {
        assertNotNull(mLoginFrag);
        assertNotNull(mLoginButton);
    }

    @Test
    public void testLoginButtonClick_Failure() {
        // Example test scenario: Type invalid email and password, then click login button
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText("invalid-email"));
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.typeText("invalid-password"));
        Espresso.onView(ViewMatchers.withId(R.id.log_in_button)).perform(ViewActions.click());

        // Verify that the login failed message is displayed
        Espresso.onView(ViewMatchers.withText("Login failed.")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }




}