package email.kleck.android.workouter;


import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddWorkoutUITest extends BaseUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void basicTrainingUITest() {
        navigateTo(6);

        addDevice("Arms", "25", "20", 2, 1, 1, null, false);
        addDevice("Cross trainer", null, null, 0, 0, 0, "10", false);
        addDevice("Incline", "15", "30", 4, 3, 2, null, false);
        addDevice("Leg press", "10", "20", 8, 5, 2, null, true);

        navigateTo(5);

        addExercise("Arms", 5, 5, 0, 0, null);
        addExercise("Leg press", 5, 5, 0, 0, null);
        addExercise("Incline", 5, 5, 3, 3, null);
        addExercise("Cross trainer", 0, 0, 0, 0, "25");

        navigateTo(4);

        addWorkout("Arm Training", Arrays.asList("Arms", "Incline"), "50", true, true, true, true, true, true, true, 1, false);
        addWorkout("Endurance Training", Arrays.asList("Incline"), "30", true, true, true, true, true, true, true, 2, true);
        addWorkout("Cardio", Arrays.asList("Cross trainer"), null, true, true, true, true, true, true, true, 3, false);

        navigateTo(1);
        navigateTo(4);

        removeTableItem(R.id.workout_listitems, 1, R.id.remove_workout_btn, 2);
        removeTableItem(R.id.workout_listitems, 1, R.id.remove_workout_btn, 2);
        removeTableItem(R.id.workout_listitems, 1, R.id.remove_workout_btn, 2);

        navigateTo(5);

        removeTableItem(R.id.exercises_listitems, 1, R.id.remove_exercise_btn, 1);
        removeTableItem(R.id.exercises_listitems, 1, R.id.remove_exercise_btn, 1);
        removeTableItem(R.id.exercises_listitems, 1, R.id.remove_exercise_btn, 1);
        removeTableItem(R.id.exercises_listitems, 1, R.id.remove_exercise_btn, 1);

        navigateTo(6);

        removeTableItem(R.id.devices_listitems, 1, R.id.remove_device_btn, 1);
        removeTableItem(R.id.devices_listitems, 1, R.id.remove_device_btn, 1);
        removeTableItem(R.id.devices_listitems, 1, R.id.remove_device_btn, 1);
        removeTableItem(R.id.devices_listitems, 1, R.id.remove_device_btn, 1);

    }

    @Test
    public void crossRemoveUITest() {
        navigateTo(6);

        addDevice("Arms", "25", "20", 2, 1, 1, null, false);
        addDevice("Cross trainer", null, null, 0, 0, 0, "10", false);
        addDevice("Incline", "15", "30", 4, 3, 2, null, false);
        addDevice("Leg press", "10", "20", 8, 5, 2, null, true);

        navigateTo(5);

        addExercise("Arms", 5, 5, 0, 0, null);
        addExercise("Leg press", 5, 5, 0, 0, null);
        addExercise("Incline", 5, 5, 3, 3, null);
        addExercise("Cross trainer", 0, 0, 0, 0, "25");

        navigateTo(4);

        addWorkout("Arm Training", Arrays.asList("Arms", "Incline"), "50", true, true, true, true, true, true, true, 1, false);
        addWorkout("Endurance Training", Arrays.asList("Incline"), "30", true, true, true, true, true, true, true, 2, true);
        addWorkout("Cardio", Arrays.asList("Cross trainer"), null, true, true, true, true, true, true, true, 3, false);

        navigateTo(6);

        removeTableItem(R.id.devices_listitems, 1, R.id.remove_device_btn, 1);
        removeTableItem(R.id.devices_listitems, 1, R.id.remove_device_btn, 1);
        removeTableItem(R.id.devices_listitems, 1, R.id.remove_device_btn, 1);
        removeTableItem(R.id.devices_listitems, 1, R.id.remove_device_btn, 1);

        navigateTo(4);

        removeTableItem(R.id.workout_listitems, 1, R.id.remove_workout_btn, 2);
        removeTableItem(R.id.workout_listitems, 1, R.id.remove_workout_btn, 2);
        removeTableItem(R.id.workout_listitems, 1, R.id.remove_workout_btn, 2);

        navigateTo(5);

    }

}
