package com.example.demo.exception;

/**
 * @ClassName StorageFileNotFoundException
 * @Description TODO
 * @Author wonderQin
 * @Date 2019-05-10 1:11
 **/
public class StorageFileNotFoundException extends Throwable {
    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
