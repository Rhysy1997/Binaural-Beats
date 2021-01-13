package com.example.binauralbeats;

import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {

    //custom matcher found online for testing textInputLayouts with editText within
    //this matcher checks the error message for the textInputLayout
    public static Matcher<View> hasTextInputLayoutErrorText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getError();

                if (error == null) {
                    return false;
                }

                String hint = error.toString();

                return expectedErrorText.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    @Rule
    public ActivityTestRule<MainActivity> mRule = new ActivityTestRule<>(MainActivity.class);

    private String mCreateTitleValid = "new beat";
    private String mCreateTitleEmpty = " ";

    private String mCreateBeatValid = "2";
    private String mCreateBeatError = "50";

    private String mCreateWaveTypeValid = "Delta";

    private String mCreateBaseValid = "300";
    private String mCreateBaseError = "1500";


    @Test
    public void test_input_title_empty(){
        onView(withId(R.id.text_input_etTitle)).perform(typeText(mCreateTitleEmpty));

        onView(withId(R.id.text_input_title)).check
                (matches(hasTextInputLayoutErrorText(mRule.getActivity().getString(R.string
                        .Title_error_empty))));
    }

    @Test
    public void test_input_beat_error(){
        onView(withId(R.id.text_input_etBeat)).perform(typeText(mCreateBeatError));

        onView(withId(R.id.text_input_beat)).check
                (matches(hasTextInputLayoutErrorText(mRule.getActivity().getString(R.string
                        .Beat_Error_values))));
    }

    @Test
    public void test_input_base_error(){
        onView(withId(R.id.text_input_etBase)).perform(typeText(mCreateBaseError));

        onView(withId(R.id.text_input_base)).check
                (matches(hasTextInputLayoutErrorText(mRule.getActivity().getString(R.string
                        .Base_Error_values))));
    }

    @Test
    public void test_button_test(){
        //click test button to check for crash
        Espresso.onView(withId(R.id.button_test)).perform(click());
    }

    @Test
    public void test_button_stop(){
        //click stop button to check for crash
        Espresso.onView(withId(R.id.button_stop)).perform(click());
    }

    @Test
    public void test_button_save(){
        //click save button to check for crash
        Espresso.onView(withId(R.id.buttonSave)).perform(click());
    }

    @Test
    public void test_user_create_scenario_valid(){
        //input text to title field
       onView(withId(R.id.text_input_etTitle)).perform(typeText(mCreateTitleValid));

       //close keyboard
        Espresso.closeSoftKeyboard();

        //input text to beat field
        onView(withId(R.id.text_input_etBeat)).perform(typeText(mCreateBeatValid));

        //close keyboard
        Espresso.closeSoftKeyboard();

        //check wavetype textfield
        Espresso.onView(withId(R.id.textView_waveType)).check(matches(withText(mCreateWaveTypeValid)));

        //input text to base field
        onView(withId(R.id.text_input_etBase)).perform(typeText(mCreateBaseValid));

        //close keyboard
        Espresso.closeSoftKeyboard();

        //click test button to check for crash
        Espresso.onView(withId(R.id.button_test)).perform(click());

        //click stop button to check for crash
        Espresso.onView(withId(R.id.button_stop)).perform(click());

        //click save button to check for crash
        Espresso.onView(withId(R.id.buttonSave)).perform(click());

        //check toast message says track has been saved
        onView(withText(R.string.Toast_saved)).inRoot(withDecorView(not(mRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

    }

    @Test
    public void test_user_create_scenario_error_title(){
        //input text to title field
        onView(withId(R.id.text_input_etTitle)).perform(typeText(mCreateTitleEmpty));

        //close keyboard
        Espresso.closeSoftKeyboard();

        //input text to beat field
        onView(withId(R.id.text_input_etBeat)).perform(typeText(mCreateBeatValid));

        //close keyboard
        Espresso.closeSoftKeyboard();

        //input text to base field
        onView(withId(R.id.text_input_etBase)).perform(typeText(mCreateBaseValid));

        //close keyboard
        Espresso.closeSoftKeyboard();

        //click test button to check for crash
        Espresso.onView(withId(R.id.button_test)).perform(click());

        //click stop button to check for crash
        Espresso.onView(withId(R.id.button_stop)).perform(click());

        //click save button to check for crash
        Espresso.onView(withId(R.id.buttonSave)).perform(click());

        //check toast message says to fix all inputs
        onView(withText(R.string.Toast_error)).inRoot(withDecorView(not(mRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void test_user_create_scenario_error_beat(){
        //input text to title field
        onView(withId(R.id.text_input_etTitle)).perform(typeText(mCreateTitleValid));

        //close keyboard
        Espresso.closeSoftKeyboard();

        //input text to beat field
        onView(withId(R.id.text_input_etBeat)).perform(typeText(mCreateBeatError));

        //close keyboard
        Espresso.closeSoftKeyboard();

        //input text to base field
        onView(withId(R.id.text_input_etBase)).perform(typeText(mCreateBaseValid));

        //close keyboard
        Espresso.closeSoftKeyboard();

        //click test button to check for crash
        Espresso.onView(withId(R.id.button_test)).perform(click());

        //click stop button to check for crash
        Espresso.onView(withId(R.id.button_stop)).perform(click());

        //click save button to check for crash
        Espresso.onView(withId(R.id.buttonSave)).perform(click());

        //check toast message says to fix all inputs
        onView(withText(R.string.Toast_error)).inRoot(withDecorView(not(mRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void test_user_create_scenario_error_base(){
        //input text to title field
        onView(withId(R.id.text_input_etTitle)).perform(typeText(mCreateTitleValid));

        //close keyboard
        Espresso.closeSoftKeyboard();

        //input text to beat field
        onView(withId(R.id.text_input_etBeat)).perform(typeText(mCreateBeatValid));

        //close keyboard
        Espresso.closeSoftKeyboard();

        //input text to base field
        onView(withId(R.id.text_input_etBase)).perform(typeText(mCreateBaseError));

        //close keyboard
        Espresso.closeSoftKeyboard();

        //click test button to check for crash
        Espresso.onView(withId(R.id.button_test)).perform(click());

        //click stop button to check for crash
        Espresso.onView(withId(R.id.button_stop)).perform(click());

        //click save button to check for crash
        Espresso.onView(withId(R.id.buttonSave)).perform(click());

        //check toast message says to fix all inputs
        onView(withText(R.string.Toast_error)).inRoot(withDecorView(not(mRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void test_navigation_home_is_displayed(){
        onView(withId(R.id.nav_home)).perform(click()).check(matches(isDisplayed()));
    }

    @Test
    public void test_navigation_library_is_displayed(){
        onView(withId(R.id.nav_library)).perform(click()).check(matches(isDisplayed()));
    }

    @Test
    public void test_navigation_more_is_displayed(){
        onView(withId(R.id.nav_settings)).perform(click()).check(matches(isDisplayed()));
    }

    @Test
    public void test_navigation_create_is_displayed(){
        onView(withId(R.id.nav_create)).perform(click()).check(matches(isDisplayed()));
    }

}


