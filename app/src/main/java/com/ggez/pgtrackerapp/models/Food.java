package com.ggez.pgtrackerapp.models;

/**
 * Created by katleen on 8/9/17.
 */
public class Food {

    String photoUrl;
    String name;
    String type;

    public Food(){

    }

    public Food(String photoUrl, String name, String type) {
        this.photoUrl = photoUrl;
        this.name = name;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
