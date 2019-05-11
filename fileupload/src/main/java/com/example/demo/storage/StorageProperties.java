package com.example.demo.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName StorageProperties
 * @Description TODO
 * @Author wonderQin
 * @Date 2019-05-10 1:27
 **/
@ConfigurationProperties("storage")
public class StorageProperties {
    /**
     * Folder location for storing files
     */
    private String location = "fileupload/src/main/resources/static/upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
