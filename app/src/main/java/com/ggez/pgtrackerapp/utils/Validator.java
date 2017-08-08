package com.ggez.pgtrackerapp.utils;

import android.text.TextUtils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.regex.Pattern;

/**
 * Created by Omar Matthew Reyes on 8/4/16.
 * Validator
 */
public class Validator {
    /**
     * Check if email is valid
     * @param email String
     * @return boolean
     */
    public static boolean isEmailValid(String email){
        Pattern patternEmail = Pattern.compile(Constants.REGEX_EMAIL);
        return !TextUtils.isEmpty(email) && patternEmail.matcher(email).matches();
    }

    /**
     * Check if password is valid
     * @param password String
     * @return boolean
     */
    public static boolean isPasswordValid(String password){
        Pattern patternPassword = Pattern.compile(Constants.REGEX_PASSWORD);
        return !TextUtils.isEmpty(password) && patternPassword.matcher(password).matches();
    }

    /**
     * Check if password and password confirmation is valid and equals
     * @param pass1 String
     * @param pass2 String
     * @return boolean
     */
    public static boolean isPasswordConfirmed(String pass1, String pass2){
        return isPasswordValid(pass1) && isPasswordValid(pass2) && pass1.equals(pass2);
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    /**
     * Get error message for Firebase Task results
     * @return String
     */
    public static String getAuthErrorMessage(Task task){
        String error;
        if(task.getException() instanceof FirebaseAuthUserCollisionException) error = "The email address is already in use by another account.";
        else if(task.getException() instanceof FirebaseAuthWeakPasswordException) error = "Weak password. Retry.";
        else if(task.getException() instanceof FirebaseAuthInvalidUserException) error = "Invalid user.";
        else if(task.getException() instanceof FirebaseNetworkException) error = "Please check your network settings.";
        else error = "Unknown Error.";
        return error;
    }
}
