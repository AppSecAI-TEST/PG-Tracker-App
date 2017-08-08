package com.ggez.pgtrackerapp.utils;

/**
 * Created by Omar Matthew Reyes on 8/4/16.
 * Constant variables
 */
public class Constants {
    public static final String REGEX_EMAIL =  "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    /** Password expression. Password must be between 6 and 50 digits long and include at least one numeric digit. **/
    public static final String REGEX_PASSWORD = "^(?=.*\\d).{6,50}$";

    public static final int RC_SELECT_PHOTO = 1000;

    public static final String FBDB_USERS = "users";
    public static final String FBDB_USER_PROFILE = "user-profile";
    public static final String FBDB_RATING = "rating";
    public static final String FBDB_RATING_UP = "vote-up";
    public static final String FBDB_RATING_UP_DOWN = "vote-up-down";
    public static final String FBDB_RATING_DOWN = "vote-down";
}
