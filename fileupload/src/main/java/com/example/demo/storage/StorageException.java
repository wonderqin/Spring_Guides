package com.example.demo.storage;

/**
 * @ClassName StorageException
 * @Description TODO
 * @Author wonderQin
 * @Date 2019-05-11 11:51
 **/
public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
