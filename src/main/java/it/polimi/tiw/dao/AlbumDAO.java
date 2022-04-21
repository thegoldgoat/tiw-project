package it.polimi.tiw.dao;

import it.polimi.tiw.beans.Album;
import it.polimi.tiw.beans.AllAlbums;
import it.polimi.tiw.beans.Image;
import it.polimi.tiw.exceptions.InvalidOperationException;
import it.polimi.tiw.exceptions.NoImageException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO extends DAO {
    private static final int PAGE_SIZE = 5;

    public AlbumDAO(Connection connection) {
        super(connection);
    }

    /**
     * Create a new album in the database
     *
     * @param UserFk user creator of the album
     * @param title  title of the album
     * @return AlbumPk of the newly generated album
     * @throws SQLException SQL library internal exception
     */
    public int addNewAlbum(int UserFk, String title) throws SQLException {
        String query = "INSERT INTO Album (title, date, UserFk) VALUES (?, ?, ?)";

        PreparedStatement pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        pStatement.setString(1, title);
        pStatement.setDate(2, new Date(System.currentTimeMillis()));
        pStatement.setInt(3, UserFk);

        pStatement.executeUpdate();

        ResultSet keys = pStatement.getGeneratedKeys();
        if (!keys.next()) {
            throw new RuntimeException("How could there be no auto generated key?");
        }

        return keys.getInt(1);
    }

    /**
     * Add image to album, both must be of the same user
     *
     * @param UserFk  ID of the owner of the image / album
     * @param ImagePk ID of the image
     * @param AlbumFk ID of the album
     * @throws SQLException              SQL library internal exception
     * @throws InvalidOperationException Image does not exist or album does ont
     *                                   exist, or they are owned by someone else
     */
    public void addImageToAlbum(int UserFk, int ImagePk, int AlbumFk) throws SQLException, InvalidOperationException {
        String checkOwnerShipQuery = """
                SELECT * FROM Album A, Image I
                WHERE A.AlbumPk = ? AND  I.ImagePk = ?
                      AND A.UserFk = ? AND A.UserFk = I.UserFk
                """;

        PreparedStatement checkOwner = connection.prepareStatement(checkOwnerShipQuery);
        checkOwner.setInt(1, AlbumFk);
        checkOwner.setInt(2, ImagePk);
        checkOwner.setInt(3, UserFk);

        if (!checkOwner.executeQuery().next()) {
            throw new InvalidOperationException(
                    "Image does not exist or album does ont exist or they are owned by someone else");
        }

        String queryAdd = """
                INSERT INTO ImageAlbum (ImageFk, AlbumFk)
                VALUES (?, ?)
                """;

        PreparedStatement pStatement = connection.prepareStatement(queryAdd);

        pStatement.setInt(1, AlbumFk);
        pStatement.setInt(2, ImagePk);

        pStatement.executeUpdate();
    }

    private List<Album> getAllAlbumsFromQuery(String query, int userId) throws SQLException {
        PreparedStatement pStatement = connection.prepareStatement(query);

        pStatement.setInt(1, userId);

        ResultSet resultSet = pStatement.executeQuery();

        List<Album> returnValues = new ArrayList<>();
        while (resultSet.next()) {
            returnValues.add(new Album(
                    resultSet.getInt("AlbumPk"),
                    resultSet.getString("title"),
                    resultSet.getDate("date"),
                    resultSet.getInt("UserFk")
            ));
        }

        return returnValues;
    }

    public AllAlbums getAllAlbums(int UserFk) throws SQLException {
        String queryMyAlbums = """
                    SELECT * FROM Album
                    WHERE UserFk = ?
                """;

        String queryOtherAlbums = """
                    SELECT * FROM Album
                    WHERE UserFk != ?
                """;
        List<Album> userAlbums = getAllAlbumsFromQuery(queryMyAlbums, UserFk);
        List<Album> otherUsersAlbums = getAllAlbumsFromQuery(queryOtherAlbums, UserFk);

        return new AllAlbums(userAlbums, otherUsersAlbums);
    }

    /**
     * Return at most 5 images inside AlbumFk album
     *
     * @param UserFk  ID of the owner of the album/Images
     * @param AlbumFk ID of the album
     * @param page    page of images, first page is 0
     * @return List of images inside that page of the album
     * @throws SQLException     SQL library internal exception
     * @throws NoImageException No images where found.
     *                          * Maybe AlbumFk is now owned by UserFk?
     *                          AlbumFk does not exist?
     */
    public List<Image> getImages(int UserFk, int AlbumFk, int page) throws SQLException, NoImageException {
        int countBegin = page * PAGE_SIZE;
        int countEnd = countBegin + PAGE_SIZE;

        String query = """
                SELECT * FROM Image I, ImageAlbum IA, Album A
                WHERE A.UserFk = ? AND A.AlbumPk = ?
                AND A.AlbumPk = IA.AlbumFk
                AND I.ImagePk = IA.ImageFk
                ORDER BY I.date DESC
                LIMIT ?,?
                """;

        PreparedStatement pStatement = connection.prepareStatement(query);

        pStatement.setInt(1, UserFk);
        pStatement.setInt(2, AlbumFk);
        pStatement.setInt(3, countBegin);
        pStatement.setInt(4, countEnd);

        ResultSet resultSet = pStatement.executeQuery();

        List<Image> images = new ArrayList<>(PAGE_SIZE);
        for (int i = 0; i < PAGE_SIZE; i++) {
            if (!resultSet.next()) {
                break;
            }
            images.add(new Image(
                    resultSet.getInt("ImagePk"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("path"),
                    resultSet.getDate("date"),
                    resultSet.getInt("UserFk"),
                    resultSet.getInt("AlbumFk")));
        }

        if (images.size() == 0) {
            throw new NoImageException();
        }

        return images;
    }
}
