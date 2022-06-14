package com.hemant.askagain;

public class comment {
    private String commentedBy;
    private String description;

    public comment() {
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        this.commentedBy = commentedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public comment(String commentedBy, String description) {
        this.commentedBy = commentedBy;
        this.description = description;
    }
}
