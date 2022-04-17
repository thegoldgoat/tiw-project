package it.polimi.tiw.exceptions;

public class InvalidCredentials extends Exception{
    public InvalidCredentials() {
        super("Invalid credentials. Username or password are wrong.");
    }
}
