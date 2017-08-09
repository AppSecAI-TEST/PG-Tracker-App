package com.ggez.pgtrackerapp.utils;

import android.content.Context;
import android.net.Uri;

import com.ggez.pgtrackerapp.R;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

/**
 * Created by omarmatthew on 6/25/17.
 * Class containing all necessary methods that uses Firebase Dynamic Links
 */

public class DynamicLinkHelper {
    private final String TAG = "DynamicLinkHelper";
    public DynamicLink.Builder dynamicLinkBuilder(Context context, String deepLink){
        return FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(deepLink))
                .setDynamicLinkDomain(context.getString(R.string.dynamic_link_domain))
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build());
    }
}
