package org.example;

//пользовательское исключение
public class InvalidInputFormatException extends Exception {

    public InvalidInputFormatException() {
        super();
    }

    public InvalidInputFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputFormatException(String message) {
        super(message);
    }

    public InvalidInputFormatException(Throwable cause) {
        super(cause);
    }
}
