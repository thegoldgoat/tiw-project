package it.polimi.tiw.exceptions;

public class UsernameAlreadyUsed extends Exception {
    public UsernameAlreadyUsed(String usedUsername) {
        super("A user is already registered with username " + usedUsername);
    }
}
