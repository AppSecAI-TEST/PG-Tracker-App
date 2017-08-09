package com.ggez.pgtrackerapp.modules.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.ggez.pgtrackerapp.BaseActivity;
import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.qr.decoder.IntentIntegrator;
import com.ggez.pgtrackerapp.qr.decoder.IntentResult;

import java.util.regex.Pattern;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String url = scanResult.getContents();
            Pattern pattern = Pattern.compile("http://([a-z0-9]*.)example.com");
            if(!pattern.matcher(url).matches()) Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
            else startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            Log.i(TAG, "getFormatName: " + scanResult.getFormatName() + " getContents: " + url);
        }
    }
}
