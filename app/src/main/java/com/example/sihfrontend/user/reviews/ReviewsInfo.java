package com.example.sihfrontend.user.reviews;

public class ReviewsInfo {
    String visitorName;
    float rating;
    String dateOfVisit;
    String review;

    public ReviewsInfo(String visitorName, float rating, String dateOfVisit, String review) {
        this.visitorName = visitorName;
        this.rating = rating;
        this.dateOfVisit = dateOfVisit;
        this.review = review;
    }

    public ReviewsInfo() {
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(String dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
