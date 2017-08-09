package com.ggez.pgtrackerapp.models;

/**
 * Created by katleen on 8/9/17.
 */
public class User {

    private String ldap;
    private String email;
    private String photoUrl;

    public User() {
    }

    public User(String ldap, String email, String photoUrl) {
        this.ldap = ldap;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public String getLdap() {
        return ldap;
    }

    public void setLdap(String ldap) {
        this.ldap = ldap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
