

package com.example.stylespo;
import org.junit.Before;
import org.junit.Test;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;

import static org.junit.Assert.*;


import com.example.stylespo.view.HomeActivity;
import com.example.stylespo.view.LoginAndSignUpActivity;
import com.example.stylespo.view.LoginFragment;
import static androidx.fragment.app.testing.FragmentScenario.launchInContainer;

import android.view.View;
import android.widget.Button;

public class LoginFragmentTest {
    private  LoginFragment mLoginFragment;
    private Button mLoginButton;
    @Rule
    public ActivityTestRule<LoginAndSignUpActivity> activityRule =
            new ActivityTestRule<>(LoginAndSignUpActivity.class);

    public LoginFragmentTest() {
        // Initialization code, if needed
    }
    @Before
    public void setup() {
        // Launch the fragment in a container
        try (FragmentScenario<LoginFragment> scenario = FragmentScenario.launchInContainer(LoginFragment.class)) {
            scenario.onFragment(fragment -> {
                mLoginFragment = fragment;
                assertNotNull(mLoginFragment);  // Assuming you want to assert that the fragment is not null

                // Replace with the actual login button ID
               mLoginButton = mLoginFragment.getView().findViewById(R.id.log_in_button);
              assertNotNull(mLoginButton);  // Assuming you want to assert that the login button is not null
            });
        }
    }

        @Test
        public void testPreconditions () {
            assertNotNull(mLoginFragment);
            assertNotNull(mLoginButton);
        }


}
