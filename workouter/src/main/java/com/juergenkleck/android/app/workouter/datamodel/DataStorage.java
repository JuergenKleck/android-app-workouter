package com.juergenkleck.android.app.workouter.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Android app - Workouter
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public final class DataStorage implements Serializable {

    public final static String TAG_ELEMENT = "storage";

    public transient boolean dataChanged = false;
    public Settings settings;
    public List<Device> devices = new ArrayList<>();
    public List<Exercise> exercises = new ArrayList<>();
    public List<Workout> workouts = new ArrayList<>();
    public transient List<Training> trainings = new ArrayList<>();

    public Long getNextId() {
        Long id = 0L;
        for (Device device : devices) {
            if (device.id > id) {
                id = device.id;
            }
        }
        for (Exercise exercise : exercises) {
            if (exercise.id > id) {
                id = exercise.id;
            }
        }
        for (Workout workout : workouts) {
            if (workout.id > id) {
                id = workout.id;
            }
        }
        for (Training training : trainings) {
            if (training.id > id) {
                id = training.id;
            }
        }
        return ++id;
    }

    @Override
    public String toString() {
        return "{" +
                "\"" + Settings.TAG_ELEMENT + "\":" + settings +
                ",\"" + Device.TAG_ELEMENT + "\":" + Arrays.toString(devices.toArray()) +
                ",\"" + Exercise.TAG_ELEMENT + "\":" + Arrays.toString(exercises.toArray()) +
                ",\"" + Workout.TAG_ELEMENT + "\":" + Arrays.toString(workouts.toArray()) +
                "}";
    }
}
