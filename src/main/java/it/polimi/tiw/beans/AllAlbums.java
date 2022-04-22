package it.polimi.tiw.beans;

import lombok.Getter;

import java.util.List;

public class AllAlbums {
    @Getter private final List<AlbumWithOwnerName> userAlbums;

    @Getter private final List<AlbumWithOwnerName> otherUserAlbums;

    public AllAlbums(
            List<AlbumWithOwnerName> userAlbums, List<AlbumWithOwnerName> otherUserAlbums) {
        this.userAlbums = userAlbums;
        this.otherUserAlbums = otherUserAlbums;
    }
}
