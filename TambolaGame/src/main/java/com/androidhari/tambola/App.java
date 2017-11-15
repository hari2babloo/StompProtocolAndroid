package com.androidhari.tambola;


public class App {

    private int mDrawable;
    private String mName;
    private float mRating;
    private String mid;


    private String s;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }


    public App(String name, int drawable, float rating,String id) {
        mName = name;
        mDrawable = drawable;
        mRating = rating;
        mid=id;
    }


    public float getRating() {
        return mRating;
    }

    public int getDrawable() {
        return mDrawable;
    }

    public String getName() {
        return mName;
    }
    public String getId() {
        return mid;
    }
}

