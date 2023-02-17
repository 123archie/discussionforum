package com.hemant.askagain;

public class PostModel {
    String postId;
    String postedBy;
    String postedTime;
    String textQuestion;
    String imageQuestion;


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public PostModel() {
    }

    public String getImageQuestion() {
        return imageQuestion;
    }

    public void setImageQuestion(String imageQuestion) {
        this.imageQuestion = imageQuestion;
    }

     public String getPostedBy() {
         if (postedBy != null) {
             return postedBy;
         }
     return "0";}

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
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


}
