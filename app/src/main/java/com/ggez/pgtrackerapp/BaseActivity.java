package com.ggez.pgtrackerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ggez.pgtrackerapp.modules.QRActivity;
import com.ggez.pgtrackerapp.modules.login.LoginActivity;
import com.ggez.pgtrackerapp.modules.register.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

public class BaseActivity extends AppCompatActivity {
    private String TAG = "BaseActivity";
    @Inject
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();
        AppController.getComponent(this).inject(this);
        Log.i(TAG, "onStart");
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
            finish();
            startActivity(new Intent(this, LoginActivity.class));
//            startActivity(new Intent(this, QRActivity.class));
        }
//        FirebaseAuth.AuthStateListener mAuthListener = firebaseAuth -> {
//            FirebaseUser user = firebaseAuth.getCurrentUser();
//            if (user != null) {
//                // User is signed in
//                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//            } else {
//                // User is signed out
//                Log.d(TAG, "onAuthStateChanged:signed_out");
//                startActivity(new Intent(this, LoginActivity.class));
//            }
//        };
    }
}
