package com.ggez.pgtrackerapp.modules.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ggez.pgtrackerapp.AppController;
import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.modules.QRActivity;
import com.ggez.pgtrackerapp.modules.home.MainActivity;
import com.ggez.pgtrackerapp.modules.register.RegisterActivity;
import com.ggez.pgtrackerapp.qr.decoder.IntentIntegrator;
import com.ggez.pgtrackerapp.qr.decoder.IntentResult;
import com.ggez.pgtrackerapp.utils.Validator;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Omar Matthew Reyes on 8/8/2017.
 */

public class LoginActivity extends AppCompatActivity implements LoginView {
    private final String TAG = "LoginActivity";
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    private ProgressDialog mProgressDialog;

    @Inject
    public FirebaseAuth mFirebaseAuth;
    @Inject
    public FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_login);
        ButterKnife.bind(LoginActivity.this);
        AppController.getComponent(this).inject(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mProgressDialog = new ProgressDialog(this);

    }

    @Override
    public void showProgress(String msg) {
        mProgressDialog.setMessage(msg);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void setUsernameError() {
        tilEmail.setError(getString(R.string.error_invalid));

    }

    @Override
    public void setPasswordError() {
        tilPassword.setError(getString(R.string.error_invalid));
    }

    @Override
    public void clearTilErrors() {
        tilEmail.setErrorEnabled(false);
        tilEmail.setError(null);
        tilPassword.setErrorEnabled(false);
        tilPassword.setError(null);
    }

    @Override
    public void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @OnClick(R.id.btn_login)
    void onClickLogin() {
//        startActivity(new Intent(this, QRActivity.class));
        hideSoftInput(btnLogin);
        clearTilErrors();
        boolean error = false;
        if (!Validator.isEmailValid(etEmail.getText().toString())) {
            error = true;
            setUsernameError();
        }
        if (!Validator.isPasswordValid(etPassword.getText().toString())) {
            error = true;
            setPasswordError();
        }
        if (!error) {
            FirebaseAuth.getInstance().signOut();   // sign-out anonymous user
            showProgress(getString(R.string.logging_in));
            mFirebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        hideProgress();
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            FirebaseAuth.getInstance().signInAnonymously();   // sign-in failed. sign-in as anonymous user again
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Bundle eventBundle = new Bundle();
                            eventBundle.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, "password_auth");
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, eventBundle);
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        }
                    });
        }
    }

    @OnClick(R.id.btn_register)
    void onClickRegister() {
        clearTilErrors();
        startActivity(new Intent(this, RegisterActivity.class));
    }


}
