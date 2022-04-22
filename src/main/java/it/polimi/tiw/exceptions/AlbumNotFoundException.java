package it.polimi.tiw.exceptions;

public class AlbumNotFoundException extends Exception {
    public AlbumNotFoundException(int albumId) {
        super("Album with ID = %d does not exists".formatted(albumId));
    }
}
