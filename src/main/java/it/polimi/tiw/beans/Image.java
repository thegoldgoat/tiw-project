package it.polimi.tiw.beans;

import lombok.Getter;

import java.util.Date;

public class Image {
    @Getter
    final private int ImagePK;

    @Getter
    final private String title;
    @Getter
    final private String description;
    @Getter
    final private String path;
    @Getter
    final private Date date;

    @Getter
    final private int userFK;
    @Getter
    final private int albumFk;

    public Image(int imagePK, String title, String description, String path, Date date, int userFK, int albumFk) {
        ImagePK = imagePK;
        this.title = title;
        this.description = description;
        this.path = path;
        this.date = date;
        this.userFK = userFK;
        this.albumFk = albumFk;
    }
}
