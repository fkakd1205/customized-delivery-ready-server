package com.customized_delivery_ready.server.exception;

public class ExcelFileUploadException extends RuntimeException {
    public ExcelFileUploadException(String message) {
        super(message);
    }

    public ExcelFileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
