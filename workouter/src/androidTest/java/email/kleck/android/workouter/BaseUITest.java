package email.kleck.android.workouter;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.RootMatchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Abstract base ui testing class containing shared methods used by various test implementations
 */
public abstract class BaseUITest {

    protected void navigateTo(int item) {
        openNavigationDrawer();
        clickNavigationItem(item);
    }

    protected void openNavigationDrawer() {
        onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()))
                .perform(click());
    }

    protected void clickNavigationItem(int item) {
        onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        item),
                        isDisplayed()))
                .perform(click());
    }

    protected ViewInteraction getViewInteraction(int id, String text, int childPos, int subChildPos, String subChildClassName) {
        return onView(
                allOf(withId(id), withText(text),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is(subChildClassName)),
                                        subChildPos),
                                childPos),
                        isDisplayed()));
    }

    protected ViewInteraction getViewInteraction(int id, int childPos, int subChildPos, String subChildClassName) {
        return onView(
                allOf(withId(id),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is(subChildClassName)),
                                        subChildPos),
                                childPos),
                        isDisplayed()));
    }

    protected ViewInteraction getViewInteraction(int id, int childPos, int subChildPos, int subChildId) {
        return onView(
                allOf(withId(id),
                        childAtPosition(
                                childAtPosition(
                                        withId(subChildId),
                                        subChildPos),
                                childPos),
                        isDisplayed()));
    }

    protected void clickAutoComplete(int index, String text) {
        onView(withText(text))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
    }

    protected Matcher<View> childAtPosition(
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
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    protected void removeTableItem(int tableId, int tableRow, int buttonId, int buttonNumber) {
        onView(
                childAtPosition(
                        allOf(withId(tableId),
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0)),
                        tableRow)).perform(scrollTo(), click());

        Espresso.closeSoftKeyboard();

        getViewInteraction(buttonId, "Remove", buttonNumber, 1, "android.widget.LinearLayout")
                .perform(click());
    }

    protected void addDevice(String deviceName, String weightStep, String startWeight, int maxWeightPlus, int persMaxWeightPlus, int endurancePlus, String difficulty, boolean ignorePercentage) {

        getViewInteraction(R.id.fab, 2, 0, R.id.drawer_layout)
                .perform(click());

        getViewInteraction(R.id.add_device_et_name, 1, 0, "android.widget.TableLayout")
                .perform(replaceText(deviceName), closeSoftKeyboard());

        if (weightStep != null) {
            getViewInteraction(R.id.add_device_et_weightstep, 1, 1, "android.widget.TableLayout")
                    .perform(replaceText(weightStep), closeSoftKeyboard());
        }

        if (startWeight != null) {
            getViewInteraction(R.id.add_device_et_startweight, 1, 2, "android.widget.TableLayout")
                    .perform(replaceText(startWeight), closeSoftKeyboard());
        }

        for (int i = 0; i < maxWeightPlus; i++) {
            getViewInteraction(R.id.add_device_btn_maxweight_plus, "+", 2, 1, "android.widget.TableRow")
                    .perform(click());
        }

        for (int i = 0; i < persMaxWeightPlus; i++) {
            getViewInteraction(R.id.add_device_btn_persmaxweight_plus, "+", 2, 1, "android.widget.TableRow")
                    .perform(click());
        }

        if (difficulty != null) {
            getViewInteraction(R.id.add_device_et_difficulty, 1, 5, "android.widget.TableLayout")
                    .perform(replaceText(difficulty), closeSoftKeyboard());
        }

        for (int i = 0; i < endurancePlus; i++) {
            getViewInteraction(R.id.add_device_btn_endurance_plus, "+", 2, 1, "android.widget.TableRow")
                    .perform(click());
        }

        if (ignorePercentage) {
            getViewInteraction(R.id.add_device_sw_ignore_perc, 1, 6, "android.widget.TableLayout")
                    .perform(click());
        }

        Espresso.closeSoftKeyboard();

        getViewInteraction(R.id.add_device_btn, "Add", 0, 1, "android.widget.LinearLayout")
                .perform(click());
    }

    protected void addExercise(String exerciseName, int repetitions, int rounds, int enduranceRepetitions, int enduranceRounds, String time) {
        getViewInteraction(R.id.fab, 2, 0, R.id.drawer_layout)
                .perform(click());

        getViewInteraction(R.id.add_exercise_ac_name, 1, 0, "android.widget.TableLayout")
                .perform(typeText(exerciseName), closeSoftKeyboard());

        clickAutoComplete(0, exerciseName);

        for (int i = 0; i < repetitions; i++) {
            getViewInteraction(R.id.add_exercise_btn_reps_plus, "+", 2, 1, "android.widget.TableRow")
                    .perform(click());
        }

        for (int i = 0; i < rounds; i++) {
            getViewInteraction(R.id.add_exercise_btn_rounds_plus, "+", 2, 1, "android.widget.TableRow")
                    .perform(click());
        }

        for (int i = 0; i < enduranceRepetitions; i++) {
            getViewInteraction(R.id.add_exercise_btn_end_reps_plus, "+", 2, 1, "android.widget.TableRow")
                    .perform(click());
        }

        for (int i = 0; i < enduranceRounds; i++) {
            getViewInteraction(R.id.add_exercise_btn_end_rounds_plus, "+", 2, 1, "android.widget.TableRow")
                    .perform(click());
        }

        if (time != null) {
            getViewInteraction(R.id.add_exercise_et_time, 1, 5, "android.widget.TableLayout")
                    .perform(replaceText(time), closeSoftKeyboard());
        }

        Espresso.closeSoftKeyboard();

        getViewInteraction(R.id.add_exercise_btn, "Add", 0, 1, "android.widget.LinearLayout")
                .perform(click());
    }

    protected void addWorkout(String workoutName, List<String> deviceName, String percentage, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday, int order, boolean endurance) {
        getViewInteraction(R.id.fab, 2, 0, R.id.drawer_layout)
                .perform(click());

        getViewInteraction(R.id.add_workout_et_name, 1, 0, "android.widget.TableLayout")
                .perform(replaceText(workoutName), closeSoftKeyboard());

        for (String device : deviceName) {
            getViewInteraction(R.id.add_workout_ac_device, 1, 1, "android.widget.TableLayout")
                    .perform(typeText(device), closeSoftKeyboard());

            clickAutoComplete(0, device);
        }

        if (percentage != null) {
            getViewInteraction(R.id.add_workout_perc, 1, 3, "android.widget.TableLayout")
                    .perform(replaceText(percentage), closeSoftKeyboard());
        }
        if (monday) {
            getViewInteraction(R.id.add_workout_ctv_day1, "Monday", 0, 1, "android.widget.TableRow")
                    .perform(click());
        }
        if (tuesday) {
            getViewInteraction(R.id.add_workout_ctv_day2, "Tuesday", 1, 1, "android.widget.TableRow")
                    .perform(click());
        }
        if (wednesday) {
            getViewInteraction(R.id.add_workout_ctv_day3, "Wednesday", 2, 1, "android.widget.TableRow")
                    .perform(click());
        }
        if (thursday) {
            getViewInteraction(R.id.add_workout_ctv_day4, "Thursday", 3, 1, "android.widget.TableRow")
                    .perform(click());
        }
        if (friday) {
            getViewInteraction(R.id.add_workout_ctv_day5, "Friday", 4, 1, "android.widget.TableRow")
                    .perform(click());
        }
        if (saturday) {
            getViewInteraction(R.id.add_workout_ctv_day6, "Saturday", 5, 1, "android.widget.TableRow")
                    .perform(click());
        }
        if (sunday) {
            getViewInteraction(R.id.add_workout_ctv_day7, "Sunday", 6, 1, "android.widget.TableRow")
                    .perform(click());
        }

        for (int i = 1; i < order; i++) {
            getViewInteraction(R.id.add_workout_btn_order_plus, "+", 2, 1, "android.widget.TableRow")
                    .perform(click());
        }

        if (endurance) {
            getViewInteraction(R.id.add_workout_sw_endurance, 1, 6, "android.widget.TableLayout")
                    .perform(click());
        }

        Espresso.closeSoftKeyboard();

        getViewInteraction(R.id.add_workout_btn, "Add", 0, 1, "android.widget.LinearLayout")
                .perform(click());
    }

}
