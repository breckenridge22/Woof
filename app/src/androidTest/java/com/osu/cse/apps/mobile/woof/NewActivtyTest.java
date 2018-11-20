package com.osu.cse.apps.mobile.woof;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.osu.cse.apps.mobile.woof.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NewActivtyTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @Test
    public void newActivtyTest() {
        ViewInteraction appCompatEditText = onView(
allOf(withId(R.id.username_text),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
2)));
        appCompatEditText.perform(scrollTo(), replaceText("park@osu.ed"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
allOf(withId(R.id.password_text),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
4)));
        appCompatEditText2.perform(scrollTo(), click());

        ViewInteraction appCompatEditText3 = onView(
allOf(withId(R.id.username_text), withText("park@osu.ed"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
2)));
        appCompatEditText3.perform(scrollTo(), click());

        ViewInteraction appCompatEditText4 = onView(
allOf(withId(R.id.username_text), withText("park@osu.ed"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
2)));
        appCompatEditText4.perform(scrollTo(), replaceText("park@osu.edu"));

        ViewInteraction appCompatEditText5 = onView(
allOf(withId(R.id.username_text), withText("park@osu.edu"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
2),
isDisplayed()));
        appCompatEditText5.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
allOf(withId(R.id.password_text),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
4)));
        appCompatEditText6.perform(scrollTo(), replaceText("password123"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
allOf(withId(R.id.login_button), withText("Sign in"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
11)));
        appCompatButton.perform(scrollTo(), click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatButton2 = onView(
allOf(withId(R.id.new_activity_button), withText("New Activity"),
childAtPosition(
childAtPosition(
withId(R.id.fragment_container),
0),
1),
isDisplayed()));
        appCompatButton2.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatTextView = onView(
allOf(withId(R.id.text_view), withText("Doggo2"),
childAtPosition(
childAtPosition(
withId(R.id.recycler_view),
0),
0),
isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton3 = onView(
allOf(withId(R.id.multi_select_continue_button), withText("Continue"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
0),
isDisplayed()));
        appCompatButton3.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatSpinner = onView(
allOf(withId(R.id.activity_type_spinner),
childAtPosition(
allOf(withId(R.id.activity_view),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
0)),
0),
isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
.inAdapterView(childAtPosition(
withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
0))
.atPosition(1);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatEditText7 = onView(
allOf(withId(R.id.food_amount_edittext),
childAtPosition(
allOf(withId(R.id.food_view),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
2)),
0),
isDisplayed()));
        appCompatEditText7.perform(replaceText("5803"), closeSoftKeyboard());

        ViewInteraction appCompatEditText8 = onView(
allOf(withId(R.id.food_brand_edittext),
childAtPosition(
allOf(withId(R.id.food_view),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
2)),
2),
isDisplayed()));
        appCompatEditText8.perform(replaceText("rai"), closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
allOf(withId(R.id.calories_eaten_edittext),
childAtPosition(
allOf(withId(R.id.food_view),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
2)),
3),
isDisplayed()));
        appCompatEditText9.perform(replaceText("2500"), closeSoftKeyboard());

        ViewInteraction appCompatEditText10 = onView(
allOf(withId(R.id.food_brand_edittext), withText("rai"),
childAtPosition(
allOf(withId(R.id.food_view),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
2)),
2),
isDisplayed()));
        appCompatEditText10.perform(replaceText("raisins"));

        ViewInteraction appCompatEditText11 = onView(
allOf(withId(R.id.food_brand_edittext), withText("raisins"),
childAtPosition(
allOf(withId(R.id.food_view),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
2)),
2),
isDisplayed()));
        appCompatEditText11.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
allOf(withId(R.id.new_activity_submit), withText("Submit"),
childAtPosition(
allOf(withId(R.id.fifthview),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
7)),
0),
isDisplayed()));
        appCompatButton4.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatButton5 = onView(
allOf(withId(R.id.new_activity_button), withText("New Activity"),
childAtPosition(
childAtPosition(
withId(R.id.fragment_container),
0),
1),
isDisplayed()));
        appCompatButton5.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatTextView2 = onView(
allOf(withId(R.id.text_view), withText("Doggo2"),
childAtPosition(
childAtPosition(
withId(R.id.recycler_view),
0),
0),
isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatButton6 = onView(
allOf(withId(R.id.multi_select_continue_button), withText("Continue"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
0),
isDisplayed()));
        appCompatButton6.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatSpinner2 = onView(
allOf(withId(R.id.activity_type_spinner),
childAtPosition(
allOf(withId(R.id.activity_view),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
0)),
0),
isDisplayed()));
        appCompatSpinner2.perform(click());

        DataInteraction appCompatCheckedTextView2 = onData(anything())
.inAdapterView(childAtPosition(
withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
0))
.atPosition(2);
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatButton7 = onView(
allOf(withId(R.id.new_activity_submit), withText("Submit"),
childAtPosition(
allOf(withId(R.id.fifthview),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
7)),
0),
isDisplayed()));
        appCompatButton7.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatButton8 = onView(
allOf(withId(R.id.new_activity_button), withText("New Activity"),
childAtPosition(
childAtPosition(
withId(R.id.fragment_container),
0),
1),
isDisplayed()));
        appCompatButton8.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatTextView3 = onView(
allOf(withId(R.id.text_view), withText("Doggo2"),
childAtPosition(
childAtPosition(
withId(R.id.recycler_view),
0),
0),
isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction appCompatButton9 = onView(
allOf(withId(R.id.multi_select_continue_button), withText("Continue"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
0),
isDisplayed()));
        appCompatButton9.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatSpinner3 = onView(
allOf(withId(R.id.activity_type_spinner),
childAtPosition(
allOf(withId(R.id.activity_view),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
0)),
0),
isDisplayed()));
        appCompatSpinner3.perform(click());

        DataInteraction appCompatCheckedTextView3 = onData(anything())
.inAdapterView(childAtPosition(
withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
0))
.atPosition(3);
        appCompatCheckedTextView3.perform(click());

        ViewInteraction appCompatSpinner4 = onView(
allOf(withId(R.id.bathroom_type_spinner),
childAtPosition(
allOf(withId(R.id.bathroom_view),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
3)),
0),
isDisplayed()));
        appCompatSpinner4.perform(click());

        DataInteraction appCompatCheckedTextView4 = onData(anything())
