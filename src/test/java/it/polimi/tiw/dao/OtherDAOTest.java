package it.polimi.tiw.dao;

import it.polimi.tiw.BaseDB;
import it.polimi.tiw.beans.*;
import it.polimi.tiw.exceptions.*;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OtherDAOTest extends BaseDB {

    /*
     * 1. Register a new user
     * !. Register a second user
     * 2. Add an image
     * 2.2 get that image with wrong ID
     * 2.3 get that image
     * #. Add a second image
     * 3. Add an image with wrong user ID
     * 4. Add an album
     * @. Add a second album
     * 5. Add an album with wrong user ID
     * 6. Add that image to that album with wrong user ID
     * 7. Add that image to that album
     * $. Add other image to other album
     * 8. Get images of that album with negative page
     * 9. Get images of that album with too high page
     * 9.1 Get images of that album with wrong albumId
     * 9.2 Create an empty album and get its images
     * 9.3 get images from empty album with too high page
     * 10. Get first page of images of that album
     * 11. Create a comment with a non-existing user
     * 12. Create a comment with a non-existing image
     * 13. Create a comment
     * 14. Get comments from that image
     * 15. Get all albums from user 1
     * 16. Get all albums from user 2
     */
    @Test
    void bigTest() {
        final String username = "testuser";
        final String password = "testpassword";
        final String imgTitle = "Test Image Title";
        final String imgDesc = "Test Image Description";
        final String imgPath = "Test Image Path";
        final String albumTitle = "Test Album Title";
        final String commentText = "Hello this is a comment :)";

        final String otherUsername = "other";
        final String otherPassword = "other";
        final String otherAlbumTitle = "Other Album Title";
        final String otherImgTitle = "Other Image Title";
        final String otherImgDesc = "Other Image Description";
        final String otherImgPath = "Other Image Path";

        final String tempAlbumTitle = "Temp Album Title";

        int userId;
        try {
            // 1
            userId = userDAO.register(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }

        int otherUserId;
        try {
            otherUserId = userDAO.register(otherUsername, otherPassword);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }

        int imageId;

        try {
            // 2
            imageId = imageDAO.addImage(userId, imgTitle, imgDesc, imgPath);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }

        // 2.2
        assertThrows(ImageNotFoundException.class, () -> imageDAO.getImage(-1));

        // 2.3
        Image image;
        try {
            image = imageDAO.getImage(imageId);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }

        assertEquals(imageId, image.getImagePK());
        assertEquals(imgPath, image.getPath());
        assertEquals(imgTitle, image.getTitle());
        assertEquals(imgDesc, image.getDescription());
        assertEquals(userId, image.getUserFK());

        int otherImageId;
        try {
            // #
            otherImageId =
                    imageDAO.addImage(otherUserId, otherImgTitle, otherImgDesc, otherImgPath);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }

        // 3
        assertThrows(SQLException.class, () -> imageDAO.addImage(-1, imgTitle, imgDesc, imgPath));

        int albumId;

        try {
            // 4
            albumId = albumDAO.addNewAlbum(userId, albumTitle);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }

        // @
        int otherAlbumId;
        try {
            otherAlbumId = albumDAO.addNewAlbum(otherUserId, otherAlbumTitle);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }

        // 5
        assertThrows(SQLException.class, () -> albumDAO.addNewAlbum(-1, "cane"));

        // 6
        assertThrows(
                InvalidOperationException.class,
                () -> albumDAO.addImageToAlbum(-1, imageId, albumId));

        // 7
        assertDoesNotThrow(() -> albumDAO.addImageToAlbum(userId, imageId, albumId));

        // $
        assertDoesNotThrow(() -> albumDAO.addImageToAlbum(otherUserId, otherImageId, otherAlbumId));

        // 8
        assertThrows(SQLException.class, () -> albumDAO.getImages(albumId, -1));

        // 9
        assertThrows(PageOutOfBoundException.class, () -> albumDAO.getImages(albumId, 1));

        // 9.1
        assertThrows(AlbumNotFoundException.class, () -> albumDAO.getImages(-1, 1));

        // 9.2
        int tempAlbumId;
        try {
            tempAlbumId = albumDAO.addNewAlbum(userId, tempAlbumTitle);
        } catch (SQLException e) {
            e.printStackTrace();
            assert false;
            return;
        }

        assertDoesNotThrow(
                () -> {
                    AllImages tempAllImages = albumDAO.getImages(tempAlbumId, 0);
                    assertEquals(0, tempAllImages.getImages().size());
                    assertEquals(0, tempAllImages.getCurrentPage());
                    assertEquals(0, tempAllImages.getPageCount());
                });

        // 9.3
        assertThrows(PageOutOfBoundException.class, () -> albumDAO.getImages(tempAlbumId, 1));

        List<Image> images;

        // 10
        try {
            AllImages allImages = albumDAO.getImages(albumId, 0);

            assertEquals(0, allImages.getCurrentPage());
            assertEquals(1, allImages.getPageCount());

            images = allImages.getImages();
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }

        assertEquals(1, images.size());

        Image img = images.get(0);
        assertEquals(imgTitle, img.getTitle());
        assertEquals(imgDesc, img.getDescription());
        assertEquals(imgPath, img.getPath());
        assertEquals(userId, img.getUserFK());

        // 11
        assertThrows(
                UserNotFoundException.class, () -> commentDAO.addComment(-1, imageId, commentText));

        // 12
        assertThrows(SQLException.class, () -> commentDAO.addComment(userId, -1, commentText));

        // 13
        int commentId;

        try {
            commentId = commentDAO.addComment(userId, imageId, commentText);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }

        // 14
        List<Comment> comments;
        try {
            comments = commentDAO.getComments(imageId);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }

        assertEquals(1, comments.size());
        Comment comment = comments.get(0);
        assertEquals(username, comment.getUsername());
        assertEquals(imageId, comment.getImageFk());
        assertEquals(commentText, comment.getText());
        assertEquals(commentId, comment.getCommentPk());

        // 15
        AllAlbums allAlbums;
        try {
            allAlbums = albumDAO.getAllAlbums(userId);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }
        List<AlbumWithOwnerName> userAlbums = allAlbums.getUserAlbums();
        List<AlbumWithOwnerName> otherUserAlbums = allAlbums.getOtherUserAlbums();

        // AlbumId + testAlbumId
        assertEquals(2, userAlbums.size());
        assertEquals(1, otherUserAlbums.size());

        AlbumWithOwnerName userAlbum = userAlbums.get(0);
        assertEquals(albumId, userAlbum.getAlbumPk());
        assertEquals(userId, userAlbum.getUserFk());
        assertEquals(albumTitle, userAlbum.getTitle());
        assertEquals(username, userAlbum.getOwnerUsername());

        AlbumWithOwnerName otherUserAlbum = otherUserAlbums.get(0);
        assertEquals(otherAlbumId, otherUserAlbum.getAlbumPk());
        assertEquals(otherUserId, otherUserAlbum.getUserFk());
        assertEquals(otherAlbumTitle, otherUserAlbum.getTitle());
        assertEquals(otherUsername, otherUserAlbum.getOwnerUsername());

        // 16
        try {
            allAlbums = albumDAO.getAllAlbums(otherUserId);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }
        // These two are inverted so that the other assertions remains the same
        otherUserAlbums = allAlbums.getUserAlbums();
        userAlbums = allAlbums.getOtherUserAlbums();

        // AlbumId + testAlbumId
        assertEquals(2, userAlbums.size());
        assertEquals(1, otherUserAlbums.size());

        userAlbum = userAlbums.get(0);
        assertEquals(albumId, userAlbum.getAlbumPk());
        assertEquals(userId, userAlbum.getUserFk());
        assertEquals(albumTitle, userAlbum.getTitle());
        assertEquals(username, userAlbum.getOwnerUsername());

        otherUserAlbum = otherUserAlbums.get(0);
        assertEquals(otherAlbumId, otherUserAlbum.getAlbumPk());
        assertEquals(otherUserId, otherUserAlbum.getUserFk());
        assertEquals(otherAlbumTitle, otherUserAlbum.getTitle());
        assertEquals(otherUsername, otherUserAlbum.getOwnerUsername());
    }
}
