package com.juergenkleck.android.app.workouter.ui.devices;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.juergenkleck.android.app.workouter.R;
import com.juergenkleck.android.app.workouter.business.DataIntegrator;
import com.juergenkleck.android.app.workouter.datamodel.Device;
import com.juergenkleck.android.app.workouter.ui.BaseFragment;
import com.juergenkleck.android.app.workouter.ui.Constants;

/**
 * Android app - Workouter
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class DeviceFragment extends BaseFragment {

    private Dialog addDialog;

    private Device currentObject;

    private int textColor = 0;
    private float fontSize = 24f;

    @Override
    public int getViewLayout() {
        return R.layout.fragment_devices;
    }

    @Override
    public void onFragmentCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        textColor = Color.rgb(DataIntegrator.localAppStorage.settings.cTextR, DataIntegrator.localAppStorage.settings.cTextG, DataIntegrator.localAppStorage.settings.cTextB);
        fontSize = Integer.valueOf(DataIntegrator.localAppStorage.settings.textSize).floatValue();

        FloatingActionButton fab = viewGroup.getRootView().findViewById(R.id.fab);
        toggleFAB(fab, true);
        fab.setOnClickListener(view -> {
            populateDialogValues("", "", "", "", "", "", "", false);
            currentObject = new Device();
            currentObject.id = DataIntegrator.localAppStorage.getNextId();
            currentObject.isNew = true;
            addDialog.show();
        });

        updateLists(layoutInflater);

        root.setBackgroundColor(Color.rgb(DataIntegrator.localAppStorage.settings.cBackR, DataIntegrator.localAppStorage.settings.cBackG, DataIntegrator.localAppStorage.settings.cBackB));

        addDialog = new Dialog(this.getContext(), R.style.Theme_AppCompat_Light_Dialog);
        addDialog.setContentView(R.layout.dialog_add_device);

        addDialog.findViewById(R.id.add_device_btn).setOnClickListener(onEditDialogOk);
        addDialog.findViewById(R.id.remove_device_btn).setOnClickListener(onEditDialogRemove);

        addDialog.findViewById(R.id.add_device_btn_maxweight_plus).setOnClickListener(onBtnMaxWeightPlus);
        addDialog.findViewById(R.id.add_device_btn_maxweight_minus).setOnClickListener(onBtnMaxWeightMinus);
        addDialog.findViewById(R.id.add_device_btn_persmaxweight_plus).setOnClickListener(onBtnPersMaxWeightPlus);
        addDialog.findViewById(R.id.add_device_btn_persmaxweight_minus).setOnClickListener(onBtnPersMaxWeightMinus);
        addDialog.findViewById(R.id.add_device_btn_endurance_plus).setOnClickListener(onBtnEnduranceWeightPlus);
        addDialog.findViewById(R.id.add_device_btn_endurance_minus).setOnClickListener(onBtnEnduranceWeightMinus);
    }

    private View.OnClickListener rowClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            String name = ((TextView) view.findViewById(R.id.tbl_row_tv2_1)).getText().toString();
            for (Device device : DataIntegrator.localAppStorage.devices) {
                if (device.name.equals(name)) {
                    currentObject = device;
                    break;
                }
            }
            currentObject.isNew = false;
            populateDialogValues(Float.toString(currentObject.difficulty), Float.toString(currentObject.startWeight), Float.toString(currentObject.weightSteps), currentObject.name, Float.toString(currentObject.personalMaxWeight), Float.toString(currentObject.maxWeight), Float.toString(currentObject.enduranceWeight), currentObject.ignorePercent);
            addDialog.show();
        }
    };

    private void populateDialogValues(String difficulty, String startWeight, String weightStep, String name, String persMaxWeight, String maxWeight, String enduranceWeight, boolean ignorePercentage) {
        ((EditText) addDialog.findViewById(R.id.add_device_et_difficulty)).setText(difficulty);
        ((EditText) addDialog.findViewById(R.id.add_device_et_weightstep)).setText(weightStep);
        ((EditText) addDialog.findViewById(R.id.add_device_et_startweight)).setText(startWeight);
        ((EditText) addDialog.findViewById(R.id.add_device_et_name)).setText(name);
        ((TextView) addDialog.findViewById(R.id.add_device_tv_persmaxweight_value)).setText(persMaxWeight);
        ((TextView) addDialog.findViewById(R.id.add_device_tv_maxweight_value)).setText(maxWeight);
        ((TextView) addDialog.findViewById(R.id.add_device_tv_endurance_value)).setText(enduranceWeight);
        ((Switch) addDialog.findViewById(R.id.add_device_sw_ignore_perc)).setChecked(ignorePercentage);

    }

    private void updateLists(@NonNull LayoutInflater inflater) {
        TableLayout tbl = root.findViewById(R.id.devices_listitems);
        tbl.removeAllViews();

        // init header row
        View row = inflater.inflate(R.layout.tbl_row_4tv, tbl, false);
        tbl.addView(row);
        row.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_LIGHT_BLUE));

        TextView tv = row.findViewById(R.id.tbl_row_tv2_1);
        tv.setText(getText(R.string.text_name));
        tv.setTextSize(fontSize);
        tv.setTextColor(textColor);

        tv = row.findViewById(R.id.tbl_row_tv2_2);
        tv.setText(getText(R.string.text_max));
        tv.setTextSize(fontSize);
        tv.setWidth(205);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(textColor);

        tv = row.findViewById(R.id.tbl_row_tv2_3);
        tv.setText(getText(R.string.text_pers));
        tv.setTextSize(fontSize);
        tv.setWidth(205);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(textColor);


        tv = row.findViewById(R.id.tbl_row_tv2_4);
        tv.setText(getText(R.string.text_end));
        tv.setTextSize(fontSize);
        tv.setWidth(205);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(textColor);

        for (Device device : DataIntegrator.localAppStorage.devices) {
            row = inflater.inflate(R.layout.tbl_row_4tv, tbl, false);
            row.setOnClickListener(rowClickListener);
            tbl.addView(row);

            tv = row.findViewById(R.id.tbl_row_tv2_1);
            tv.setText(device.name);
            tv.setTextSize(fontSize);
            tv.setTextColor(textColor);

            if (device.difficulty > 0) {
                tv = row.findViewById(R.id.tbl_row_tv2_2);
                tv.setText(Float.toString(device.difficulty));
                tv.setTextSize(fontSize);
                tv.setWidth(245);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(textColor);
                tv.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_GRAY));

                tv = row.findViewById(R.id.tbl_row_tv2_3);
                tv.setText("");
            } else {
                tv = row.findViewById(R.id.tbl_row_tv2_2);
                tv.setText(Float.toString(device.maxWeight));
                tv.setTextSize(fontSize);
                tv.setWidth(245);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(textColor);
                tv.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_GRAY));

                tv = row.findViewById(R.id.tbl_row_tv2_3);
                tv.setText(Float.toString(device.personalMaxWeight));
                tv.setTextSize(fontSize);
                tv.setWidth(245);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(textColor);
                if (device.personalMaxWeight == device.maxWeight)
                    tv.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_RED));
                else tv.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_GREEN));

                tv = row.findViewById(R.id.tbl_row_tv2_4);
                tv.setText(Float.toString(device.enduranceWeight));
                tv.setTextSize(fontSize);
                tv.setWidth(245);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(textColor);
                if (device.enduranceWeight == device.maxWeight)
                    tv.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_RED));
                else tv.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_GREEN));
            }
        }
    }

    private String getEditTextValue(int id) {
        return ((EditText) addDialog.findViewById(id)).getText().toString();
    }

    private void updateWeightSteps() {
        String text = getEditTextValue(R.id.add_device_et_weightstep);
        if (text.length() > 0) {
            currentObject.weightSteps = Float.parseFloat(text);
        } else {
            currentObject.weightSteps = 0f;
        }
        text = getEditTextValue(R.id.add_device_et_startweight);
        if (text.length() > 0) {
            currentObject.startWeight = Float.parseFloat(text);
        } else {
            currentObject.startWeight = 0f;
        }
    }

    private View.OnClickListener onBtnMaxWeightPlus = new View.OnClickListener() {
        public void onClick(View v) {
            updateWeightSteps();
            if (currentObject.maxWeight < currentObject.startWeight) {
                currentObject.maxWeight = currentObject.startWeight;
            }
            currentObject.maxWeight += currentObject.weightSteps;
            if (currentObject.maxWeight > 999f) {
                currentObject.maxWeight -= currentObject.weightSteps;
            }
            ((TextView) addDialog.findViewById(R.id.add_device_tv_maxweight_value)).setText(Float.toString(currentObject.maxWeight));
        }
    };

    private View.OnClickListener onBtnMaxWeightMinus = new View.OnClickListener() {
        public void onClick(View v) {
            updateWeightSteps();
            currentObject.maxWeight -= currentObject.weightSteps;
            if (currentObject.maxWeight < currentObject.startWeight) {
                currentObject.maxWeight = currentObject.startWeight;
            }
            if (currentObject.maxWeight < 0f) {
                currentObject.maxWeight = 0f;
            }
            if (currentObject.maxWeight < currentObject.personalMaxWeight) {
                currentObject.personalMaxWeight = currentObject.maxWeight;
                ((TextView) addDialog.findViewById(R.id.add_device_tv_persmaxweight_value)).setText(Float.toString(currentObject.personalMaxWeight));
            }
            ((TextView) addDialog.findViewById(R.id.add_device_tv_maxweight_value)).setText(Float.toString(currentObject.maxWeight));
        }
    };

    private View.OnClickListener onBtnPersMaxWeightPlus = new View.OnClickListener() {
        public void onClick(View v) {
            updateWeightSteps();
            if (currentObject.personalMaxWeight < currentObject.startWeight) {
                currentObject.personalMaxWeight = currentObject.startWeight;
            }
            currentObject.personalMaxWeight += currentObject.weightSteps;
            if (currentObject.personalMaxWeight > 999f || currentObject.personalMaxWeight > currentObject.maxWeight) {
                currentObject.personalMaxWeight -= currentObject.weightSteps;
            }
            ((TextView) addDialog.findViewById(R.id.add_device_tv_persmaxweight_value)).setText(Float.toString(currentObject.personalMaxWeight));
        }
    };

    private View.OnClickListener onBtnPersMaxWeightMinus = new View.OnClickListener() {
        public void onClick(View v) {
            updateWeightSteps();
            currentObject.personalMaxWeight -= currentObject.weightSteps;
            if (currentObject.personalMaxWeight < currentObject.startWeight) {
                currentObject.personalMaxWeight = currentObject.startWeight;
            }
            if (currentObject.personalMaxWeight < 0f) {
                currentObject.personalMaxWeight = 0f;
            }
            ((TextView) addDialog.findViewById(R.id.add_device_tv_persmaxweight_value)).setText(Float.toString(currentObject.personalMaxWeight));
        }
    };

    private View.OnClickListener onBtnEnduranceWeightPlus = new View.OnClickListener() {
        public void onClick(View v) {
            updateWeightSteps();
            if (currentObject.enduranceWeight < currentObject.startWeight) {
                currentObject.enduranceWeight = currentObject.startWeight;
            }
            currentObject.enduranceWeight += currentObject.weightSteps;
            if (currentObject.enduranceWeight > 999f || currentObject.enduranceWeight > currentObject.maxWeight) {
                currentObject.enduranceWeight -= currentObject.weightSteps;
            }
            ((TextView) addDialog.findViewById(R.id.add_device_tv_endurance_value)).setText(Float.toString(currentObject.enduranceWeight));
        }
    };

    private View.OnClickListener onBtnEnduranceWeightMinus = new View.OnClickListener() {
        public void onClick(View v) {
            updateWeightSteps();
            currentObject.enduranceWeight -= currentObject.weightSteps;
            if (currentObject.enduranceWeight < currentObject.startWeight) {
                currentObject.enduranceWeight = currentObject.startWeight;
            }
            if (currentObject.enduranceWeight < 0f) {
                currentObject.enduranceWeight = 0f;
            }
            ((TextView) addDialog.findViewById(R.id.add_device_tv_endurance_value)).setText(Float.toString(currentObject.enduranceWeight));
        }
    };

    private View.OnClickListener onEditDialogOk = new View.OnClickListener() {
        public void onClick(View v) {

            currentObject.name = getEditTextValue(R.id.add_device_et_name);

            String weightSteps = getEditTextValue(R.id.add_device_et_weightstep);
            if (!weightSteps.isEmpty())
                currentObject.weightSteps = Float.parseFloat(weightSteps);

            String startWeight = getEditTextValue(R.id.add_device_et_startweight);
            if (!startWeight.isEmpty())
                currentObject.startWeight = Float.parseFloat(startWeight);

            String difficulty = getEditTextValue(R.id.add_device_et_difficulty);
            if (!difficulty.isEmpty())
                currentObject.difficulty = Float.parseFloat(difficulty);

            currentObject.ignorePercent = ((Switch) addDialog.findViewById(R.id.add_device_sw_ignore_perc)).isChecked();

            if (currentObject.name != null && !currentObject.name.isEmpty()) {
                if (currentObject.isNew)
                    DataIntegrator.localAppStorage.devices.add(currentObject);

                DataIntegrator.writeData(getContext(), DataIntegrator.localAppStorage, null);

                updateLists(getLayoutInflater());
                currentObject.isNew = false;
                currentObject = null;
            }

            addDialog.dismiss();
        }
    };

    private View.OnClickListener onEditDialogRemove = new View.OnClickListener() {
        public void onClick(View v) {
            // remove from workouts
            DataIntegrator.localAppStorage.workouts.forEach(workout -> workout.exercises.removeIf(exercise -> exercise.device.equals(currentObject)));
            // remove exercises
            DataIntegrator.localAppStorage.exercises.removeIf(exercise -> exercise.device.equals(currentObject));
            DataIntegrator.localAppStorage.devices.remove(currentObject);
            DataIntegrator.writeData(getContext(), DataIntegrator.localAppStorage, null);
            updateLists(getLayoutInflater());
            currentObject = null;
            addDialog.dismiss();
        }
    };

}