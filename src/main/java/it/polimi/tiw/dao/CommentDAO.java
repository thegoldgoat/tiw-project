package it.polimi.tiw.dao;

import it.polimi.tiw.beans.Comment;
import it.polimi.tiw.exceptions.UserNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO extends DAO {
    public CommentDAO(Connection connection) {
        super(connection);
    }

    /**
     * Add a new comment to an image
     * 
     * @param writerId user who wrote the comment
     * @param ImageFk image where the user is writing the comment
     * @param text body of the comment written by the user
     * @return CommentPk of the newly created comment
     * @throws SQLException SQL library internal exception
     * @throws UserNotFoundException writerId not found in the database
     */
    public int addComment(int writerId, int ImageFk, String text) throws SQLException, UserNotFoundException {
        String queryUsername = "SELECT username FROM User WHERE UserPk = ?";

        PreparedStatement pStatement = connection.prepareStatement(queryUsername);

        pStatement.setInt(1, writerId);

        ResultSet resultSet = pStatement.executeQuery();

        if (!resultSet.next()) {
            throw new UserNotFoundException();
        }

        String username = resultSet.getString("username");

        if (resultSet.next()) {
            throw new RuntimeException("How could there be 2 users with same username?");
        }

        String createCommentQuery = """
                    INSERT INTO Comment (text, username, ImageFk)
                    VALUE (?, ?, ?)
                """;
        pStatement = connection.prepareStatement(createCommentQuery, Statement.RETURN_GENERATED_KEYS);

        pStatement.setString(1, text);
        pStatement.setString(2, username);
        pStatement.setInt(3, ImageFk);

        pStatement.executeUpdate();

        ResultSet keys = pStatement.getGeneratedKeys();

        if (!keys.next()) {
            throw new RuntimeException("How could there be no CommentPk after creating a new comment?");
        }

        return keys.getInt(1);
    }

    /**
    * Get the comments for n image
    *
    * @param imageFk ID of the image
    * @return List of comments for this image
    * @throws SQLException SQL library internal exception
    */
    public List<Comment> getComments(int imageFk) throws SQLException {
        String query = "SELECT * FROM Comment WHERE ImageFk = ?";

        PreparedStatement pStatement = connection.prepareStatement(query);

        pStatement.setInt(1, imageFk);

        ResultSet resultSet = pStatement.executeQuery();

        List<Comment> comments = new ArrayList<>();
        while (resultSet.next()) {
            comments.add(new Comment(
                    resultSet.getInt("CommentPk"),
                    resultSet.getString("text"),
                    resultSet.getString("username"),
                    resultSet.getInt("ImageFk")

            ));
        }

        return comments;
    }
}
