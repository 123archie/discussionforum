package com.hemant.askagain;

public class CommentModel {
    private String commentedBy;
    private String textAnswer;
    private String imageAnswer;
    public CommentModel() {
    }
    public String getTextAnswer() {
        return textAnswer;
    }

    public void setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }

    public String getImageAnswer() {
        return imageAnswer;
    }

    public void setImageAnswer(String imageAnswer) {
        this.imageAnswer = imageAnswer;
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        this.commentedBy = commentedBy;
    }
}
