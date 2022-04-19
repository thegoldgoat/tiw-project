DROP TABLE IF EXISTS `Comment`;
DROP TABLE IF EXISTS `ImageAlbum`;
DROP TABLE IF EXISTS `Album`;
DROP TABLE IF EXISTS `Image`;
DROP TABLE IF EXISTS `User`;

CREATE TABLE `User`
(
    `UserPk`   int PRIMARY KEY AUTO_INCREMENT,
    `username` varchar(100) NOT NULL,
    `password` varchar(64)  NOT NULL
);


CREATE TABLE `Album`
(
    `AlbumPk` int PRIMARY KEY AUTO_INCREMENT,
    `title`   varchar(50) NOT NULL,
    `date`    date        NOT NULL,
    `UserFk`  int,
    CONSTRAINT `AlbumOwner` FOREIGN KEY (`UserFk`) REFERENCES `User` (`UserPk`)
);


CREATE TABLE `Image`
(
    `ImagePk`     int PRIMARY KEY AUTO_INCREMENT,
    `title`       varchar(50)  NOT NULL,
    `description` varchar(200) NOT NULL,
    `date`        date         NOT NULL,
    `path`        varchar(200) NOT NULL,
    `UserFk`      int,
    CONSTRAINT `ImageOwner` FOREIGN KEY (`UserFk`) REFERENCES `User` (`UserPk`)
);

CREATE TABLE `ImageAlbum`
(
    `ImageFk` int,
    `AlbumFk` int,
    PRIMARY KEY (`ImageFk`, `AlbumFk`),
    CONSTRAINT `AlbumImage` FOREIGN KEY (`AlbumFk`) REFERENCES `Album` (`AlbumPk`),
    CONSTRAINT `ImageAlbum` FOREIGN KEY (`ImageFk`) REFERENCES `Image` (`ImagePk`)
);

CREATE TABLE `Comment`
(
    `CommentPk` int PRIMARY KEY AUTO_INCREMENT,
    `text`      varchar(1000) NOT NULL,
    `username`  varchar(100)  NOT NULL,
    `ImageFk`   int,
    CONSTRAINT `CommentImage` FOREIGN KEY (`imageFk`) REFERENCES `Image` (`ImagePk`)
);