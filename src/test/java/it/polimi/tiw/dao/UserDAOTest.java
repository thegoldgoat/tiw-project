package it.polimi.tiw.dao;

import it.polimi.tiw.BaseDB;
import it.polimi.tiw.exceptions.InvalidCredentialsException;
import it.polimi.tiw.exceptions.InvalidEmailException;
import it.polimi.tiw.exceptions.UsernameAlreadyUsedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest extends BaseDB {

    /*
     * 1.  Login with not existing user
     * 2.1 Register him with bad email
     * 2.2 Register him with good email
     * 3.  Login again
     * 4.  Register again
     */
    @Test
    public void login() {
        final String email = "andrea@example.net";
        final String username = "andrea";
        final String password = "andrea";

        // 1
        assertThrowsExactly(
                InvalidCredentialsException.class, () -> userDAO.login(username, password));

        // 2.1
        assertThrowsExactly(
                InvalidEmailException.class,
                () -> userDAO.register("andrea.somaini.dev", username, password));

        assertDoesNotThrow(
                () -> {
                    // 2.2
                    int newUserId = userDAO.register(email, username, password);

                    // 3
                    assertEquals(newUserId, userDAO.login(username, password));
                });

        assertThrowsExactly(
                UsernameAlreadyUsedException.class,
                () -> userDAO.register(email, username, password));
    }
}
