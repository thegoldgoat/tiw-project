package it.polimi.tiw;

import it.polimi.tiw.dao.AlbumDAO;
import it.polimi.tiw.dao.ImageDAO;
import it.polimi.tiw.dao.UserDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class BaseDB {
    static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/dbfortests";
    static final String DB_USER = "testing";
    static final String DB_PASSWORD = "testing";

    protected Connection connection;
    protected UserDAO userDAO;
    protected AlbumDAO albumDAO;
    protected ImageDAO imageDAO;

    @BeforeAll
    static void prepareDB() {
        Connection prepareConnection;
        // Connect to DB
        assertDoesNotThrow(() -> Class.forName(DRIVER_NAME));

        try {
            prepareConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            assertNotNull(prepareConnection);
        } catch (SQLException e) {
            e.printStackTrace();
            assert false;
            return;
        }

        InputStream tablesFileStream = BaseDB.class.getClassLoader().getResourceAsStream("tables.sql");
        assertNotNull(tablesFileStream);
        String fileContent;
        try {
            fileContent = new String(tablesFileStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            assert false;
            return;
        }

        assertDoesNotThrow(
                () -> {
                    for (String line : fileContent.split(";")) {
                        Statement statement = prepareConnection.createStatement();
                        statement.execute(line);
                    }
                });
    }

    @BeforeEach
    void prepareConnection() {
        assertDoesNotThrow(() -> Class.forName(DRIVER_NAME));
        assertDoesNotThrow(
                () -> connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD));

        assertNotNull(connection);

        userDAO = new UserDAO(connection);
        albumDAO = new AlbumDAO(connection);
        imageDAO = new ImageDAO(connection);
    }
}
