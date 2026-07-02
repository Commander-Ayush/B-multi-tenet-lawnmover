package com.growthmul.app.lawnmover_fs.dto;

public class ReviewSubmitRequest {
    private String reviewerName;
    private String reviewerCity;
    private int stars;
    private String text;

    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }
    public String getReviewerCity() { return reviewerCity; }
    public void setReviewerCity(String reviewerCity) { this.reviewerCity = reviewerCity; }
    public int getStars() { return stars; }
    public void setStars(int stars) { this.stars = stars; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
