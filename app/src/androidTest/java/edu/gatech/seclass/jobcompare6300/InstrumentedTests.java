package edu.gatech.seclass.jobcompare6300;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.view.View;
import android.widget.SeekBar;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityMain =
            new ActivityScenarioRule<>(MainActivity.class);


    // Custom ViewAction to set the progress of a SeekBar
    public static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }

            @Override
            public String getDescription() {
                return "Set SeekBar progress to: " + progress;
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((SeekBar) view).setProgress(progress); // Set the progress
            }
        };
    }
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("edu.gatech.seclass.jobcompare6300", appContext.getPackageName());
    }


    // Test to verify App Start
    @Test
    public void checkTextViewDisplayed() {
        onView(withId(R.id.appName)) // Find the TextView
                .check(matches(isDisplayed())); // Assert that it's displayed
    }


    // Test to verify if the main menu  are displayed
    @Test
    public void verifyMainMenuOptionsDisplayed() {
        // Check if "Current Job Details" button is displayed
        onView(withId(R.id.currentJobDetails))
                .check(matches(isDisplayed()));

        onView(withId(R.id.menu))
                .check(matches(isDisplayed()));

        onView(withId(R.id.jobOffers))
                .check(matches(isDisplayed()));


        onView(withId(R.id.settings))
                .check(matches(isDisplayed()));


        onView(withId(R.id.compareJob))
                .check(matches(isDisplayed()));
    }

    // Test to simulate button clicks and verify the correct action for main menu
    @Test
    public void verifyButtonClicks() {
        // Simulate click on "Current Job Details" button
        onView(withId(R.id.currentJobDetails)).perform(click());
        pressBack();

        onView(withId(R.id.jobOffers)).perform(click());
        pressBack();

        onView(withId(R.id.settings)).perform(click());
        pressBack();

        onView(withId(R.id.compareJob)).perform(click());
        pressBack();
    }


    // Test to verify entering current job details
    @Test
    public void verifyCurrentJobDetailsEntered() {
        // Navigate to the Current Job Details screen
        onView(withId(R.id.currentJobDetails)).perform(click());

        // Enter valid details
        String jobTitle = "Software Engineer";
        String company = "Company A";
        String city = "Atlanta";
        String state = "GA";
        String index = "125";
        String salary = "100000";
        String bonus = "10000";
        String stock = "5000";
        String learningDev = "4000";
        String familyAssistance = "3000";

        onView(withId(R.id.textEditTitle)).perform(typeText(jobTitle));
        onView(withId(R.id.textEditCompany)).perform(typeText(company));
        onView(withId(R.id.textEditCity)).perform(typeText(city));
        onView(withId(R.id.textEditState)).perform(typeText(state));
        onView(withId(R.id.textEditIndex)).perform(typeText(index));
        onView(withId(R.id.textEditSalary)).perform(typeText(salary));
        onView(withId(R.id.textEditBonus)).perform(typeText(bonus));
        onView(withId(R.id.textEditStock)).perform(typeText(stock));
        onView(withId(R.id.textEditLearning)).perform(typeText(learningDev));
        onView(withId(R.id.textEditAssistance)).perform(typeText(familyAssistance),closeSoftKeyboard());
        onView(withId(R.id.seekBarRetirement)).perform(setProgress(13));


    }



    // Test to verify entering current job details
    @Test
    public void verifyNewJobEntered() {
        // Navigate to the Current Job Details screen
        onView(withId(R.id.jobOffers)).perform(click());

        // Enter valid details
        String jobTitle = "Software Engineer";
        String company = "Company B";
        String city = "New York";
        String state = "NY";
        String index = "135";
        String salary = "150000";
        String bonus = "20000";
        String stock = "10000";
        String learningDev = "8000";
        String familyAssistance = "9000";

        onView(withId(R.id.textEditTitle)).perform(typeText(jobTitle));
        onView(withId(R.id.textEditCompany)).perform(typeText(company));
        onView(withId(R.id.textEditCity)).perform(typeText(city));
        onView(withId(R.id.textEditState)).perform(typeText(state));
        onView(withId(R.id.textEditIndex)).perform(typeText(index));
        onView(withId(R.id.textEditSalary)).perform(typeText(salary));
        onView(withId(R.id.textEditBonus)).perform(typeText(bonus));
        onView(withId(R.id.textEditStock)).perform(typeText(stock));
        onView(withId(R.id.textEditLearning)).perform(typeText(learningDev));
        onView(withId(R.id.textEditAssistance)).perform(typeText(familyAssistance),closeSoftKeyboard());
        onView(withId(R.id.seekBarRetirement)).perform(setProgress(17));

    }
}



