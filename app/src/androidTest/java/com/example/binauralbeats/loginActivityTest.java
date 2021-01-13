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
public class loginActivityTest {

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
    public ActivityTestRule<loginActivity> mRule = new ActivityTestRule<>(loginActivity.class);

    private String mloginEmailValid = "test@gmail.com"; // between 1 and 20 chars with @ and domain name
    private String mloginEmailValidDoesntExist = "testdoesntexist@gmail.com"; // between 1 and 20 chars with @ and domain name

    private String mloginEmailEmpty = " ";
    private String mloginEmailError = "anewtestuserheretrying@gmail.com"; // longer than 30 chars
    private String mloginEmailErrorInvalid = "testuer@nothing"; // invalid

    private String mloginPasswordValid = "123456"; //6 or more chars
    private String mloginPasswordValidDoesntExist = "1233333"; //doesnt exist

    private String mloginPasswordEmpty = " ";
    private String mloginPasswordError = "123"; //less than 6 chars


    @Test
    public void test_input_email_empty(){

        //enter empty email
        onView(withId(R.id.text_input_email_etLogin)).perform(typeText(mloginEmailEmpty));

        //click signup button
        Espresso.onView(withId(R.id.button_login)).perform(click());

        //check for error
        onView(withId(R.id.text_input_email_login)).check
                (matches(hasTextInputLayoutErrorText(mRule.getActivity().getString(R.string
                        .Email_empty))));
    }

    @Test
    public void test_input_email_error_length(){

        //enter error
        onView(withId(R.id.text_input_email_etLogin)).perform(typeText(mloginEmailError));

        //click signup button
        Espresso.onView(withId(R.id.button_login)).perform(click());

        //check for error
        onView(withId(R.id.text_input_email_login)).check
                (matches(hasTextInputLayoutErrorText(mRule.getActivity().getString(R.string
                        .Email_error))));
    }

    @Test
    public void test_input_email_error_incorrect_format(){

        //enter error
        onView(withId(R.id.text_input_email_etLogin)).perform(typeText(mloginEmailErrorInvalid));

        //click signup button
        Espresso.onView(withId(R.id.button_login)).perform(click());

        //check for error
        onView(withId(R.id.text_input_email_login)).check
                (matches(hasTextInputLayoutErrorText(mRule.getActivity().getString(R.string
                        .Email_error_invalid))));
    }

    @Test
    public void test_input_password_empty(){

        //enter valid email
        onView(withId(R.id.text_input_email_etLogin)).perform(typeText(mloginEmailValid));

        //enter empty password
        onView(withId(R.id.text_input_password_etLogin)).perform(typeText(mloginPasswordEmpty));

        //click signup button
        Espresso.onView(withId(R.id.button_login)).perform(click());

        //check for error
        onView(withId(R.id.text_input_pass_login)).check
                (matches(hasTextInputLayoutErrorText(mRule.getActivity().getString(R.string
                        .Password_empty))));
    }

    @Test
    public void test_input_password_error_length(){

        //enter valid email
        onView(withId(R.id.text_input_email_etLogin)).perform(typeText(mloginEmailValid));

        //enter error password
        onView(withId(R.id.text_input_password_etLogin)).perform(typeText(mloginPasswordError));

        //click signup button
        Espresso.onView(withId(R.id.button_login)).perform(click());

        //check for error
        onView(withId(R.id.text_input_pass_login)).check
                (matches(hasTextInputLayoutErrorText(mRule.getActivity().getString(R.string
                        .Password_error))));
    }

    @Test
    public void test_email_does_not_exist(){

        //enter valid email
        onView(withId(R.id.text_input_email_etLogin)).perform(typeText(mloginEmailValidDoesntExist));

        //enter valid password
        onView(withId(R.id.text_input_password_etLogin)).perform(typeText(mloginPasswordValid));

        //click signup button
        Espresso.onView(withId(R.id.button_login)).perform(click());

        //check toast message for non existing email
        onView(withText(R.string.Toast_email_does_not_exist)).inRoot(withDecorView(not(mRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void test_password_does_not_exist(){

        //enter valid email
        onView(withId(R.id.text_input_email_etLogin)).perform(typeText(mloginEmailValid));

        //enter valid password
        onView(withId(R.id.text_input_password_etLogin)).perform(typeText(mloginPasswordValidDoesntExist));

        //click signup button
        Espresso.onView(withId(R.id.button_login)).perform(click());

        //check toast message for non existing email
        onView(withText(R.string.Toast_password_does_not_exist)).inRoot(withDecorView(not(mRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

}