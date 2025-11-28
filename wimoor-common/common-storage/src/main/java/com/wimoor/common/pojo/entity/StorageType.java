package com.wimoor.common.pojo.entity;

public enum StorageType {
    FTP("ftp", "ftp"),
    OSS("oss", "oss"),
    MinIO("minio", "minio");

    private String key;
    private String value;

    StorageType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValueByKey(String key) {
        for (StorageType e : StorageType.values()) {
            if (e.key.equals(key)) {
                return e.value;
            }
        }
        throw new IllegalArgumentException("Invalid key: " + key);
    }
}
