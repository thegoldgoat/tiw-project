package it.polimi.tiw.beans;

import lombok.Getter;

import java.util.Date;

public class Album {
    @Getter
    final private int albumPK;

    @Getter
    final private String title;

    @Getter
    final private Date date;

    @Getter
    final private int userFk;

    public Album(int albumPK, String title, Date date, int userFk) {
        this.albumPK = albumPK;
        this.title = title;
        this.date = date;
        this.userFk = userFk;
    }
}
