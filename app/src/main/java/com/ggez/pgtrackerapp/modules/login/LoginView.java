package com.ggez.pgtrackerapp.modules.login;

import android.view.View;

/**
 * Created by Omar Matthew Reyes on 8/8/2017.
 */

interface LoginView {
    void showProgress(String msg);

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void clearTilErrors();

    void hideSoftInput(View view);
}
