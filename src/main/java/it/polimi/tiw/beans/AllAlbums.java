package it.polimi.tiw.beans;

import lombok.Getter;

import java.util.List;

public class AllAlbums {
    @Getter private final List<Album> userAlbums;

    @Getter private final List<Album> otherUserAlbums;

    public AllAlbums(List<Album> userAlbums, List<Album> otherUserAlbums) {
        this.userAlbums = userAlbums;
        this.otherUserAlbums = otherUserAlbums;
    }
}
