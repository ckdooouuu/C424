package com.example.c424.bean;

public class ReqFeedback {
    private String bundle_id;
    private String country;
    private String titlesFeed;
    private String questions;
    private String userAddressMail;

    public ReqFeedback() {

    }

    public ReqFeedback(String bundle_id, String country, String titlesFeed, String questions, String userAddressMail) {
        this.bundle_id = bundle_id;
        this.country = country;
        this.titlesFeed = titlesFeed;
        this.questions = questions;
        this.userAddressMail = userAddressMail;
    }

    public String getBundle_id() {
        return bundle_id;
    }

    public void setBundle_id(String bundle_id) {
        this.bundle_id = bundle_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTitlesFeed() {
        return titlesFeed;
    }

    public void setTitlesFeed(String titlesFeed) {
        this.titlesFeed = titlesFeed;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getUserAddressMail() {
        return userAddressMail;
    }

    public void setUserAddressMail(String userAddressMail) {
        this.userAddressMail = userAddressMail;
    }

    @Override
    public String toString() {
        return "ReqFeedback{" +
                "bundle_id='" + bundle_id + '\'' +
                ", country='" + country + '\'' +
                ", titlesFeed='" + titlesFeed + '\'' +
                ", questions='" + questions + '\'' +
                ", userAddressMail='" + userAddressMail + '\'' +
                '}';
    }
}
