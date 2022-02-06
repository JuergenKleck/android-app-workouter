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
public class Training extends BaseItem implements Serializable {

    public final static String TAG_ELEMENT = "training";

    public List<Workout> workouts = new ArrayList<>();

    @Override
    public String toString() {
        return "{" +
                "\"workout\":" + Arrays.toString(workouts.stream().map(w -> w.id).toArray()) +
                ",\"" + TAG_ID + "\":" + id +
                '}';
    }
}
