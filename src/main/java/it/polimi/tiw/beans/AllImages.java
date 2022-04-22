package it.polimi.tiw.beans;

import lombok.Getter;

import java.util.List;

public class AllImages {
    @Getter private final List<Image> images;
    @Getter private final int pageCount;
    @Getter private final int currentPage;

    public AllImages(List<Image> images, int pageCount, int currentPage) {
        this.images = images;
        this.pageCount = pageCount;
        this.currentPage = currentPage;
    }
}
