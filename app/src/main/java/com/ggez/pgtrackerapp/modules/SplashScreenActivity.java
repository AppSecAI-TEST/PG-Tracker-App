package com.ggez.pgtrackerapp.modules;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.modules.home.MainActivity;
import com.ggez.pgtrackerapp.modules.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by katleen on 8/10/17.
 */
public class SplashScreenActivity extends Activity {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.layout_activity_splash);

        //Utils.fetchConfig();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}