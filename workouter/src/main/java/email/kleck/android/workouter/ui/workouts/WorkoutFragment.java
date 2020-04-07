package email.kleck.android.workouter.ui.workouts;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import email.kleck.android.workouter.R;
import email.kleck.android.workouter.business.DataIntegrator;
import email.kleck.android.workouter.business.Utility;
import email.kleck.android.workouter.datamodel.Device;
import email.kleck.android.workouter.datamodel.Exercise;
import email.kleck.android.workouter.datamodel.Workout;
import email.kleck.android.workouter.ui.BaseFragment;
import email.kleck.android.workouter.ui.Constants;
import email.kleck.android.workouter.ui.CustomExerciseAdapter;

public class WorkoutFragment extends BaseFragment {

    private Dialog addDialog;

    private Workout currentObject;

    private CustomExerciseAdapter selectedExercises;
    private AutoCompleteTextView autoCmpltTv;

    private int textColor = 0;
    private float fontSize = 24f;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_workouts, container, false);

        textColor = Color.rgb(DataIntegrator.localAppStorage.settings.cTextR, DataIntegrator.localAppStorage.settings.cTextG, DataIntegrator.localAppStorage.settings.cTextB);
        fontSize = Integer.valueOf(DataIntegrator.localAppStorage.settings.textSize).floatValue();

        FloatingActionButton fab = container.getRootView().findViewById(R.id.fab);
        toggleFAB(fab, true);
        fab.setOnClickListener(view -> {
            populateDialogValues("", "", null, false, "1");
            currentObject = new Workout();
            currentObject.id = DataIntegrator.localAppStorage.getNextId();
            currentObject.isNew = true;
            currentObject.maxWeightPercentages = new float[0];
            currentObject.weekdays = new int[0];
            currentObject.order = 1;
            selectedExercises.clear();
            addDialog.show();
        });

        updateLists(inflater);

        root.setBackgroundColor(Color.rgb(DataIntegrator.localAppStorage.settings.cBackR, DataIntegrator.localAppStorage.settings.cBackG, DataIntegrator.localAppStorage.settings.cBackB));

        addDialog = new Dialog(this.getContext(), R.style.Theme_AppCompat_Light_Dialog);
        addDialog.setContentView(R.layout.dialog_add_workout);
        addDialog.findViewById(R.id.add_workout_btn).setOnClickListener(onEditDialogOk);
        addDialog.findViewById(R.id.remove_workout_btn).setOnClickListener(onEditDialogRemove);
        addDialog.findViewById(R.id.copy_workout_btn).setOnClickListener(onEditDialogCopy);
        addDialog.findViewById(R.id.add_workout_btn_order_plus).setOnClickListener(onBtnOrderPlus);
        addDialog.findViewById(R.id.add_workout_btn_order_minus).setOnClickListener(onBtnOrderMinus);

        autoCmpltTv = addDialog.findViewById(R.id.add_workout_ac_device);
        CustomExerciseAdapter adapter = new CustomExerciseAdapter(getContext(), new ArrayList<>(DataIntegrator.localAppStorage.exercises));
        autoCmpltTv.setAdapter(adapter);
        autoCmpltTv.setText("");
        autoCmpltTv.setOnItemClickListener(onAutoCompleteClick);

        ListView deviceList = addDialog.findViewById(R.id.workout_device_list);
        selectedExercises = new CustomExerciseAdapter(getContext(), new ArrayList<>());
        deviceList.setAdapter(selectedExercises);
        deviceList.setOnItemClickListener(onDeviceListClick);

        return root;
    }

    private AdapterView.OnItemClickListener onDeviceListClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Exercise exercise = (Exercise) adapterView.getAdapter().getItem(i);
            if (exercise != null) {
                currentObject.exercises.remove(exercise);
                selectedExercises.remove(exercise);
            }
        }
    };

    private AdapterView.OnItemClickListener onAutoCompleteClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Exercise exercise = (Exercise) adapterView.getAdapter().getItem(i);
            if (exercise != null) {
                if (!currentObject.exercises.contains(exercise)) {
                    currentObject.exercises.add(exercise);
                    selectedExercises.add(exercise);
                }
                autoCmpltTv.setText("");
            }
        }
    };

    private View.OnClickListener rowClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            String name = ((TextView) view.findViewById(R.id.tbl_row_tv_1)).getText().toString();
            String perc = ((TextView) view.findViewById(R.id.tbl_row_tv_2)).getText().toString();
            String wday = ((TextView) view.findViewById(R.id.tbl_row_tv_3)).getText().toString();

            TableLayout tbl = root.findViewById(R.id.workout_listitems);
            for (int i = 0; i < tbl.getChildCount(); i++) {
                View row = tbl.getChildAt(i);
                TextView tv1 = row.findViewById(R.id.tbl_row_tv_1);
                TextView tv2 = row.findViewById(R.id.tbl_row_tv_2);
                TextView tv3 = row.findViewById(R.id.tbl_row_tv_3);
                if (tv1.getText().toString().equals(name) && tv2.getText().toString().equals(perc) && tv3.getText().toString().equals(wday)) {
                    currentObject = DataIntegrator.localAppStorage.workouts.get(i - 1);
                    break;
                }
            }

            if (currentObject != null) {
                selectedExercises.clear();
                for (Exercise e : currentObject.exercises) {
                    selectedExercises.add(e);
                }
                populateDialogValues(currentObject.name, currentObject.maxWeightPercentage, currentObject.weekday, currentObject.endurance, Integer.toString(currentObject.order));
                addDialog.show();
            }
        }
    };

    private String getEditTextValue(int id) {
        return ((EditText) addDialog.findViewById(id)).getText().toString();
    }

    private void populateDialogValues(String name, String maxWeightPercentage, String weekday, boolean endurance, String order) {
        ((EditText) addDialog.findViewById(R.id.add_workout_et_name)).setText(name);
        ((TextView) addDialog.findViewById(R.id.add_workout_perc)).setText(maxWeightPercentage);
        ((TextView) addDialog.findViewById(R.id.add_workout_tv_order_value)).setText(order);
        ((CheckBox) addDialog.findViewById(R.id.add_workout_ctv_day1)).setChecked(false);
        ((CheckBox) addDialog.findViewById(R.id.add_workout_ctv_day2)).setChecked(false);
        ((CheckBox) addDialog.findViewById(R.id.add_workout_ctv_day3)).setChecked(false);
        ((CheckBox) addDialog.findViewById(R.id.add_workout_ctv_day4)).setChecked(false);
        ((CheckBox) addDialog.findViewById(R.id.add_workout_ctv_day5)).setChecked(false);
        ((CheckBox) addDialog.findViewById(R.id.add_workout_ctv_day6)).setChecked(false);
        ((CheckBox) addDialog.findViewById(R.id.add_workout_ctv_day7)).setChecked(false);
        if (weekday != null) {
            int[] weekdays = Utility.stringToInt(weekday);
            for (int value : weekdays) {
                int id = -1;
                switch (value) {
                    case 1:
                        id = R.id.add_workout_ctv_day1;
                        break;
                    case 2:
                        id = R.id.add_workout_ctv_day2;
                        break;
                    case 3:
                        id = R.id.add_workout_ctv_day3;
                        break;
                    case 4:
                        id = R.id.add_workout_ctv_day4;
                        break;
                    case 5:
                        id = R.id.add_workout_ctv_day5;
                        break;
                    case 6:
                        id = R.id.add_workout_ctv_day6;
                        break;
                    case 7:
                        id = R.id.add_workout_ctv_day7;
                        break;
                }
                if (id > -1) {
                    ((CheckBox) addDialog.findViewById(id)).setChecked(true);
                }
            }
        }
        ((Switch) addDialog.findViewById(R.id.add_workout_sw_endurance)).setChecked(endurance);
    }

    private void updateLists(@NonNull LayoutInflater inflater) {
        TableLayout tbl = root.findViewById(R.id.workout_listitems);
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
        tv.setText(getText(R.string.text_percentage));
        tv.setTextSize(fontSize);
        tv.setWidth(205);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(textColor);

        tv = row.findViewById(R.id.tbl_row_tv_3);
        tv.setText(getText(R.string.text_weekday));
        tv.setTextSize(fontSize);
        tv.setWidth(355);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(textColor);

        DataIntegrator.localAppStorage.workouts.sort((w1, w2) -> w1.weekday.compareTo(w2.weekday));

        for (Workout workout : DataIntegrator.localAppStorage.workouts) {
            row = inflater.inflate(R.layout.tbl_row_3tv, tbl, false);
            row.setOnClickListener(rowClickListener);
            tbl.addView(row);

            int lines = 1;
            StringBuilder names = new StringBuilder();
            names.append(workout.name).append(workout.endurance ? " (End)" : "");
            names.append(":\n");
            for (int i = 0; i < workout.exercises.size(); i++) {
                if (workout.exercises.get(i) != null) {
                    Device tmpDevice = getDeviceForExercise(workout.exercises.get(i).device.id);
                    names.append(tmpDevice.name);
                    names.append(" (");
                    if (workout.exercises.get(i).time > 0) {
                        names.append(workout.exercises.get(i).time);
                        names.append(" min");
                    } else {
                        // if no personal weight we just keep the max weight
                        float weight = calculateWeight(workout, tmpDevice, tmpDevice.maxWeight, -1);

                        names.append(weight);
                        if (DataIntegrator.localAppStorage.settings.kg) {
                            names.append(" kg");
                        } else {
                            names.append(" lb");
                        }
                    }
                    names.append(")");
                    if (i + 1 < workout.exercises.size()) {
                        names.append("\n");
                    }
                    lines++;
                }
            }

            tv = row.findViewById(R.id.tbl_row_tv_1);
            tv.setText(names.toString());
            tv.setMaxLines(lines);
            tv.setTextSize(fontSize);
            tv.setTextColor(textColor);

            tv = row.findViewById(R.id.tbl_row_tv_2);
            tv.setText(workout.maxWeightPercentage.replaceAll(",", "\n"));
            tv.setTextSize(fontSize);
            tv.setWidth(205);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setTextColor(textColor);
            tv.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_GRAY));

            tv = row.findViewById(R.id.tbl_row_tv_3);
            tv.setText(getWeekdays(workout.weekday).replaceAll(",", "\n"));
            tv.setTextSize(fontSize);
            tv.setWidth(355);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setTextColor(textColor);
            tv.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_GREEN));
        }
    }

    private String getWeekdays(String weekdays) {
        int[] days = Utility.stringToInt(weekdays);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < days.length; i++) {
            sb.append(getText(getWeekdayText(days[i])));
            if (i + 1 < days.length) sb.append(", ");
        }
        return sb.toString();
    }

    /*
    View.OnClickListener onBtnPercentagePlus = new View.OnClickListener() {
        public void onClick(View v) {
            currentObject.maxWeightPercentage += 5;
            if (currentObject.maxWeightPercentage > 100) {
                currentObject.maxWeightPercentage -= 5;
            }
            if (currentObject.maxWeightPercentage < 10) {
                currentObject.maxWeightPercentage = 10;
            }
            ((TextView) addDialog.findViewById(R.id.add_workout_tv_perc_value)).setText(Float.toString(currentObject.maxWeightPercentage));
        }
    };

    View.OnClickListener onBtnPercentageMinus = new View.OnClickListener() {
        public void onClick(View v) {
            currentObject.maxWeightPercentage -= 5;
            if (currentObject.maxWeightPercentage < 10) {
                currentObject.maxWeightPercentage = 10;
            }
            ((TextView) addDialog.findViewById(R.id.add_workout_tv_perc_value)).setText(Float.toString(currentObject.maxWeightPercentage));
        }
    };
    */

    private View.OnClickListener onBtnOrderPlus = new View.OnClickListener() {
        public void onClick(View v) {
            currentObject.order += 1;
            if (currentObject.order > 99) {
                currentObject.order -= 1;
            }
            ((TextView) addDialog.findViewById(R.id.add_workout_tv_order_value)).setText(Integer.toString(currentObject.order));
        }
    };

    private View.OnClickListener onBtnOrderMinus = new View.OnClickListener() {
        public void onClick(View v) {
            currentObject.order -= 1;
            if (currentObject.order < 1) {
                currentObject.order = 1;
            }
            ((TextView) addDialog.findViewById(R.id.add_workout_tv_order_value)).setText(Integer.toString(currentObject.order));
        }
    };

    private View.OnClickListener onEditDialogOk = new View.OnClickListener() {
        public void onClick(View v) {

            currentObject.name = getEditTextValue(R.id.add_workout_et_name);
            currentObject.endurance = ((Switch) addDialog.findViewById(R.id.add_workout_sw_endurance)).isChecked();

            List<Integer> days = new ArrayList<>();
            if (((CheckBox) addDialog.findViewById(R.id.add_workout_ctv_day1)).isChecked())
                days.add(1);
            if (((CheckBox) addDialog.findViewById(R.id.add_workout_ctv_day2)).isChecked())
                days.add(2);
            if (((CheckBox) addDialog.findViewById(R.id.add_workout_ctv_day3)).isChecked())
                days.add(3);
            if (((CheckBox) addDialog.findViewById(R.id.add_workout_ctv_day4)).isChecked())
                days.add(4);
            if (((CheckBox) addDialog.findViewById(R.id.add_workout_ctv_day5)).isChecked())
                days.add(5);
            if (((CheckBox) addDialog.findViewById(R.id.add_workout_ctv_day6)).isChecked())
                days.add(6);
            if (((CheckBox) addDialog.findViewById(R.id.add_workout_ctv_day7)).isChecked())
                days.add(7);

            int[] dayInt = new int[days.size()];
            for (int i = 0; i < dayInt.length; i++) {
                dayInt[i] = days.get(i);
            }
            currentObject.weekdays = dayInt;
            currentObject.weekday = Utility.intToString(dayInt);

            currentObject.maxWeightPercentage = getEditTextValue(R.id.add_workout_perc);
            currentObject.maxWeightPercentages = Utility.isNotEmpty(currentObject.maxWeightPercentage) ? Utility.stringToFloat(currentObject.maxWeightPercentage) : new float[0];

            if (currentObject.maxWeightPercentages.length > 0 && currentObject.maxWeightPercentages.length != currentObject.weekdays.length) {
                float[] tmp = new float[currentObject.weekdays.length];
                for (int i = 0; i < currentObject.weekdays.length; i++) {
                    if (i == 0)
                        tmp[i] = currentObject.maxWeightPercentages[i];
                    if (i > 0) {
                        if (i >= currentObject.maxWeightPercentages.length) {
                            tmp[i] = currentObject.maxWeightPercentages[0];
                        } else {
                            tmp[i] = currentObject.maxWeightPercentages[i];
                        }
                    }
                }
                currentObject.maxWeightPercentages = tmp;
                currentObject.maxWeightPercentage = Utility.floatToString(tmp);
            }

            if (currentObject.name != null && !currentObject.name.isEmpty()) {
                if (currentObject.isNew)
                    DataIntegrator.localAppStorage.workouts.add(currentObject);

                DataIntegrator.writeData(getContext(), DataIntegrator.localAppStorage, null);

                updateLists(getLayoutInflater());
                currentObject.isNew = false;
                currentObject = null;
                selectedExercises.clear();

                addDialog.dismiss();
            }
        }
    };

    private View.OnClickListener onEditDialogRemove = new View.OnClickListener() {
        public void onClick(View v) {
            DataIntegrator.localAppStorage.workouts.remove(currentObject);
            DataIntegrator.writeData(getContext(), DataIntegrator.localAppStorage, null);
            updateLists(getLayoutInflater());
            currentObject = null;
            selectedExercises.clear();
            addDialog.dismiss();
        }
    };

    private View.OnClickListener onEditDialogCopy = new View.OnClickListener() {
        public void onClick(View v) {
            Workout workout = new Workout();
            workout.endurance = currentObject.endurance;
            workout.weekday = currentObject.weekday;
            workout.maxWeightPercentage = currentObject.maxWeightPercentage;
            workout.isNew = true;
            workout.name = currentObject.name;
            workout.order = currentObject.order;
            workout.exercises = new ArrayList<>(currentObject.exercises);
            workout.id = DataIntegrator.localAppStorage.getNextId();
            currentObject = workout;
            selectedExercises.clear();
            addDialog.show();
        }
    };

}