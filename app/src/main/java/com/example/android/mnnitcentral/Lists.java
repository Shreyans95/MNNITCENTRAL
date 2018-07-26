package com.example.android.mnnitcentral;

/**
 * Created by LENOVO on 4/21/2018.
 */

public class Lists {
    private String details;
    private String name;
    private String image;

    public Lists(){}
    public Lists(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Lists(String name, String image) {

        this.name = name;
        this.image = image;
    }
}
