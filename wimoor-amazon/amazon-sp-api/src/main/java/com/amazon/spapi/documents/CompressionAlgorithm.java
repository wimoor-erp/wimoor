package com.amazon.spapi.documents;

/**
 * The compression algorithm.
 */
public enum CompressionAlgorithm {
    GZIP;

    /**
     * Convert from any equivalent enum value. If the specified enum value is null, return null.
     *
     * @param val The equivalent enum value to convert
     * @return This enum's equivalent to the specified enum value
     */
    public static <T extends Enum> CompressionAlgorithm fromEquivalent(T val) {
        if (val != null) {
            return CompressionAlgorithm.valueOf(val.toString());
        }

        return null;
    }

    /**
     * Convert from a string. If the specified string is null, return null.
     *
     * @param val The value to convert.
     * @return This enum's equivalent to the specified string
     */
    public static CompressionAlgorithm fromEquivalent(String val) {
        if (val != null) {
            return CompressionAlgorithm.valueOf(val);
        }

        return null;
    }
}
