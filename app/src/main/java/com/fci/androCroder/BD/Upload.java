package com.fci.androCroder.BD;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String name;
    private String image;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Upload() {
        //empty constructor needed
    }

    public Upload(String mName, String mImageUrl, String name, String image,String date) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        this.mName = mName;
        this.mImageUrl = mImageUrl;
        this.name = name;
        this.image = image;
        this.date = date;

    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
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
}