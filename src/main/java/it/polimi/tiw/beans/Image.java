package it.polimi.tiw.beans;

import lombok.Getter;

import java.util.Date;

public class Image {
    @Getter private final int ImagePK;

    @Getter private final String title;
    @Getter private final String description;
    @Getter private final String path;
    @Getter private final Date date;

    @Getter private final int userFK;

    public Image(
            int imagePK, String title, String description, String path, Date date, int userFK) {
        ImagePK = imagePK;
        this.title = title;
        this.description = description;
        this.path = path;
        this.date = date;
        this.userFK = userFK;
    }
}
