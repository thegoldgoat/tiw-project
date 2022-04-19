package it.polimi.tiw.exceptions;

public class UsernameAlreadyUsedException extends Exception {
    public UsernameAlreadyUsedException(String usedUsername) {
        super("A user is already registered with username " + usedUsername);
    }
}
