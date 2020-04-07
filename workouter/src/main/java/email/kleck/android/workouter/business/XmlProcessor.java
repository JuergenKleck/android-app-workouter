package email.kleck.android.workouter.business;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import email.kleck.android.workouter.datamodel.DataStorage;
import email.kleck.android.workouter.datamodel.Device;
import email.kleck.android.workouter.datamodel.Exercise;
import email.kleck.android.workouter.datamodel.Settings;
import email.kleck.android.workouter.datamodel.Training;
import email.kleck.android.workouter.datamodel.Workout;

/**
 * XML Processor handling the XML data storage
 */
public class XmlProcessor implements StorageProcessor {

    // We don't use namespaces
    private static final String ns = null;

    @Override
    public void write(DataStorage dataStorage, OutputStream outputStream) {
        try {
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(outputStream, null);
            serializer.startTag(ns, DataStorage.TAG_ELEMENT);

            writeSettings(dataStorage, serializer);
            writeDevices(dataStorage, serializer);
            writeExercises(dataStorage, serializer);
            writeWorkouts(dataStorage, serializer);

            serializer.endTag(ns, DataStorage.TAG_ELEMENT);
            serializer.endDocument();
        } catch (IOException ignored) {

        }
    }

    private void writeWorkouts(DataStorage dataStorage, XmlSerializer serializer) throws IOException {
        for (Workout workout : dataStorage.workouts) {
            serializer.startTag(ns, Workout.TAG_ELEMENT);
            writeTag(serializer, Workout.TAG_ID, workout.id);
            writeTag(serializer, Workout.TAG_NAME, workout.name);
            writeTag(serializer, Workout.TAG_MAX_WEIGHT_PERCENTAGE, Utility.floatToString(workout.maxWeightPercentages));
            writeTag(serializer, Workout.TAG_WEEKDAY, Utility.intToString(workout.weekdays));
            writeTag(serializer, Workout.TAG_ENDURANCE, workout.endurance);
            writeTag(serializer, Workout.TAG_ORDER, workout.order);
            for (Exercise exercise : workout.exercises) {
                serializer.startTag(ns, Exercise.TAG_ELEMENT);
                serializer.attribute(null, Exercise.TAG_ID, Long.toString(exercise.id));
                serializer.endTag(ns, Exercise.TAG_ELEMENT);
            }
            serializer.endTag(ns, Workout.TAG_ELEMENT);
        }
    }

    private void writeExercises(DataStorage dataStorage, XmlSerializer serializer) throws IOException {
        for (Exercise exercise : dataStorage.exercises) {
            serializer.startTag(ns, Exercise.TAG_ELEMENT);
            writeTag(serializer, Exercise.TAG_ID, exercise.id);
            writeTag(serializer, Exercise.TAG_REPETITIONS, exercise.repetitions);
            writeTag(serializer, Exercise.TAG_ROUNDS, exercise.rounds);
            writeTag(serializer, Exercise.TAG_ENDURANCE_REPETITIONS, exercise.enduranceRepetitions);
            writeTag(serializer, Exercise.TAG_ENDURANCE_ROUNDS, exercise.enduranceRounds);
            writeTag(serializer, Exercise.TAG_TIME, exercise.time);
            serializer.startTag(ns, Device.TAG_ELEMENT);
            serializer.attribute(null, Device.TAG_ID, Long.toString(exercise.device.id));
            serializer.endTag(ns, Device.TAG_ELEMENT);
            serializer.endTag(ns, Exercise.TAG_ELEMENT);
        }
    }

    private void writeDevices(DataStorage dataStorage, XmlSerializer serializer) throws IOException {
        for (Device device : dataStorage.devices) {
            serializer.startTag(ns, Device.TAG_ELEMENT);
            writeTag(serializer, Device.TAG_ID, device.id);
            writeTag(serializer, Device.TAG_DIFFICULTY, device.difficulty);
            writeTag(serializer, Device.TAG_MAX_WEIGHT, device.maxWeight);
            writeTag(serializer, Device.TAG_NAME, device.name);
            writeTag(serializer, Device.TAG_PERSONAL_MAX_WEIGHT, device.personalMaxWeight);
            writeTag(serializer, Device.TAG_WEIGHT_STEPS, device.weightSteps);
            writeTag(serializer, Device.TAG_START_WEIGHT, device.startWeight);
            writeTag(serializer, Device.TAG_IGNORE_PERCENT, device.ignorePercent);
            writeTag(serializer, Device.TAG_ENDURANCE_WEIGHT, device.enduranceWeight);
            serializer.endTag(ns, Device.TAG_ELEMENT);
        }
    }

