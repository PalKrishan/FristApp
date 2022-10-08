package com.app.awaaz.Models;

public class CommentModel {
    String id;
    String user_pic;
    String username;
    String comment;

    public CommentModel(String id, String user_pic, String username, String comment) {
        this.id = id;
        this.user_pic = user_pic;
        this.username = username;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }
}
