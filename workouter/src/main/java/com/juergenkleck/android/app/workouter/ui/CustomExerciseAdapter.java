package com.juergenkleck.android.app.workouter.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import com.juergenkleck.android.app.workouter.R;
import com.juergenkleck.android.app.workouter.datamodel.Exercise;

/**
 * Android app - Workouter
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class CustomExerciseAdapter extends ArrayAdapter<Exercise> {

    private List<Exercise> items;

    public CustomExerciseAdapter(Context context, ArrayList<Exercise> items) {
        super(context, 0, items);
        this.items = new ArrayList<>(items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Exercise exercise = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_adapter_2line, parent, false);
        }
        // Lookup view for data population
        TextView tvValue1 = convertView.findViewById(R.id.cdaValue1);
        TextView tvValue2 = convertView.findViewById(R.id.cdaValue2);
        // Populate the data into the template view using the data object
        tvValue1.setText(exercise.device.name);
        if (exercise.device.difficulty > 0) {
            tvValue2.setText(Float.toString(exercise.device.difficulty));
        } else {
            tvValue2.setText(Float.toString(exercise.device.personalMaxWeight));
        }
        // Return the completed view to render on screen
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return acFilter;
    }

    private Filter acFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            List<Exercise> values = new ArrayList<>();
            if (charSequence != null) {
                for (Exercise exercise : items) {
                    if (exercise.device.name.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        values.add(exercise);
                    }
                }
            }
            results.count = values.size();
            results.values = values;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            List<Exercise> filteredList = (ArrayList<Exercise>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (Exercise c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}

