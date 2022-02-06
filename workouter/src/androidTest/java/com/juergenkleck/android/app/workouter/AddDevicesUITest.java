package com.juergenkleck.android.app.workouter;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddDevicesUITest extends BaseUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addDevices() {
        navigateTo(6);

        addDevice("Cross trainer", null, null, 0, 0, 0, "10", false);
        addDevice("Incline", "5", "0", 6, 4, 2, null, false);
        addDevice("Leg press", "10", "20", 8, 5, 2, null, true);


        removeTableItem(R.id.devices_listitems, 3, R.id.remove_device_btn, 1);
        removeTableItem(R.id.devices_listitems, 2, R.id.remove_device_btn, 1);
        removeTableItem(R.id.devices_listitems, 1, R.id.remove_device_btn, 1);
    }

}
