package com.redbadger.martianrobots.io;

/** Raised when the input text does not follow the challenge's format or limits. */
public final class InputException extends RuntimeException {

    public InputException(String message) {
        super(message);
    }
}
