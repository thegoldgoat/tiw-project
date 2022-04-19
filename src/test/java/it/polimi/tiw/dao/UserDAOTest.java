package it.polimi.tiw.dao;

import it.polimi.tiw.BaseDB;
import it.polimi.tiw.exceptions.InvalidCredentialsException;
import it.polimi.tiw.exceptions.UsernameAlreadyUsedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest extends BaseDB {

    /** 1. Login with not existing user 2. Register him 3. Login again 4. Register again */
    @Test
    public void login() {
        final String username = "andreatest";
        final String password = "andreatestpw";

        // 1
        assertThrowsExactly(
                InvalidCredentialsException.class, () -> userDAO.login(username, password));

        assertDoesNotThrow(
                () -> {
                    // 2
                    int newUserId = userDAO.register(username, password);

                    // 3
                    assertEquals(newUserId, userDAO.login(username, password));
                });

        assertThrowsExactly(
                UsernameAlreadyUsedException.class, () -> userDAO.register(username, password));
    }
}
