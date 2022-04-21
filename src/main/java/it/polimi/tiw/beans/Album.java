package it.polimi.tiw.beans;

import lombok.Getter;

import java.util.Date;

public class Album {
    @Getter private final int albumPK;

    private final String title;
    @Getter private final Date date;
    @Getter private final int userFk;

    public Album(int albumPK, String title, Date date, int userFk) {
        this.albumPK = albumPK;
        this.title = title;
        this.date = date;
        this.userFk = userFk;
    }

    public String getTitle() {
        return title;
    }
}
