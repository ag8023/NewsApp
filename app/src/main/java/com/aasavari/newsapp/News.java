package com.aasavari.newsapp;

/**
 * Created by Aasavari on 3/2/2017.
 */

public class News {

    private String mNewsTitle;
    private String mNewsSection;
    private String mNewsAuthor;
    private String mNewsUrl;

    public News(String newsTitle, String newsSection, String newsAuthor, String newsUrl){
        mNewsTitle = newsTitle;
        mNewsSection = newsSection;
        mNewsAuthor = newsAuthor;
        mNewsUrl = newsUrl;
    }

    public String getNewsTitle() {
        return mNewsTitle;
    }

    public String getNewsSection() {
        return mNewsSection;
    }

    public String getNewsAuthor() {
        return mNewsAuthor;
    }

    public String getNewsUrl() {
        return mNewsUrl;
    }

    @Override
    public String toString() {
        return "News{" +
                "mNewsTitle='" + mNewsTitle + '\'' +
                ", mNewsSection='" + mNewsSection + '\'' +
                ", mNewsAuthor='" + mNewsAuthor + '\'' +
                ", mNewsUrl='" + mNewsUrl + '\'' +
                '}';
    }
}
