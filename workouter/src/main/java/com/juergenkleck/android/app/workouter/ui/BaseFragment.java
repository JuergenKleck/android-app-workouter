package com.juergenkleck.android.app.workouter.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.juergenkleck.android.app.workouter.R;
import com.juergenkleck.android.app.workouter.business.DataIntegrator;
import com.juergenkleck.android.app.workouter.business.Utility;
import com.juergenkleck.android.app.workouter.datamodel.Device;
import com.juergenkleck.android.app.workouter.datamodel.Exercise;
import com.juergenkleck.android.app.workouter.datamodel.Workout;
import com.juergenkleck.android.appengine.PermissionHelper;
import com.juergenkleck.android.appengine.screens.IPermissionHandler;

/**
 * Android app - Workouter
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public abstract class BaseFragment extends Fragment implements IPermissionHandler {

    protected View root;
    protected PermissionHelper permissionHelper = new PermissionHelper();

    public abstract int getViewLayout();

    public abstract void onFragmentCreateView(@NonNull LayoutInflater inflater,
                                              ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (getViewLayout() > 0) {
            root = inflater.inflate(getViewLayout(), container, false);
        }
        onFragmentCreateView(inflater, container, savedInstanceState);
        return root;
    }

    @Override
    public void onDestroyView() {
        if (DataIntegrator.localAppStorage.dataChanged)
            DataIntegrator.writeData(getContext(), DataIntegrator.localAppStorage, null);
        super.onDestroyView();
    }

    @Override
    public void onPermissionResult(String s, boolean b) {
    }

    public boolean checkPermission(String permission, Boolean alwaysAsk) {
        return permissionHelper.checkPermission(getContext(), getActivity(), permission, alwaysAsk);
    }

    public void toggleFAB(View superLayout, boolean show) {
        superLayout.findViewById(R.id.fab).setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    protected String getNonNull(Object object) {
        if (object != null) {
            return object.toString();
        }
        return "";
    }

    protected Device getDeviceForExercise(Long id) {
        for (Device tmp : DataIntegrator.localAppStorage.devices) {
            if (tmp.id.longValue() == id.longValue()) {
                return tmp;
            }
        }
        return null;
    }

    protected Device getDeviceForExercise(String name) {
        for (Device tmp : DataIntegrator.localAppStorage.devices) {
            if (tmp.name.equals(name)) {
                return tmp;
            }
        }
        return null;
    }

    protected Exercise getExerciseForDeviceName(String name) {
        Device device = getDeviceForExercise(name);
        if (device != null) {
            for (Exercise exercise : DataIntegrator.localAppStorage.exercises) {
                if (exercise.device.id.equals(device.id)) {
                    return exercise;
                }
            }
        }
        return null;
    }

    protected int getWeekdayText(int weekday) {
        switch (weekday) {
            case 1:
                return R.string.weekday_1;
            case 2:
                return R.string.weekday_2;
            case 3:
                return R.string.weekday_3;
            case 4:
                return R.string.weekday_4;
            case 5:
                return R.string.weekday_5;
            case 6:
                return R.string.weekday_6;
            case 7:
                return R.string.weekday_7;
        }
        return R.string.unknown;
    }


    protected float calculateWeight(Workout workout, Device tmpDevice, float mWeight, int day) {
        float weight = mWeight;
        if (tmpDevice.personalMaxWeight > 0.0f) {
            if (!workout.endurance && !tmpDevice.ignorePercent && day > 0) {
                // the calculated raw percentage

                float persMaxWeightPerc = 0.0f;
                float[] tmpPercs = Utility.stringToFloat(workout.maxWeightPercentage);
                int[] weekdays = Utility.stringToInt(workout.weekday);
                for (int i = 0; i < weekdays.length; i++) {
                    if (weekdays[i] == day) {
                        persMaxWeightPerc = tmpPercs[i];
                        break;
                    }
                }

                weight = tmpDevice.personalMaxWeight * persMaxWeightPerc / 100;
                if (tmpDevice.startWeight > 0.0f) {
                    // need to get the matching weight bracket
                    float start = 0.0f, end = 0.0f;
                    for (float i = tmpDevice.startWeight; i <= tmpDevice.personalMaxWeight; i += tmpDevice.weightSteps) {
                        if (start == 0.0f && i + tmpDevice.weightSteps > weight) start = i;
                        if (end == 0.0f && i >= weight) end = i;
                    }
                    float modStart = (weight - start) % tmpDevice.weightSteps;
                    float modEnd = (end - weight) % tmpDevice.weightSteps;
                    // calculate the lower difference and use this
                    weight = modStart < modEnd ? start : end;

                } else {
                    // plain calculate modulus here
                    float modulus = (weight % tmpDevice.weightSteps);
                    if (modulus != 0) {
                        if (tmpDevice.weightSteps / 2 > modulus) {
                            weight -= modulus;
                        } else {
                            weight = weight - modulus + tmpDevice.weightSteps;
                        }
                    }
                }
            } else if (workout.endurance) {
                weight = tmpDevice.enduranceWeight;
            } else {
                weight = tmpDevice.personalMaxWeight;
            }
        }
        return weight;
    }


}
