package email.kleck.android.workouter.business;

import android.util.JsonReader;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import email.kleck.android.workouter.datamodel.DataStorage;
import email.kleck.android.workouter.datamodel.Device;
import email.kleck.android.workouter.datamodel.Exercise;
import email.kleck.android.workouter.datamodel.Settings;
import email.kleck.android.workouter.datamodel.Training;
import email.kleck.android.workouter.datamodel.Workout;

public class JSONProcessor implements StorageProcessor {

    @Override
    public void write(DataStorage dataStorage, OutputStream outputStream) throws Throwable {

        writeJsonStream(outputStream, dataStorage);

    }

    @Override
    public DataStorage parse(InputStream in) throws Throwable {
        try (JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return readDataStorage(reader);
        }
    }

    @Override
    public String getFileExtension() {
        return ".json";
    }

    private void writeJsonStream(OutputStream out, DataStorage dataStorage) throws IOException {
        out.write(JSONObject.wrap(dataStorage.toString()).toString().getBytes());
    }

    private DataStorage readDataStorage(JsonReader reader) throws IOException {
        DataStorage dataStorage = new DataStorage();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            // Starts by looking for the entry tag
            switch (name) {
                case Settings.TAG_ELEMENT:
                    dataStorage.settings = readSettings(reader);
                    break;
                case Training.TAG_ELEMENT:
                    dataStorage.trainings = readTrainings(reader, dataStorage);
                    break;
                case Workout.TAG_ELEMENT:
                    dataStorage.workouts = readWorkouts(reader, dataStorage);
                    break;
                case Exercise.TAG_ELEMENT:
                    dataStorage.exercises = readExercises(reader, dataStorage);
                    break;
                case Device.TAG_ELEMENT:
                    dataStorage.devices = readDevices(reader);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        // self-clean incomplete data
        List<Workout> removalW = new ArrayList<>();
        List<Exercise> removalE = new ArrayList<>();
        List<Device> removalD = new ArrayList<>();
        for (Device device : dataStorage.devices) {
            if (device.name == null || device.name.isEmpty()) {
                removalD.add(device);
            }
        }
        dataStorage.devices.removeAll(removalD);

        for (Exercise exercise : dataStorage.exercises) {
            if (exercise.device != null) {
                boolean found = false;
                for (Device d : dataStorage.devices) {
                    found |= d != null && d.id.equals(exercise.device.id);
                }
                if (!found || removalD.contains(exercise.device)) {
                    exercise.device = null;
                }
            }
            if (exercise.device == null || exercise.device.id == null) {
                removalE.add(exercise);
            }
        }
        dataStorage.exercises.removeAll(removalE);

        for (Workout workout : dataStorage.workouts) {
            if (workout.exercises != null) {
                List<Exercise> tmp = new ArrayList<>();
                for (Exercise e : workout.exercises) {
                    boolean found = false;
                    for (Exercise tmpE : dataStorage.exercises) {
                        found |= e != null && tmpE.id.equals(e.id);
                    }
                    if (!found || removalE.contains(e)) {
                        tmp.add(e);
                    }
                }
                workout.exercises.removeAll(tmp);
            }
            if (workout.exercises == null || workout.exercises.isEmpty()) {
                removalW.add(workout);
            }
        }
        dataStorage.workouts.removeAll(removalW);

        return dataStorage;
    }


    private List<Training> readTrainings(JsonReader reader, DataStorage dataStorage) throws IOException {
        List<Training> list = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            Training training = new Training();
            while (reader.hasNext()) {
                String name = reader.nextName();
                // Starts by looking for the entry tag
                if (name.equals(Training.TAG_ID)) {
                    training.id = reader.nextLong();
                } else if (name.equals(Workout.TAG_ELEMENT)) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        Long id = reader.nextLong();
                        training.workouts.add((Workout) getBaseItemById(dataStorage.workouts, id));
                    }
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }
            list.add(training);
            reader.endObject();
        }
        reader.endArray();
        return list;
    }

    private List<Workout> readWorkouts(JsonReader reader, DataStorage dataStorage) throws IOException {
        List<Workout> list = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            Workout workout = new Workout();
            while (reader.hasNext()) {
                String name = reader.nextName();
                // Starts by looking for the entry tag
                switch (name) {
                    case Workout.TAG_ID:
                        workout.id = reader.nextLong();
                        break;
                    case Workout.TAG_NAME:
                        workout.name = reader.nextString();
                        break;
                    case Exercise.TAG_ELEMENT:
                        reader.beginArray();
                        while (reader.hasNext()) {
                            Long id = reader.nextLong();
                            workout.exercises.add((Exercise) getBaseItemById(dataStorage.exercises, id));
                        }
                        reader.endArray();
                        break;
                    case Workout.TAG_MAX_WEIGHT_PERCENTAGE:
                        workout.maxWeightPercentage = reader.nextString();
                        workout.maxWeightPercentages = Utility.stringToFloat(workout.maxWeightPercentage);
                        break;
                    case Workout.TAG_ENDURANCE:
                        workout.endurance = reader.nextBoolean();
                        break;
                    case Workout.TAG_WEEKDAY:
                        workout.weekday = reader.nextString();
                        workout.weekdays = Utility.stringToInt(workout.weekday);
                        break;
                    case Workout.TAG_ORDER:
                        workout.order = reader.nextInt();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            list.add(workout);
            reader.endObject();
        }
        reader.endArray();
        return list;
    }

    private List<Exercise> readExercises(JsonReader reader, DataStorage dataStorage) throws IOException {
        List<Exercise> list = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            Exercise exercise = new Exercise();
            while (reader.hasNext()) {
                String name = reader.nextName();
                // Starts by looking for the entry tag
                switch (name) {
                    case Exercise.TAG_ID:
                        exercise.id = reader.nextLong();
                        break;
                    case Device.TAG_ELEMENT:
                        Long id = reader.nextLong();
                        exercise.device = (Device) getBaseItemById(dataStorage.devices, id);
                        break;
                    case Exercise.TAG_REPETITIONS:
                        exercise.repetitions = reader.nextInt();
                        break;
                    case Exercise.TAG_ROUNDS:
                        exercise.rounds = reader.nextInt();
                        break;
                    case Exercise.TAG_ENDURANCE_REPETITIONS:
                        exercise.enduranceRepetitions = reader.nextInt();
                        break;
                    case Exercise.TAG_ENDURANCE_ROUNDS:
                        exercise.enduranceRounds = reader.nextInt();
                        break;
                    case Exercise.TAG_TIME:
                        exercise.time = reader.nextInt();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            list.add(exercise);
            reader.endObject();
        }
        reader.endArray();
        return list;
    }

    private List<Device> readDevices(JsonReader reader) throws IOException {
        List<Device> list = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            Device device = new Device();
            while (reader.hasNext()) {
                String name = reader.nextName();
                // Starts by looking for the entry tag
                switch (name) {
                    case Device.TAG_ID:
                        device.id = reader.nextLong();
                        break;
                    case Device.TAG_DIFFICULTY:
                        device.difficulty = Double.valueOf(reader.nextDouble()).floatValue();
                        break;
                    case Device.TAG_MAX_WEIGHT:
                        device.maxWeight = Double.valueOf(reader.nextDouble()).floatValue();
                        break;
                    case Device.TAG_PERSONAL_MAX_WEIGHT:
                        device.personalMaxWeight = Double.valueOf(reader.nextDouble()).floatValue();
                        break;
                    case Device.TAG_WEIGHT_STEPS:
                        device.weightSteps = Double.valueOf(reader.nextDouble()).floatValue();
                        break;
                    case Device.TAG_START_WEIGHT:
                        device.startWeight = Double.valueOf(reader.nextDouble()).floatValue();
                        break;
                    case Device.TAG_ENDURANCE_WEIGHT:
                        device.enduranceWeight = Double.valueOf(reader.nextDouble()).floatValue();
                        break;
                    case Device.TAG_IGNORE_PERCENT:
                        device.ignorePercent = reader.nextBoolean();
                        break;
                    case Device.TAG_NAME:
                        device.name = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            list.add(device);
            reader.endObject();
        }
        reader.endArray();
        return list;
    }

    private Settings readSettings(JsonReader reader) throws IOException {
        reader.beginObject();
        Settings settings = new Settings();

        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case Settings.TAG_TEXT_R:
                    settings.cTextR = reader.nextInt();
                    break;
                case Settings.TAG_TEXT_G:
                    settings.cTextG = reader.nextInt();
                    break;
                case Settings.TAG_TEXT_B:
                    settings.cTextB = reader.nextInt();
                    break;
                case Settings.TAG_BACK_R:
                    settings.cBackR = reader.nextInt();
                    break;
                case Settings.TAG_BACK_G:
                    settings.cBackG = reader.nextInt();
                    break;
                case Settings.TAG_BACK_B:
                    settings.cBackB = reader.nextInt();
                    break;
                case Settings.TAG_TEXT_SIZE:
                    settings.textSize = reader.nextInt();
                    break;
                case Settings.TAG_KG:
                    settings.kg = reader.nextBoolean();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return settings;
    }

}
