// @Getter private final List<Image> images;
// @Getter private final int pageCount;
// @Getter private final int currentPage;

// @Getter private final int ImagePK;

// @Getter private final String title;
// @Getter private final String description;
// @Getter private final String path;
// @Getter private final Date date;

// @Getter private final int userFK;
export type Image = {
    title: string
    description: string
    path: string
    date: Date
}

export type AllImages = {
    images: Image[]
    pageCount: number
    currentPage: number
}