.inAdapterView(childAtPosition(
withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
0))
.atPosition(1);
        appCompatCheckedTextView4.perform(click());

        ViewInteraction appCompatButton10 = onView(
allOf(withId(R.id.new_activity_submit), withText("Submit"),
childAtPosition(
allOf(withId(R.id.fifthview),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
7)),
0),
isDisplayed()));
        appCompatButton10.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatButton11 = onView(
allOf(withId(R.id.new_activity_button), withText("New Activity"),
childAtPosition(
childAtPosition(
withId(R.id.fragment_container),
0),
1),
isDisplayed()));
        appCompatButton11.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatTextView4 = onView(
allOf(withId(R.id.text_view), withText("Doggo2"),
childAtPosition(
childAtPosition(
withId(R.id.recycler_view),
0),
0),
isDisplayed()));
        appCompatTextView4.perform(click());

        ViewInteraction appCompatButton12 = onView(
allOf(withId(R.id.multi_select_continue_button), withText("Continue"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
0),
isDisplayed()));
        appCompatButton12.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatSpinner5 = onView(
allOf(withId(R.id.activity_type_spinner),
childAtPosition(
allOf(withId(R.id.activity_view),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
0)),
0),
isDisplayed()));
        appCompatSpinner5.perform(click());

        DataInteraction appCompatCheckedTextView5 = onData(anything())
.inAdapterView(childAtPosition(
withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
0))
.atPosition(4);
        appCompatCheckedTextView5.perform(click());

        ViewInteraction appCompatEditText12 = onView(
allOf(withId(R.id.vet_address_edittext),
childAtPosition(
allOf(withId(R.id.vet_view),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
4)),
0),
isDisplayed()));
        appCompatEditText12.perform(replaceText("Hells  Kitchen"), closeSoftKeyboard());

        ViewInteraction appCompatEditText13 = onView(
allOf(withId(R.id.vet_reason_edittext),
childAtPosition(
allOf(withId(R.id.vet_view),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
4)),
1),
isDisplayed()));
        appCompatEditText13.perform(replaceText("The dog coughed blood"), closeSoftKeyboard());

        ViewInteraction appCompatButton13 = onView(
allOf(withId(R.id.new_activity_submit), withText("Submit"),
childAtPosition(
allOf(withId(R.id.fifthview),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
7)),
0),
isDisplayed()));
        appCompatButton13.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatButton14 = onView(
allOf(withId(R.id.new_activity_button), withText("New Activity"),
childAtPosition(
childAtPosition(
withId(R.id.fragment_container),
0),
1),
isDisplayed()));
        appCompatButton14.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatTextView5 = onView(
allOf(withId(R.id.text_view), withText("Doggggo"),
childAtPosition(
childAtPosition(
withId(R.id.recycler_view),
1),
0),
isDisplayed()));
        appCompatTextView5.perform(click());

        ViewInteraction appCompatButton15 = onView(
allOf(withId(R.id.multi_select_continue_button), withText("Continue"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
0),
isDisplayed()));
        appCompatButton15.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatButton16 = onView(
allOf(withId(R.id.new_activity_submit), withText("Submit"),
childAtPosition(
allOf(withId(R.id.fifthview),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
7)),
0),
isDisplayed()));
        appCompatButton16.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatButton17 = onView(
allOf(withId(R.id.begin_button), withText("Begin Walk"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.RelativeLayout")),
0),
2),
isDisplayed()));
        appCompatButton17.perform(click());

         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatButton18 = onView(
allOf(withId(R.id.finish_walk_button), withText("Finish Walk"),
childAtPosition(
childAtPosition(
withId(R.id.fragment_container),
0),
0),
isDisplayed()));
        appCompatButton18.perform(click());
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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
