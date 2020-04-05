package email.kleck.android.workouter;


import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddExerciseUITest extends BaseUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addExerciseUITest() {
        navigateTo(6);

        addDevice("Legs", "25", "20", 2, 1, 1, null, false);
        addDevice("Cross trainer", null, null, 0, 0, 0, "10", false);

        navigateTo(5);

        addExercise("Legs", 5, 5, 0, 0, null);
        addExercise("Cross trainer", 0, 0, 0, 0, "25");

        removeTableItem(R.id.exercises_listitems, 1, R.id.remove_exercise_btn, 1);
        removeTableItem(R.id.exercises_listitems, 1, R.id.remove_exercise_btn, 1);

        navigateTo(6);

        removeTableItem(R.id.devices_listitems, 1, R.id.remove_device_btn, 1);
        removeTableItem(R.id.devices_listitems, 1, R.id.remove_device_btn, 1);
    }


}
