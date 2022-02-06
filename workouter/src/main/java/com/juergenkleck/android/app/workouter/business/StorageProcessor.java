package com.juergenkleck.android.app.workouter.business;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.juergenkleck.android.app.workouter.datamodel.BaseItem;
import com.juergenkleck.android.app.workouter.datamodel.DataStorage;

/**
 * Interface for storage implementations
 *
 *
 * Android app - Workouter
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public interface StorageProcessor {

    /**
     * Retrieve a base item according to it's id
     *
     * @param list the list of base items
     * @param id   the id to lookup
     * @return the object or null
     */
    default BaseItem getBaseItemById(List<? extends BaseItem> list, Long id) {
        for (BaseItem item : list) {
            if (item.id.equals(id)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Write the data storage object into the output stream
     *
     * @param dataStorage  The storage object to transform
     * @param outputStream The output stream which will contain the data
     * @throws Throwable Thrown if an error occurs during the transformation
     */
    void write(DataStorage dataStorage, OutputStream outputStream) throws Throwable;

    /**
     * Read the data storage object from the input stream
     *
     * @param in The input stream which contains the data
     * @return The data storage object
     * @throws Throwable Thrown if an error occurs during the transformation
     */
    DataStorage parse(InputStream in) throws Throwable;

    /**
     * Get the file extension based on the processor implementation
     *
     * @return The file extension e.g. this will return '.xml'
     */
    String getFileExtension();

}
