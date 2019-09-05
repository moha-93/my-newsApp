package com.moha.nytimesapp.utility;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.content.ContextCompat;

import com.moha.nytimesapp.R;

import org.chromium.customtabsclient.CustomTabsActivityHelper;
import org.chromium.customtabsclient.CustomTabsHelper;

public class ChromeUtils {

    private static CustomTabsClient mClient;
    private static CustomTabsSession mSession;

    public static void launchChromeTabs(final String url, final Context context) {

        mSession = setSession(context,url);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(mSession);
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        builder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right);
        builder.addDefaultShareMenuItem();
        builder.setShowTitle(true);
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_arrow_back));
        CustomTabsIntent customTabsIntent = builder.build();
        CustomTabsHelper.addKeepAliveExtra(context, customTabsIntent.intent);
        CustomTabsActivityHelper.openCustomTab((Activity) context, customTabsIntent,
                Uri.parse(url), new CustomTabsActivityHelper.CustomTabsFallback() {
            @Override
            public void openUri(Activity activity, Uri uri) {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.putExtra(Intent.EXTRA_REFERRER, uri +
                        context.getPackageName());
                context.startActivity(intent);
            }
        });
    }

 private static CustomTabsSession setSession(final Context context, final String url){

     CustomTabsClient.bindCustomTabsService( context, "com.android.chrome",
             new CustomTabsServiceConnection() {
                 @Override
                 public void onCustomTabsServiceConnected(ComponentName componentName,
                                                          CustomTabsClient customTabsClient) {
                     mClient = customTabsClient;
                     mClient.warmup(0);
                     mSession = mClient.newSession(new CustomTabsCallback());
                     mSession.mayLaunchUrl(Uri.parse(url), null, null);
                 }

                 @Override
                 public void onServiceDisconnected(ComponentName name) {
                     mClient = null;


                 }
             });

        return mSession;
 }
}
