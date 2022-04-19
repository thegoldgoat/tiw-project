package it.polimi.tiw.exceptions;

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException() {
        super("Invalid credentials. Username or password are wrong.");
    }
}
