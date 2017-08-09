package com.ggez.pgtrackerapp.modules.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ggez.pgtrackerapp.BaseActivity;
import com.ggez.pgtrackerapp.R;

public class MainActivity extends BaseActivity {
    private String TAG = "MainActivity";
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        changeFragment(new HomeFragment(), false);
    }

    /**
     * Change Fragment
     * clearStack true will clear the FragmentTransaction stack
     * @param fragment Fragment
     * @param clearStack boolean
     */
    public void changeFragment(Fragment fragment, boolean clearStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_fragment_container, fragment);
        if(clearStack) getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        else fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
