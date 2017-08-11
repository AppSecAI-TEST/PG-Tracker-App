package com.ggez.pgtrackerapp.modules.home;

import android.app.ProgressDialog;
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
import com.ggez.pgtrackerapp.utils.Constants;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;


public class MainActivity extends BaseActivity {
    private String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        mProgressDialog = new ProgressDialog(this);

        changeFragment(new HomeFragment(), true);
        FirebaseAnalytics.getInstance(this);
        processDeepLink(getIntent());
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

    public void showProgress(String msg) {
        mProgressDialog.setMessage(msg);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null && scanResult.getContents() != null) {
            String url = scanResult.getContents();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            Log.i(TAG, "getFormatName: " + scanResult.getFormatName() + " getContents: " + url);
        }
    }

    public void processDeepLink(Intent intent) {
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
                .addOnSuccessListener(this, data -> {
                    if (data == null) {
                        Log.d(TAG, "getInvitation: no data");
                        return;
                    }

                    // Get the deep link
                    // Format: https://pgtrackerapp.com/menu/{yyyymmdd}/{meal}
                    // yyyymmdd dataLink[4]
                    // meal dataLink[5]
                    Uri deepLink = data.getLink();
                    Log.i(TAG, "Deep Link: " + deepLink);
                    String url = deepLink.toString();
                    String[] urlData = url.split("/");
                    for(String dataLink : urlData) Log.i(TAG, "FBDL " + dataLink);
                    Toast.makeText(getApplicationContext(), "Deep Link received: " + deepLink, Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    Log.i(TAG, "Date: " + urlData[4] + ", Meal: " + urlData[5]);
                    bundle.putString(Constants.BUNDLE_DATE, urlData[4]);
                    bundle.putString(Constants.BUNDLE_MEAL, urlData[5]);
                    bundle.putString(Constants.BUNDLE_DEEPLINK, url);
                    Fragment foodPickerFragment = new FoodPickerFragment();
                    foodPickerFragment.setArguments(bundle);
                    changeFragment(foodPickerFragment, false); // go to menu for the day fragment

                    // Extract invite
                    FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(data);
                    if (invite != null) {
                        String invitationId = invite.getInvitationId();
                        Log.i(TAG, "Invitation ID: " + invitationId);
                        Toast.makeText(getApplicationContext(), "Invitation ID: " + invitationId, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, e -> {
//                    Toast.makeText(getApplicationContext(), "Dynamic Link failure: " + e, Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "getDynamicLink:onFailure", e);
                });
    }
}
