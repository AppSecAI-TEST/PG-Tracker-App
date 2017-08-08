package com.ggez.pgtrackerapp;

import android.app.Application;
import android.content.Context;

import com.ggez.pgtrackerapp.components.AppComponent;
import com.ggez.pgtrackerapp.components.DaggerAppComponent;
import com.ggez.pgtrackerapp.components.PGTrackerModule;

/**
 * Created by Omar Matthew Reyes on 8/8/2017.
 * PG Tracker application controller
 */

public class AppController extends Application {
    private static AppController sInstance;
    private AppComponent appComponent;

    /**
     * Get application instance
     *
     * @return AppController
     */
    public static AppController getsInstance() {
        return sInstance;
    }

    public static AppComponent getComponent(Context context) {
        return ((AppController) context.getApplicationContext()).appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        // Initialize Dagger AppComponent
        appComponent = DaggerAppComponent.builder()
                .pGTrackerModule(new PGTrackerModule(this)).build();
    }
}
