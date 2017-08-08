package com.ggez.pgtrackerapp.components;

import com.ggez.pgtrackerapp.modules.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Omar Matthew Reyes on 8/8/2017.
 * App component
 */

@Singleton
@Component(modules = {PGTrackerModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
}
