package it.polimi.tiw.exceptions;

public class InvalidEmailException extends Exception {
    public InvalidEmailException() {
        super("The format of the email is not valid");
    }
}
