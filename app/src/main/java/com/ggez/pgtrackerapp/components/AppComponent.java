package com.ggez.pgtrackerapp.components;

import com.ggez.pgtrackerapp.BaseActivity;
import com.ggez.pgtrackerapp.modules.home.MainActivity;
import com.ggez.pgtrackerapp.modules.login.LoginActivity;
import com.ggez.pgtrackerapp.modules.register.RegisterActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Omar Matthew Reyes on 8/8/2017.
 * App component
 */

@Singleton
@Component(modules = {PGTrackerModule.class})
public interface AppComponent {
    void inject(BaseActivity baseActivity);
    void inject(MainActivity mainActivity);
    void inject(LoginActivity loginActivity);
    void inject(RegisterActivity registerActivity);
}
