package it.polimi.tiw.dao;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.exceptions.InvalidCredentialsException;
import it.polimi.tiw.exceptions.UsernameAlreadyUsedException;

import java.sql.*;

public class UserDAO extends DAO {
    public UserDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param username username of the user
     * @param password plaintext password of the user
     * @return In case of successful login, UserPk of the user
     * @throws SQLException SQL library internal exception
     * @throws InvalidCredentialsException user was not
     */
    public int login(String username, String password)
            throws SQLException, InvalidCredentialsException {
        String query = "SELECT UserPk FROM User WHERE username = ? AND password = ?";

        String hashedPassword = User.hashPassword(password);

        PreparedStatement pStatement = connection.prepareStatement(query);
        pStatement.setString(1, username);
        pStatement.setString(2, hashedPassword);

        ResultSet resultSet = pStatement.executeQuery();

        if (!resultSet.next()) {
            throw new InvalidCredentialsException();
        }
        int userId = resultSet.getInt("UserPk");

        if (resultSet.next()) {
            throw new RuntimeException("Why did we get two records while trying login?!");
        }

        return userId;
    }

    /**
     * @param username username of the user
     * @param password password of the user
     * @return UserPk of the new user, if registered correctly
     * @throws SQLException SQL library internal exception
     * @throws UsernameAlreadyUsedException There already exists an user with this username.
     */
    public int register(String username, String password)
            throws SQLException, UsernameAlreadyUsedException {
        String queryCheckUsername = "SELECT UserPk FROM User WHERE username = ?";

        PreparedStatement pStatement1 = connection.prepareStatement(queryCheckUsername);

        pStatement1.setString(1, username);

        ResultSet resultSet1 = pStatement1.executeQuery();

        if (resultSet1.next()) {
            throw new UsernameAlreadyUsedException(username);
        }

        String queryInsert = "INSERT INTO User (username, password) VALUES (?, ?)";

        String hashedPassword = User.hashPassword(password);

        PreparedStatement pStatement2 =
                connection.prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS);

        pStatement2.setString(1, username);
        pStatement2.setString(2, hashedPassword);

        pStatement2.executeUpdate();

        ResultSet resultSet2 = pStatement2.getGeneratedKeys();

        if (!resultSet2.next()) {
            throw new RuntimeException("Should always return auto generated keys?");
        }

        return resultSet2.getInt(1);
    }
}
