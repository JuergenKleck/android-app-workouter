package email.kleck.android.workouter.business;

/**
 * Factory class which will return an instance of the transformer
 */
final class StorageFactory {

    /**
     * Create a storage processor based on the provided type
     *
     * @param storageType The enumeration to determine the processor
     * @return The processor instance or throws an exception
     */
    static StorageProcessor createProcessor(StorageType storageType) {
        if (StorageType.JSON == storageType) {
            return new JSONProcessor();
        } else if (StorageType.XML == storageType) {
            return new XmlProcessor();
        }
        throw new IllegalArgumentException("Storage type not implemented yet");
    }

}
