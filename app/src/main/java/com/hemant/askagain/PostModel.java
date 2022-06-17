package com.hemant.askagain;

public class PostModel {
    String postedByProfilePic;
    String postedByName;
    String postedTime;
    String textQuestion;

    public PostModel() {
    }

    String like;

    public PostModel(String postedByProfilePic, String postedByName, String postedTime, String textQuestion, String like) {
        this.postedByProfilePic = postedByProfilePic;
        this.postedByName = postedByName;
        this.postedTime = postedTime;
        this.textQuestion = textQuestion;
        this.like = like;
    }

    public String getPostedByProfilePic() {
        return postedByProfilePic;
    }

    public void setPostedByProfilePic(String postedByProfilePic) {
        this.postedByProfilePic = postedByProfilePic;
    }

    public String getPostedByName() {
        return postedByName;
    }

    public void setPostedByName(String postedByName) {
        this.postedByName = postedByName;
    }

    public String getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(String postedTime) {
        this.postedTime = postedTime;
    }

    public String getTextQuestion() {
        return textQuestion;
    }

    public void setTextQuestion(String textQuestion) {
        this.textQuestion = textQuestion;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }
}
