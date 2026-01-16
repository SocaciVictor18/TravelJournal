package org.example.traveljournalend.exception;

public class InvalidCredentialsException extends Throwable {
    public InvalidCredentialsException(String invalidEmailOrPassword) {
        super(invalidEmailOrPassword);
    }
}
