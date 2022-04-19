package it.polimi.tiw.dao;

import it.polimi.tiw.BaseDB;
import it.polimi.tiw.beans.Image;
import it.polimi.tiw.exceptions.InvalidOperationException;
import it.polimi.tiw.exceptions.NoImageException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OtherDAOTest extends BaseDB {

    /*
     * 1. Register a new user
     * 2. Add an image
     * 3. Add an image with wrong user ID
     * 4. Add an album
     * 5. Add an album with wrong user ID
     * 6. Add that image to that album with wrong user ID
     * 7. Add that image to that album
     * 8. Get images of that album with negative page
     * 9. Get images of that album with too high page
     * 10. Get first page of images of that album
     */
    @Test
    void bigTest() {
        final String username = "testuser";
        final String password = "testpassword";
        final String imgTitle = "Test Image Title";
        final String imgDesc = "Test Image Description";
        final String imgPath = "Test Image Description";
        final String albumTitle = "Test Album Title";

        int userId;
        try {
            // 1
            userId = userDAO.register(username, password);
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

        // 5
        assertThrows(SQLException.class, () -> albumDAO.addNewAlbum(-1, "cane"));

        // 6
        assertThrows(
                InvalidOperationException.class,
                () -> albumDAO.addImageToAlbum(-1, imageId, albumId));

        // 7
        assertDoesNotThrow(() -> albumDAO.addImageToAlbum(userId, imageId, albumId));

        // 8
        assertThrows(SQLException.class, () -> albumDAO.getImages(userId, albumId, -1));

        // 9
        assertThrows(NoImageException.class, () -> albumDAO.getImages(userId, albumId, 1));

        List<Image> images;

        // 10
        try {
            images = albumDAO.getImages(userId, albumId, 0);
        } catch (SQLException | NoImageException e) {
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
    }
}
