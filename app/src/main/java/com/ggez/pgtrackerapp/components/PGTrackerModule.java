package com.ggez.pgtrackerapp.components;

import com.ggez.pgtrackerapp.AppController;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Omar Matthew on 8/8/2017.
 * PG Tracker app module
 */

@Module
public class PGTrackerModule {private final AppController app;
    public PGTrackerModule(AppController app) {
        this.app = app;
    }
    /**
     * Provide injectable FirebaseAuth instance
     * @return FirebaseAuth
     */
    @Provides
    @Singleton
    FirebaseAuth provideFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }
    /**
     * Provide injectable FirebaseAnalytics instance
     * @return FirebaseAnalytics
     */
    @Provides
    @Singleton
    FirebaseAnalytics provideFirebaseAnalytics(){
        return FirebaseAnalytics.getInstance(app);
    }
}
