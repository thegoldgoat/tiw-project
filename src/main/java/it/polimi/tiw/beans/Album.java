package it.polimi.tiw.beans;

import lombok.Getter;

import java.util.Date;

public class Album {
    @Getter private final int albumPk;
    @Getter private final String title;
    @Getter private final Date date;
    @Getter private final int userFk;

    public Album(int albumPk, String title, Date date, int userFk) {
        this.albumPk = albumPk;
        this.title = title;
        this.date = date;
        this.userFk = userFk;
    }
}
