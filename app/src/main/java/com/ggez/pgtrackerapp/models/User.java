package com.ggez.pgtrackerapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by katleen on 8/9/17.
 * Modified by Omar Matthew Reyes
 */
public class User implements Parcelable{

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

    public User(Parcel read) {
        this.ldap = read.readString();
        this.email = read.readString();
        this.employeeType = read.readInt();
        this.photoUrl = read.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel write, int flags) {
        write.writeString(this.ldap);
        write.writeString(this.email);
        write.writeInt(this.employeeType);
        write.writeString(this.photoUrl);
    }

    public static final Parcelable.Creator<User> CREATOR =
            new Parcelable.Creator<User>() {
                @Override
                public User createFromParcel(Parcel source) {
                    return new User(source);
                }
                @Override
                public User[] newArray(int size) {
                    return new User[size];
                }
            };
}
