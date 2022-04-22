package it.polimi.tiw.beans;

import lombok.Getter;

import java.util.Date;

public class AlbumWithOwnerName extends Album {
    @Getter private final String ownerUsername;

    public AlbumWithOwnerName(
            int albumPk, String title, Date date, int userFk, String ownerUsername) {
        super(albumPk, title, date, userFk);

        this.ownerUsername = ownerUsername;
    }
}
