package com.juergenkleck.android.app.workouter.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StorageFactoryTest {

    @Test
    void createProcessor_JSON() {
        Assertions.assertEquals(JSONProcessor.class, StorageFactory.createProcessor(StorageType.JSON).getClass());
    }

    @Test
    void createProcessor_XML() {
        Assertions.assertEquals(XmlProcessor.class, StorageFactory.createProcessor(StorageType.XML).getClass());
    }

    @Test
    void createProcessor_Exception() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> StorageFactory.createProcessor(null));
    }
}