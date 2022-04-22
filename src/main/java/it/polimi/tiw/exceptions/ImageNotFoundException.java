package it.polimi.tiw.exceptions;

public class ImageNotFoundException extends Exception {
    public ImageNotFoundException(int imageId) {
        super("Image with ID %d not found.".formatted(imageId));
    }
}
