package com.ggez.pgtrackerapp.models;

import static android.R.attr.type;

/**
 * Created by katleen on 8/9/17.
 */
public class Food {

    String photoUrl;
    String name;

    public Food(){

    }

    public Food(String name, String photoUrl) {
        this.photoUrl = photoUrl;
        this.name = name;

    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
