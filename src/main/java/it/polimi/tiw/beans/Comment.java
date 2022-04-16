package it.polimi.tiw.beans;

import lombok.Getter;

public class Comment {
    @Getter
    final private int commentPk;

    @Getter
    final private String text;

    @Getter
    final private String username;

    @Getter
    final private int imageFk;

    public Comment(int commentPk, String text, String username, int imageFk) {
        this.commentPk = commentPk;
        this.text = text;
        this.username = username;
        this.imageFk = imageFk;
    }
}