    private void writeSettings(DataStorage dataStorage, XmlSerializer serializer) throws IOException {
        serializer.startTag(ns, Settings.TAG_ELEMENT);
        writeTag(serializer, Settings.TAG_TEXT_R, dataStorage.settings.cTextR);
        writeTag(serializer, Settings.TAG_TEXT_G, dataStorage.settings.cTextG);
        writeTag(serializer, Settings.TAG_TEXT_B, dataStorage.settings.cTextB);
        writeTag(serializer, Settings.TAG_BACK_R, dataStorage.settings.cBackR);
        writeTag(serializer, Settings.TAG_BACK_G, dataStorage.settings.cBackG);
        writeTag(serializer, Settings.TAG_BACK_B, dataStorage.settings.cBackB);
        writeTag(serializer, Settings.TAG_TEXT_SIZE, dataStorage.settings.textSize);
        writeTag(serializer, Settings.TAG_KG, dataStorage.settings.kg);
        serializer.endTag(ns, Settings.TAG_ELEMENT);
    }

    private void writeTag(XmlSerializer serializer, String tagName, Object value) throws IOException {
        serializer.startTag(ns, tagName);
        serializer.text(value != null ? value.toString() : "");
        serializer.endTag(ns, tagName);
    }

    @Override
    public DataStorage parse(InputStream in) throws Throwable {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readDataStorage(parser);
        } finally {
            in.close();
        }
    }

    @Override
    public String getFileExtension() {
        return ".xml";
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private DataStorage readDataStorage(XmlPullParser parser) throws XmlPullParserException, IOException {
        DataStorage dataStorage = new DataStorage();

        parser.require(XmlPullParser.START_TAG, ns, DataStorage.TAG_ELEMENT);
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            switch (name) {
                case Settings.TAG_ELEMENT:
                    dataStorage.settings = readSettings(parser);
                    break;
                case Training.TAG_ELEMENT:
                    dataStorage.trainings.add(readTraining(parser, dataStorage));
                    break;
                case Workout.TAG_ELEMENT:
                    dataStorage.workouts.add(readWorkout(parser, dataStorage));
                    break;
                case Exercise.TAG_ELEMENT:
                    dataStorage.exercises.add(readExercise(parser, dataStorage));
                    break;
                case Device.TAG_ELEMENT:
                    dataStorage.devices.add(readDevice(parser));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

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

    private Training readTraining(XmlPullParser parser, DataStorage dataStorage) throws XmlPullParserException, IOException {
        Training training = new Training();

        parser.require(XmlPullParser.START_TAG, ns, Training.TAG_ELEMENT);
        while (parser.next() == XmlPullParser.START_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals(Training.TAG_ID)) {
                training.id = Long.valueOf(readTag(parser, name));
            } else if (name.equals(Workout.TAG_ELEMENT)) {
                Long id = Long.valueOf(parser.getAttributeValue(null, Training.TAG_ID));
                training.workouts.add((Workout) getBaseItemById(dataStorage.workouts, id));
                parser.nextTag();
            } else {
                skip(parser);
            }
        }
        return training;
    }

    private Workout readWorkout(XmlPullParser parser, DataStorage dataStorage) throws XmlPullParserException, IOException {
        Workout workout = new Workout();

        parser.require(XmlPullParser.START_TAG, ns, Workout.TAG_ELEMENT);
        while (parser.next() == XmlPullParser.START_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            switch (name) {
                case Workout.TAG_ID:
                    workout.id = Long.valueOf(readTag(parser, name));
                    break;
                case Workout.TAG_NAME:
                    workout.name = readTag(parser, name);
                    break;
                case Exercise.TAG_ELEMENT:
                    Long id = Long.valueOf(parser.getAttributeValue(null, Workout.TAG_ID));
                    workout.exercises.add((Exercise) getBaseItemById(dataStorage.exercises, id));
                    parser.nextTag();
                    break;
                case Workout.TAG_MAX_WEIGHT_PERCENTAGE:
                    workout.maxWeightPercentage = readTag(parser, name);
                    workout.maxWeightPercentages = Utility.stringToFloat(workout.maxWeightPercentage);
                    break;
                case Workout.TAG_ENDURANCE:
                    workout.endurance = Boolean.parseBoolean(readTag(parser, name));
                    break;
                case Workout.TAG_WEEKDAY:
                    workout.weekday = readTag(parser, name);
                    workout.weekdays = Utility.stringToInt(workout.weekday);
                    break;
                case Workout.TAG_ORDER:
                    workout.order = Integer.parseInt(readTag(parser, name));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return workout;
    }

    private Exercise readExercise(XmlPullParser parser, DataStorage dataStorage) throws XmlPullParserException, IOException {
        Exercise exercise = new Exercise();

        parser.require(XmlPullParser.START_TAG, ns, Exercise.TAG_ELEMENT);
        while (parser.next() == XmlPullParser.START_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            switch (name) {
                case Exercise.TAG_ID:
                    exercise.id = Long.valueOf(readTag(parser, name));
                    break;
                case Device.TAG_ELEMENT:
                    Long id = Long.valueOf(parser.getAttributeValue(null, Exercise.TAG_ID));
                    exercise.device = (Device) getBaseItemById(dataStorage.devices, id);
                    parser.nextTag();
                    break;
                case Exercise.TAG_REPETITIONS:
                    exercise.repetitions = Integer.parseInt(readTag(parser, name));
                    break;
                case Exercise.TAG_ROUNDS:
                    exercise.rounds = Integer.parseInt(readTag(parser, name));
                    break;
                case Exercise.TAG_ENDURANCE_REPETITIONS:
                    exercise.enduranceRepetitions = Integer.parseInt(readTag(parser, name));
                    break;
                case Exercise.TAG_ENDURANCE_ROUNDS:
                    exercise.enduranceRounds = Integer.parseInt(readTag(parser, name));
                    break;
                case Exercise.TAG_TIME:
                    exercise.time = Integer.parseInt(readTag(parser, name));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return exercise;
    }

    private Device readDevice(XmlPullParser parser) throws XmlPullParserException, IOException {
        Device device = new Device();

        parser.require(XmlPullParser.START_TAG, ns, Device.TAG_ELEMENT);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            switch (name) {
                case Device.TAG_ID:
                    device.id = Long.valueOf(readTag(parser, name));
                    break;
                case Device.TAG_DIFFICULTY:
                    device.difficulty = Float.parseFloat(readTag(parser, name));
                    break;
                case Device.TAG_MAX_WEIGHT:
                    device.maxWeight = Float.parseFloat(readTag(parser, name));
                    break;
                case Device.TAG_PERSONAL_MAX_WEIGHT:
                    device.personalMaxWeight = Float.parseFloat(readTag(parser, name));
                    break;
                case Device.TAG_WEIGHT_STEPS:
                    device.weightSteps = Float.parseFloat(readTag(parser, name));
                    break;
                case Device.TAG_START_WEIGHT:
                    device.startWeight = Float.parseFloat(readTag(parser, name));
                    break;
                case Device.TAG_ENDURANCE_WEIGHT:
                    device.enduranceWeight = Float.parseFloat(readTag(parser, name));
                    break;
                case Device.TAG_IGNORE_PERCENT:
                    device.ignorePercent = Boolean.parseBoolean(readTag(parser, name));
                    break;
                case Device.TAG_NAME:
                    device.name = readTag(parser, name);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return device;
    }

    private Settings readSettings(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, Settings.TAG_ELEMENT);
        Settings settings = new Settings();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case Settings.TAG_TEXT_R:
                    settings.cTextR = Integer.parseInt(readTag(parser, name));
                    break;
                case Settings.TAG_TEXT_G:
                    settings.cTextG = Integer.parseInt(readTag(parser, name));
                    break;
                case Settings.TAG_TEXT_B:
                    settings.cTextB = Integer.parseInt(readTag(parser, name));
                    break;
                case Settings.TAG_BACK_R:
                    settings.cBackR = Integer.parseInt(readTag(parser, name));
                    break;
                case Settings.TAG_BACK_G:
                    settings.cBackG = Integer.parseInt(readTag(parser, name));
                    break;
                case Settings.TAG_BACK_B:
                    settings.cBackB = Integer.parseInt(readTag(parser, name));
                    break;
                case Settings.TAG_TEXT_SIZE:
                    settings.textSize = Integer.parseInt(readTag(parser, name));
                    break;
                case Settings.TAG_KG:
                    settings.kg = Boolean.parseBoolean(readTag(parser, name));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return settings;
    }

    // Read a simple xml tag
    private String readTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return summary;
    }

    // Read the text content
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

}
