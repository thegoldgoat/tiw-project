package it.polimi.tiw.exceptions;

public class AlbumNotExistsException extends Exception {
    public AlbumNotExistsException(int albumId) {
        super("Album with ID = %d does not exists".formatted(albumId));
    }
}
