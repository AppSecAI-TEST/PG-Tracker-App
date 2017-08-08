package com.ggez.pgtrackerapp.modules.register;

/**
 * Created by Omar Matthew Reyes on 8/9/2017.
 */

interface RegisterView {
    void showProgress(String msg);

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void setPasswordConfirmError();

    void clearTilErrors();

}
