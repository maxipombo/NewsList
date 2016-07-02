package com.example.android.newslist;

/**
 * Created by mpombos on 2/7/16.
 */
public class Article {

    String webTitle;
    String webPublicationDate;
    String webUrl;
    String thumbnail;

    public Article(String webTitle, String webPublicationDate, String webUrl, String thumbnail) {
        this.webTitle = webTitle;
        this.webPublicationDate = webPublicationDate;
        this.webUrl = webUrl;
        this.thumbnail = thumbnail;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getThumbnail() {
        return thumbnail;
    }


}
