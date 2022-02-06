package com.juergenkleck.android.app.workouter.business;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.juergenkleck.android.app.workouter.R;
import com.juergenkleck.android.app.workouter.datamodel.DataStorage;
import com.juergenkleck.android.app.workouter.datamodel.Settings;

/**
 * The data integrator acts as interface for reading and writing the data on the local file system.
 *
 *
 * Android app - Workouter
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public final class DataIntegrator {

    private static final String FILE_NAME = "workouter-data";

    public static DataStorage localAppStorage;

    /**
     * Write the data storage into a portable format
     *
     * @param context     the android context
     * @param dataStorage the data storage object to persist
     * @param path        the path to store the file into
     */
    public static void writeData(Context context, DataStorage dataStorage, String path) {

        StorageProcessor processor = StorageFactory.createProcessor(StorageType.JSON);
        FileOutputStream outputStream = null;
        try {
            if (path != null) {
                File file = new File(path + "/" + FILE_NAME + processor.getFileExtension());
                outputStream = new FileOutputStream(file);
                Toast.makeText(context, R.string.text_config_exported, Toast.LENGTH_SHORT).show();
            } else {
                outputStream = context.openFileOutput(FILE_NAME + processor.getFileExtension(), Context.MODE_PRIVATE);
            }
            processor.write(dataStorage, outputStream);
        } catch (Throwable e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Read the data storage object from the portable format
     *
     * @param context the android context
     * @param path    the path to read the file from
     * @return the data storage object from the file or a new instantiated object
     */
    public static DataStorage readData(Context context, String path) {
        DataStorage dataStorage;

        try {
            StorageProcessor processor = StorageFactory.createProcessor(StorageType.JSON);
            if (path != null) {
                File file = new File(path + "/" + FILE_NAME + processor.getFileExtension());
                dataStorage = processor.parse(new FileInputStream(file));
                Toast.makeText(context, R.string.text_config_restored, Toast.LENGTH_SHORT).show();
            } else {
                dataStorage = processor.parse(context.openFileInput(FILE_NAME + processor.getFileExtension()));
            }
        } catch (Throwable e) {
            dataStorage = readDataFromXML(context, path);
            // something went wrong here
            if (dataStorage == null) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                DataIntegrator.writeData(context, dataStorage, null);
                Toast.makeText(context, R.string.text_loaded_xml_backup, Toast.LENGTH_SHORT).show();
            }
        }

        if (dataStorage == null) {
            dataStorage = new DataStorage();
        }

        // set initial values here
        if (dataStorage.settings == null) {
            dataStorage.settings = new Settings();
            dataStorage.settings.cTextR = 52;
            dataStorage.settings.cTextG = 148;
            dataStorage.settings.cTextB = 250;
            dataStorage.settings.cBackR = 0;
            dataStorage.settings.cBackG = 0;
            dataStorage.settings.cBackB = 0;
            dataStorage.settings.textSize = 24;
        }

        return dataStorage;
    }

    /**
     * Read the data storage file from the xml format
     *
     * @param context the android context
     * @param path    the path to read the file from
     * @return the data storage object from the file or a new instantiated object
     */
    private static DataStorage readDataFromXML(Context context, String path) {
        DataStorage dataStorage = null;

        try {
            StorageProcessor processor = StorageFactory.createProcessor(StorageType.XML);
            if (path != null) {
                File file = new File(path + "/" + FILE_NAME + processor.getFileExtension());
                dataStorage = processor.parse(new FileInputStream(file));
                Toast.makeText(context, R.string.text_config_restored, Toast.LENGTH_SHORT).show();
            } else {
                dataStorage = processor.parse(context.openFileInput(FILE_NAME + processor.getFileExtension()));
            }
        } catch (Throwable ignored) {
            // something went wrong here
        }

        return dataStorage;
    }

}
