package com.juergenkleck.android.app.workouter.ui.training;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import com.juergenkleck.android.app.workouter.R;
import com.juergenkleck.android.app.workouter.business.DataIntegrator;
import com.juergenkleck.android.app.workouter.business.Utility;
import com.juergenkleck.android.app.workouter.datamodel.Device;
import com.juergenkleck.android.app.workouter.datamodel.Workout;
import com.juergenkleck.android.app.workouter.ui.BaseFragment;
import com.juergenkleck.android.app.workouter.ui.Constants;

/**
 * Android app - Workouter
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class TrainingFragment extends BaseFragment {

    private int textColor = 0;
    private float fontSize = 24f;

    @Override
    public int getViewLayout() {
        return R.layout.fragment_training;
    }

    @Override
    public void onFragmentCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        textColor = Color.rgb(DataIntegrator.localAppStorage.settings.cTextR, DataIntegrator.localAppStorage.settings.cTextG, DataIntegrator.localAppStorage.settings.cTextB);
        fontSize = Integer.valueOf(DataIntegrator.localAppStorage.settings.textSize).floatValue();

        FloatingActionButton fab = viewGroup.getRootView().findViewById(R.id.fab);
        toggleFAB(fab, false);

        updateLists(layoutInflater);

        root.setBackgroundColor(Color.rgb(DataIntegrator.localAppStorage.settings.cBackR, DataIntegrator.localAppStorage.settings.cBackG, DataIntegrator.localAppStorage.settings.cBackB));
    }

    private boolean isWeekdayInDay(String weekday, int day) {
        int[] days = Utility.stringToInt(weekday);
        for (int value : days) {
            if (value == day) {
                return true;
            }
        }
        return false;
    }

    private void updateLists(@NonNull LayoutInflater inflater) {
        TableLayout tbl = root.findViewById(R.id.training_listitems);
        tbl.removeAllViews();

        // init header row
        View row = inflater.inflate(R.layout.tbl_row_3tv, tbl, false);
        tbl.addView(row);
        row.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_LIGHT_BLUE));

        TextView tv = row.findViewById(R.id.tbl_row_tv_1);
        tv.setText(getText(R.string.text_devices));
        tv.setTextSize(fontSize);
        tv.setTextColor(textColor);

        tv = row.findViewById(R.id.tbl_row_tv_2);
        tv.setText(getText(R.string.text_weight_time));
        tv.setTextSize(fontSize);
        tv.setWidth(275);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(textColor);

        tv = row.findViewById(R.id.tbl_row_tv_3);
        tv.setText(getText(R.string.text_reps_rounds));
        tv.setTextSize(fontSize);
        tv.setWidth(245);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(textColor);

        // mo = 1, su = 7
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        day -= 1;

        if (day == 0) {
            // switch sunday to correct index
            day = 7;
        }

        DataIntegrator.localAppStorage.workouts.sort((w1, w2) -> Integer.compare(w1.order, w2.order));

        for (Workout workout : DataIntegrator.localAppStorage.workouts) {
            if (!isWeekdayInDay(workout.weekday, day)) continue;

            row = inflater.inflate(R.layout.tbl_row_3tv, tbl, false);
            tbl.addView(row);


            tv = row.findViewById(R.id.tbl_row_tv_1);
            ((TableRow.LayoutParams) tv.getLayoutParams()).span = 3;
            tv.setText(workout.name + (workout.endurance ? " (Endurance)" : ""));
            tv.setTextSize(fontSize);
            tv.setTextColor(textColor);
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_YELLOW));

            for (int i = 0; i < workout.exercises.size(); i++) {
                if (workout.exercises.get(i) != null) {
                    Device tmpDevice = getDeviceForExercise(workout.exercises.get(i).device.id);

                    StringBuilder names = new StringBuilder();
                    String repsRounds = "";
                    if (workout.exercises.get(i).time > 0) {
                        names.append(workout.exercises.get(i).time);
                        names.append(" min");
                    } else {
                        // if no personal weight we just keep the max weight
                        float weight = calculateWeight(workout, tmpDevice, tmpDevice.maxWeight, day);
                        names.append(weight);
                        if (DataIntegrator.localAppStorage.settings.kg) {
                            names.append(" kg");
                        } else {
                            names.append(" lb");
                        }
                        if (workout.endurance) {
                            repsRounds = workout.exercises.get(i).enduranceRepetitions + " / " + workout.exercises.get(i).enduranceRounds;
                        } else {
                            repsRounds = workout.exercises.get(i).repetitions + " / " + workout.exercises.get(i).rounds;
                        }
                    }

                    row = inflater.inflate(R.layout.tbl_row_3tv, tbl, false);
                    tbl.addView(row);

                    tv = row.findViewById(R.id.tbl_row_tv_1);
                    tv.setText(tmpDevice.name);
                    tv.setTextSize(fontSize);
                    tv.setTextColor(textColor);

                    tv = row.findViewById(R.id.tbl_row_tv_2);
                    tv.setText(names.toString());
                    tv.setTextSize(fontSize);
                    tv.setWidth(275);
                    tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tv.setTextColor(textColor);
                    tv.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_GRAY));

                    tv = row.findViewById(R.id.tbl_row_tv_3);
                    tv.setText(repsRounds);
                    tv.setTextSize(fontSize);
                    tv.setWidth(245);
                    tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tv.setTextColor(textColor);
                    if (!repsRounds.isEmpty())
                        tv.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_GREEN));
                }
            }
        }
    }
}