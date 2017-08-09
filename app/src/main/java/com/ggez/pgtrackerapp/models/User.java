package com.ggez.pgtrackerapp.models;

/**
 * Created by katleen on 8/9/17.
 * Modified by Omar Matthew Reyes
 */
public class User {

    private String ldap;
    private String email;
    private String photoUrl;
    private int employeeType;

    public User() {
    }

    public User(String ldap, String email, int employeeType, String photoUrl) {
        this.ldap = ldap;
        this.email = email;
        this.employeeType = employeeType;
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

    public int getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(int employeeType) {
        this.employeeType = employeeType;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
