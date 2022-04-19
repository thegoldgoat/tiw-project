package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

public class ImageDAO extends DAO {

    public ImageDAO(Connection connection) {
        super(connection);
    }

    /**
     * Add a new entry for an image in the database
     * 
     * @param UserFk      owner of the image
     * @param title       title of the image
     * @param description description of the image
     * @param path        path in the filesystem where the image file is stored
     * @return int ImagePk of the newly added image
     * @throws SQLException SQL library internal error
     */
    public int addImage(int UserFk, String title, String description, String path) throws SQLException {
        String query = """
                INSERT INTO Image (title, description, date, path, UserFk)
                VALUES (?, ?, ?, ?, ?)
                """;

        PreparedStatement pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        pStatement.setString(1, title);
        pStatement.setString(2, description);
        pStatement.setDate(3, (java.sql.Date) new Date(System.currentTimeMillis()));
        pStatement.setString(4, path);
        pStatement.setInt(5, UserFk);

        pStatement.executeUpdate();

        ResultSet keys = pStatement.getGeneratedKeys();

        if (!keys.next()) {
            throw new RuntimeException("No ImagePk when creating Image row. How?");
        }

        return keys.getInt(1);
    }
}
