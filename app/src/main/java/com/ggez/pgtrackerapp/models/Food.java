package com.ggez.pgtrackerapp.models;

import java.io.Serializable;

import static android.R.attr.type;

/**
 * Created by katleen on 8/9/17.
 */
public class Food implements Serializable{

    String photoUrl;
    String name;
    String id;

    public Food(){

    }

    public Food(String name, String photoUrl, String id) {
        this.photoUrl = photoUrl;
        this.name = name;
        this.id = id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
