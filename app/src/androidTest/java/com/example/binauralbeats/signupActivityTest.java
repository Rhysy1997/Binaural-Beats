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
import static org.junit.Assert.*;


@RunWith(AndroidJUnit4ClassRunner.class)
public class signupActivityTest {

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
    public ActivityTestRule<signupActivity> mRule = new ActivityTestRule<>(signupActivity.class);

    private String msignupUsernameValid = "Test User"; // between 1 and 20 chars
    private String msignupUsernameEmpty = " ";
    private String msignupUsernameError = "Username longer than 20 characters"; // longer than 20 chars

    private String msignupEmailValid = "test@gmail.com"; // between 1 and 20 chars with @ and domain name
    private String msignupEmailEmpty = " ";
    private String msignupEmailError = "anewtestuserheretrying@gmail.com"; // longer than 30 chars
    private String msignupEmailErrorInvalid = "testuer@nothing"; // longer than 30 chars

    private String msignupPasswordValid = "123456"; //6 or more chars
    private String msignupPasswordEmpty = " ";
    private String msignupPasswordError = "123"; //less than 6 chars


    @Test
    public void test_input_username_empty(){
        onView(withId(R.id.text_input_username_etSignup)).perform(typeText(msignupUsernameEmpty));

        //click signup button
        Espresso.onView(withId(R.id.button_signup)).perform(click());

        onView(withId(R.id.text_input_username_signup)).check
                (matches(hasTextInputLayoutErrorText(mRule.getActivity().getString(R.string
                        .Username_empty))));
    }

    @Test
    public void test_input_username_error_length(){

        onView(withId(R.id.text_input_username_etSignup)).perform(typeText(msignupUsernameError));

        //click signup button
        Espresso.onView(withId(R.id.button_signup)).perform(click());

        onView(withId(R.id.text_input_username_signup)).check
                (matches(hasTextInputLayoutErrorText(mRule.getActivity().getString(R.string
                        .Username_error))));
    }

    @Test
    public void test_input_email_empty(){

        //enter valid username
        onView(withId(R.id.text_input_username_etSignup)).perform(typeText(msignupUsernameValid));

        //enter empty
        onView(withId(R.id.text_input_email_etSignup)).perform(typeText(msignupEmailEmpty));

        //click signup button
        Espresso.onView(withId(R.id.button_signup)).perform(click());

        //check for error
        onView(withId(R.id.text_input_email_signup)).check
                (matches(hasTextInputLayoutErrorText(mRule.getActivity().getString(R.string
                        .Email_empty))));
    }

    @Test
    public void test_input_email_error_length(){

        //enter valid username
        onView(withId(R.id.text_input_username_etSignup)).perform(typeText(msignupUsernameValid));

        //enter error
        onView(withId(R.id.text_input_email_etSignup)).perform(typeText(msignupEmailError));

        //click signup button
        Espresso.onView(withId(R.id.button_signup)).perform(click());

        //check for error
        onView(withId(R.id.text_input_email_signup)).check
                (matches(hasTextInputLayoutErrorText(mRule.getActivity().getString(R.string
                        .Email_error))));
    }

    @Test
    public void test_input_email_error_incorrect_format(){

        //enter valid username
        onView(withId(R.id.text_input_username_etSignup)).perform(typeText(msignupUsernameValid));

        //enter error
        onView(withId(R.id.text_input_email_etSignup)).perform(typeText(msignupEmailErrorInvalid));

        //click signup button
        Espresso.onView(withId(R.id.button_signup)).perform(click());

        //check for error
        onView(withId(R.id.text_input_email_signup)).check
                (matches(hasTextInputLayoutErrorText(mRule.getActivity().getString(R.string
                        .Email_error_invalid))));
    }

    @Test
    public void test_input_password_empty(){

        //enter valid username
        onView(withId(R.id.text_input_username_etSignup)).perform(typeText(msignupUsernameValid));

        //enter valid email
        onView(withId(R.id.text_input_email_etSignup)).perform(typeText(msignupEmailValid));

        //enter empty password
        onView(withId(R.id.text_input_password_etSignup)).perform(typeText(msignupPasswordEmpty));

        //click signup button
        Espresso.onView(withId(R.id.button_signup)).perform(click());

        //check for error
        onView(withId(R.id.text_input_pass_signup)).check
                (matches(hasTextInputLayoutErrorText(mRule.getActivity().getString(R.string
                        .Password_empty))));
    }

    @Test
    public void test_input_password_error_length(){

        //enter valid username
        onView(withId(R.id.text_input_username_etSignup)).perform(typeText(msignupUsernameValid));

        //enter valid email
        onView(withId(R.id.text_input_email_etSignup)).perform(typeText(msignupEmailValid));

        //enter empty password
        onView(withId(R.id.text_input_password_etSignup)).perform(typeText(msignupPasswordError));

        //click signup button
        Espresso.onView(withId(R.id.button_signup)).perform(click());

        //check for error
        onView(withId(R.id.text_input_pass_signup)).check
                (matches(hasTextInputLayoutErrorText(mRule.getActivity().getString(R.string
                        .Password_error))));
    }

    @Test
    public void test_user_signup_scenario_valid(){

        //enter valid username
        onView(withId(R.id.text_input_username_etSignup)).perform(typeText(msignupUsernameValid));

        //enter valid email
        onView(withId(R.id.text_input_email_etSignup)).perform(typeText(msignupEmailValid));

        //enter valid password
        onView(withId(R.id.text_input_password_etSignup)).perform(typeText(msignupPasswordValid));

        //click signup button and check profile is displayed
        Espresso.onView(withId(R.id.button_signup)).perform(click()).check(matches(isDisplayed()));
    }

    @Test
    public void test_email_already_registered(){

        //enter valid username
        onView(withId(R.id.text_input_username_etSignup)).perform(typeText(msignupUsernameValid));

        //enter valid email
        onView(withId(R.id.text_input_email_etSignup)).perform(typeText(msignupEmailValid));

        //enter valid password
        onView(withId(R.id.text_input_password_etSignup)).perform(typeText(msignupPasswordValid));

        //click signup button
        Espresso.onView(withId(R.id.button_signup)).perform(click());

        //check toast message for choosing a different name
        onView(withText(R.string.Toast_email)).inRoot(withDecorView(not(mRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

}