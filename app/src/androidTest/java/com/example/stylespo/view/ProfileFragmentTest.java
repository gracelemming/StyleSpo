import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.stylespo.view.LoginAndSignUpActivity;
import com.example.stylespo.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class ProfileFragmentTest {

    @Rule
    public ActivityScenarioRule<LoginAndSignUpActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginAndSignUpActivity.class);

    @Test
    public void profileFragmentTest() {
        // Perform actions to navigate to the ProfileFragment, assuming there's a login process
        onView(withId(R.id.email)).perform(click(), replaceText("g.lemming13@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(replaceText("password"), closeSoftKeyboard());
        onView(withId(R.id.log_in_button)).perform(click());

        // Wait for the navigation to complete
        // ...

        // Perform click on the drop-down button in the ProfileFragment
        onView(withId(R.id.drop_down_button)).perform(click());

        // Perform click on the "Settings" option in the drop-down menu
        onView(withText("Settings Selected")).perform(click());

        // Verify that the SettingsActivity is displayed
        onView(withId(R.id.action_settings)).check(matches(isDisplayed()));
        // Add more verifications as needed

        // Navigate back to the ProfileFragment if needed
        Espresso.pressBack();

        // Perform click on the profile image in the ProfileFragment
        onView(withId(R.id.profile_image)).perform(click());

        // Verify that the camera is launched
        // Add more verifications related to the camera behavior
    }


}
