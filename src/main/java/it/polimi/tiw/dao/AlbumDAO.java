package it.polimi.tiw.dao;

import it.polimi.tiw.beans.AlbumWithOwnerName;
import it.polimi.tiw.beans.AllAlbums;
import it.polimi.tiw.beans.AllImages;
import it.polimi.tiw.beans.Image;
import it.polimi.tiw.exceptions.AlbumNotFoundException;
import it.polimi.tiw.exceptions.InvalidOperationException;
import it.polimi.tiw.exceptions.PageOutOfBoundException;

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

    private List<AlbumWithOwnerName> getAllAlbumsFromQuery(String query, int userId) throws SQLException {
        PreparedStatement pStatement = connection.prepareStatement(query);

        pStatement.setInt(1, userId);

        ResultSet resultSet = pStatement.executeQuery();

        List<AlbumWithOwnerName> returnValues = new ArrayList<>();
        while (resultSet.next()) {
            returnValues.add(new AlbumWithOwnerName(
                    resultSet.getInt("AlbumPk"),
                    resultSet.getString("title"),
                    resultSet.getDate("date"),
                    resultSet.getInt("UserFk"),
                    resultSet.getString("username")
            ));
        }

        return returnValues;
    }

    public AllAlbums getAllAlbums(int UserFk) throws SQLException {
        String queryMyAlbums = """
                    SELECT A.*, U.username FROM Album A, User U
                    WHERE UserFk = ? AND A.UserFk = U.UserPk
                    ORDER BY date DESC
                """;

        String queryOtherAlbums = """
                    SELECT A.*, U.username FROM Album A, User U
                    WHERE A.UserFk != ? AND A.UserFk = U.UserPk
                    ORDER BY date DESC
                """;
        List<AlbumWithOwnerName> userAlbums = getAllAlbumsFromQuery(queryMyAlbums, UserFk);
        List<AlbumWithOwnerName> otherUsersAlbums = getAllAlbumsFromQuery(queryOtherAlbums, UserFk);

        return new AllAlbums(userAlbums, otherUsersAlbums);
    }

    private boolean checkAlbumExists(int AlbumFk) throws SQLException {
        String checkAlbumExistQuery = """
                SELECT * FROM Album
                WHERE AlbumPk = ?
                """;

        PreparedStatement checkStatement = connection.prepareStatement(checkAlbumExistQuery);
        checkStatement.setInt(1, AlbumFk);
        ResultSet checkResultSet = checkStatement.executeQuery();

        return checkResultSet.next();
    }

    /**
     * Return at most 5 images inside AlbumFk album
     *
     * @param AlbumFk ID of the album
     * @param page    page of images, first page is 0
     * @return List of images inside that page of the album
     * @throws SQLException     SQL library internal exception
     * @throws AlbumNotFoundException Album does not exist
     */
    public AllImages getImages(int AlbumFk, int page) throws SQLException, AlbumNotFoundException, PageOutOfBoundException {
        if (page < 0) {
            throw new PageOutOfBoundException();
        }

        int countBegin = page * PAGE_SIZE;
        int countEnd = countBegin + PAGE_SIZE;

        if (!checkAlbumExists(AlbumFk)) {
            throw new AlbumNotFoundException(AlbumFk);
        }

        String imageCountQuery = """
                SELECT Count(*) AS ImageCount From ImageAlbum
                WHERE AlbumFk = ?
                """;

        PreparedStatement imageCountStatement = connection.prepareStatement(imageCountQuery);
        imageCountStatement.setInt(1, AlbumFk);

        ResultSet imageCountResult = imageCountStatement.executeQuery();

        if (!imageCountResult.next()) {
            throw new RuntimeException("Why we are not able to count?");
        }

        int imageCount = imageCountResult.getInt("ImageCount");

        if (imageCount == 0) {
            if (page == 0) {
                return new AllImages(List.of(), 0, 0);
            } else {
                throw new PageOutOfBoundException();
            }
        }
        int pageCount = (int) Math.ceil((float) imageCount / PAGE_SIZE);

        // Page is 0-indexed, so if page == pageCount we are viewing an empty page
        if (page >= pageCount) {
            throw new PageOutOfBoundException();
        }

        String query = """
                SELECT * FROM Image I, ImageAlbum IA, Album A
                WHERE A.AlbumPk = ?
                AND A.AlbumPk = IA.AlbumFk
                AND I.ImagePk = IA.ImageFk
                ORDER BY I.date DESC
                LIMIT ?,?
                """;
        PreparedStatement pStatement = connection.prepareStatement(query);

        pStatement.setInt(1, AlbumFk);
        pStatement.setInt(2, countBegin);
        pStatement.setInt(3, countEnd);

        List<Image> images = getListImagesFromQuery(pStatement.executeQuery());

        return new AllImages(images, pageCount, page);
    }

    private List<Image> getListImagesFromQuery(ResultSet resultSet) throws SQLException {
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
                    resultSet.getInt("UserFk")));
        }

        return images;
    }

    /**
     * Return all images inside AlbumFk album
     *
     * @param AlbumFk ID of the album
     * @return List of images inside that page of the album
     * @throws SQLException     SQL library internal exception
     * @throws AlbumNotFoundException Album does not exist
     */
    public List<Image> getAllImages(int AlbumFk) throws SQLException, AlbumNotFoundException {


        if (!checkAlbumExists(AlbumFk)) {
            throw new AlbumNotFoundException(AlbumFk);
        }
        String query = """
                SELECT * FROM Image I, ImageAlbum IA, Album A
                WHERE A.AlbumPk = ?
                AND A.AlbumPk = IA.AlbumFk
                AND I.ImagePk = IA.ImageFk
                ORDER BY I.date DESC
                """;
        PreparedStatement pStatement = connection.prepareStatement(query);

        pStatement.setInt(1, AlbumFk);

        return getListImagesFromQuery(pStatement.executeQuery());
    }
}
