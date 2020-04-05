package email.kleck.android.workouter.ui.exercises;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import email.kleck.android.workouter.R;
import email.kleck.android.workouter.business.DataIntegrator;
import email.kleck.android.workouter.datamodel.Device;
import email.kleck.android.workouter.datamodel.Exercise;
import email.kleck.android.workouter.ui.BaseFragment;
import email.kleck.android.workouter.ui.Constants;

public class ExerciseFragment extends BaseFragment {

    private Dialog addDialog;

    private Exercise currentObject;
    private Device currentDevice;

    private int textColor = 0;
    private float fontSize = 24f;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_exercises, container, false);

        textColor = Color.rgb(DataIntegrator.localAppStorage.settings.cTextR, DataIntegrator.localAppStorage.settings.cTextG, DataIntegrator.localAppStorage.settings.cTextB);
        fontSize = Integer.valueOf(DataIntegrator.localAppStorage.settings.textSize).floatValue();

        FloatingActionButton fab = container.getRootView().findViewById(R.id.fab);
        toggleFAB(fab, true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateDialogValues("", "", "", "", "", "");
                currentObject = new Exercise();
                currentObject.id = DataIntegrator.localAppStorage.getNextId();
                currentObject.isNew = true;
                addDialog.show();
            }
        });

        updateLists(inflater);

        root.setBackgroundColor(Color.rgb(DataIntegrator.localAppStorage.settings.cBackR, DataIntegrator.localAppStorage.settings.cBackG, DataIntegrator.localAppStorage.settings.cBackB));

        addDialog = new Dialog(this.getContext(), R.style.Theme_AppCompat_Light_Dialog);
        addDialog.setContentView(R.layout.dialog_add_exercise);

        addDialog.findViewById(R.id.add_exercise_btn).setOnClickListener(onEditDialogOk);
        addDialog.findViewById(R.id.remove_exercise_btn).setOnClickListener(onEditDialogRemove);

        addDialog.findViewById(R.id.add_exercise_btn_reps_plus).setOnClickListener(onBtnRepsPlus);
        addDialog.findViewById(R.id.add_exercise_btn_reps_minus).setOnClickListener(onBtnRepsMinus);
        addDialog.findViewById(R.id.add_exercise_btn_rounds_plus).setOnClickListener(onBtnRoundsPlus);
        addDialog.findViewById(R.id.add_exercise_btn_rounds_minus).setOnClickListener(onBtnRoundsMinus);

        addDialog.findViewById(R.id.add_exercise_btn_end_reps_plus).setOnClickListener(onBtnEndRepsPlus);
        addDialog.findViewById(R.id.add_exercise_btn_end_reps_minus).setOnClickListener(onBtnEndRepsMinus);
        addDialog.findViewById(R.id.add_exercise_btn_end_rounds_plus).setOnClickListener(onBtnEndRoundsPlus);
        addDialog.findViewById(R.id.add_exercise_btn_end_rounds_minus).setOnClickListener(onBtnEndRoundsMinus);

        AutoCompleteTextView mText = addDialog.findViewById(R.id.add_exercise_ac_name);
        List<String> strings = new ArrayList<>();
        for (Device tmp : DataIntegrator.localAppStorage.devices) {
            strings.add(tmp.name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, strings);
        mText.setAdapter(adapter);
        mText.setText("");

        return root;
    }

    private View.OnClickListener rowClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            String name = ((TextView) view.findViewById(R.id.tbl_row_tv_1)).getText().toString();
            currentDevice = getDeviceForExercise(name);
            for (Exercise exercise : DataIntegrator.localAppStorage.exercises) {
                if (currentDevice != null && exercise.device.id.longValue() == currentDevice.id.longValue()) {
                    currentObject = exercise;
                    break;
                }
            }
            currentObject.isNew = false;
            populateDialogValues(currentDevice != null ? currentDevice.name : "", Integer.toString(currentObject.repetitions), Integer.toString(currentObject.rounds), Integer.toString(currentObject.time), Integer.toString(currentObject.enduranceRepetitions), Integer.toString(currentObject.enduranceRounds));
            addDialog.show();
        }
    };

    private void populateDialogValues(String name, String repetitions, String rounds, String time, String enduranceRepetitions, String enduranceRounds) {
        ((EditText) addDialog.findViewById(R.id.add_exercise_ac_name)).setText(name);
        ((TextView) addDialog.findViewById(R.id.add_exercise_tv_reps_value)).setText(repetitions);
        ((TextView) addDialog.findViewById(R.id.add_exercise_tv_rounds_value)).setText(rounds);
        ((EditText) addDialog.findViewById(R.id.add_exercise_et_time)).setText(time);
        ((TextView) addDialog.findViewById(R.id.add_exercise_tv_end_reps_value)).setText(enduranceRepetitions);
        ((TextView) addDialog.findViewById(R.id.add_exercise_tv_end_rounds_value)).setText(enduranceRounds);

    }

    private void updateLists(@NonNull LayoutInflater inflater) {
        TableLayout tbl = root.findViewById(R.id.exercises_listitems);
        tbl.removeAllViews();

        // init header row
        View row = inflater.inflate(R.layout.tbl_row_3tv, tbl, false);
        tbl.addView(row);
        row.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_LIGHT_BLUE));

        TextView tv = row.findViewById(R.id.tbl_row_tv_1);
        tv.setText(getText(R.string.text_name));
        tv.setTextSize(fontSize);
        tv.setTextColor(textColor);

        tv = row.findViewById(R.id.tbl_row_tv_2);
        tv.setText(getText(R.string.text_rnd_reps));
        tv.setTextSize(fontSize);
        tv.setWidth(245);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(textColor);

        tv = row.findViewById(R.id.tbl_row_tv_3);
        tv.setText(getText(R.string.text_end_rnd_reps));
        tv.setTextSize(fontSize);
        tv.setWidth(245);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(textColor);

        for (Exercise exercise : DataIntegrator.localAppStorage.exercises) {
            row = inflater.inflate(R.layout.tbl_row_3tv, tbl, false);
            row.setOnClickListener(rowClickListener);
            tbl.addView(row);

            Device tmpDevice = getDeviceForExercise(exercise.device.id);

            tv = row.findViewById(R.id.tbl_row_tv_1);
            tv.setText(tmpDevice != null ? tmpDevice.name : "");
            tv.setTextSize(fontSize);
            tv.setTextColor(textColor);

            if (exercise.time > 0) {
                tv = row.findViewById(R.id.tbl_row_tv_2);
                tv.setText(Integer.toString(exercise.time));
                tv.setTextSize(fontSize);
                tv.setWidth(245);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(textColor);
                tv.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_GRAY));

                tv = row.findViewById(R.id.tbl_row_tv_3);
                tv.setText("");
            } else {
                tv = row.findViewById(R.id.tbl_row_tv_2);
                tv.setText(Integer.toString(exercise.repetitions) + " / " + Integer.toString(exercise.rounds));
                tv.setTextSize(fontSize);
                tv.setWidth(245);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(textColor);
                tv.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_GRAY));

                tv = row.findViewById(R.id.tbl_row_tv_3);
                tv.setText(Integer.toString(exercise.enduranceRepetitions) + " / " + Integer.toString(exercise.enduranceRounds));
                tv.setTextSize(fontSize);
                tv.setWidth(245);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(textColor);
                tv.setBackgroundColor(Color.parseColor(Constants.COLOR_DARK_GREEN));
            }
        }
    }

    private String getEditTextValue(int id) {
        return ((EditText) addDialog.findViewById(id)).getText().toString();
    }

    View.OnClickListener onBtnRepsPlus = new View.OnClickListener() {
        public void onClick(View v) {
            currentObject.repetitions += 1;
            if (currentObject.repetitions > 999) {
                currentObject.repetitions -= 1;
            }
            ((TextView) addDialog.findViewById(R.id.add_exercise_tv_reps_value)).setText(Integer.toString(currentObject.repetitions));
        }
    };

    View.OnClickListener onBtnRepsMinus = new View.OnClickListener() {
        public void onClick(View v) {
            currentObject.repetitions -= 1;
            if (currentObject.repetitions < 0) {
                currentObject.repetitions = 0;
            }
            ((TextView) addDialog.findViewById(R.id.add_exercise_tv_reps_value)).setText(Integer.toString(currentObject.repetitions));
        }
    };

    View.OnClickListener onBtnRoundsPlus = new View.OnClickListener() {
        public void onClick(View v) {
            currentObject.rounds += 1;
            if (currentObject.rounds > 999) {
                currentObject.rounds -= 1;
            }
            ((TextView) addDialog.findViewById(R.id.add_exercise_tv_rounds_value)).setText(Integer.toString(currentObject.rounds));
        }
    };

    View.OnClickListener onBtnRoundsMinus = new View.OnClickListener() {
        public void onClick(View v) {
            currentObject.rounds -= 1;
            if (currentObject.rounds < 0) {
                currentObject.rounds = 0;
            }
            ((TextView) addDialog.findViewById(R.id.add_exercise_tv_rounds_value)).setText(Integer.toString(currentObject.rounds));
        }
    };

    View.OnClickListener onBtnEndRepsPlus = new View.OnClickListener() {
        public void onClick(View v) {
            currentObject.enduranceRepetitions += 1;
            if (currentObject.enduranceRepetitions > 999) {
                currentObject.enduranceRepetitions -= 1;
            }
            ((TextView) addDialog.findViewById(R.id.add_exercise_tv_end_reps_value)).setText(Integer.toString(currentObject.enduranceRepetitions));
        }
    };

    View.OnClickListener onBtnEndRepsMinus = new View.OnClickListener() {
        public void onClick(View v) {
            currentObject.enduranceRepetitions -= 1;
            if (currentObject.enduranceRepetitions < 0) {
                currentObject.enduranceRepetitions = 0;
            }
            ((TextView) addDialog.findViewById(R.id.add_exercise_tv_end_reps_value)).setText(Integer.toString(currentObject.enduranceRepetitions));
        }
    };

    View.OnClickListener onBtnEndRoundsPlus = new View.OnClickListener() {
        public void onClick(View v) {
            currentObject.enduranceRounds += 1;
            if (currentObject.enduranceRounds > 999) {
                currentObject.enduranceRounds -= 1;
            }
            ((TextView) addDialog.findViewById(R.id.add_exercise_tv_end_rounds_value)).setText(Integer.toString(currentObject.enduranceRounds));
        }
    };

    View.OnClickListener onBtnEndRoundsMinus = new View.OnClickListener() {
        public void onClick(View v) {
            currentObject.enduranceRounds -= 1;
            if (currentObject.enduranceRounds < 0) {
                currentObject.enduranceRounds = 0;
            }
            ((TextView) addDialog.findViewById(R.id.add_exercise_tv_end_rounds_value)).setText(Integer.toString(currentObject.enduranceRounds));
        }
    };

    View.OnClickListener onEditDialogOk = new View.OnClickListener() {
        public void onClick(View v) {

            currentObject.device = getDeviceForExercise(getEditTextValue(R.id.add_exercise_ac_name));

            String time = getEditTextValue(R.id.add_exercise_et_time);
            if (time != null && !time.isEmpty())
                currentObject.time = Integer.valueOf(time);

            if (currentObject.device.id != null && currentObject.device.id > 0L) {
                if (currentObject.isNew)
                    DataIntegrator.localAppStorage.exercises.add(currentObject);

                DataIntegrator.writeData(getContext(), DataIntegrator.localAppStorage, null);

                updateLists(getLayoutInflater());
                currentObject.isNew = false;
                currentObject = null;
                currentDevice = null;
            }

            addDialog.dismiss();
        }
    };

    View.OnClickListener onEditDialogRemove = new View.OnClickListener() {
        public void onClick(View v) {
            // remove from workouts
            DataIntegrator.localAppStorage.workouts.forEach(workout -> workout.exercises.remove(currentObject));
            DataIntegrator.localAppStorage.exercises.remove(currentObject);
            DataIntegrator.writeData(getContext(), DataIntegrator.localAppStorage, null);
            updateLists(getLayoutInflater());
            currentObject = null;
            currentDevice = null;
            addDialog.dismiss();
        }
    };

}