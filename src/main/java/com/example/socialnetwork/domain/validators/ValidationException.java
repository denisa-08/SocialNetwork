package com.example.socialnetwork.domain.validators;

/**
 * Class that indicates conditions that a reasonable application might want to catch.
 */
public class ValidationException extends RuntimeException {
    /**
     * Constructs a new ValidationException with no detail message
     */
    public ValidationException() {
    }

    /**
     * Constructs a new ValidationException with the specified detail message
     * @param message the detail message for the validation
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ValidationException with the specified detail message and cause
     * @param message the detail message for the validation
     * @param cause the cause of the validation
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ValidationException with the specified cause
     * @param cause the cause of the validation
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new ValidationException with the specified detail message, cause,
     * suppression enabled or disabled and writable stack trace enabled or disabled.
     * @param message the detail message for the validation
     * @param cause the cause of the validation
     * @param enableSuppression whether suppression is enabled or disabled
     * @param writableStackTrace whether the stack trace should be writable
     */
    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
