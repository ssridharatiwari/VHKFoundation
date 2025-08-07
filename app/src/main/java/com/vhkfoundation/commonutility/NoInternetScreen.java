package com.vhkfoundation.commonutility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vhkfoundation.R;
import com.vhkfoundation.activity.ActivitySplash;

public class NoInternetScreen {
    private final Context context;
    private static RelativeLayout layConnection;
    private Activity act;

    public NoInternetScreen(Context context, View view, Activity act) {
        this.context = context;
        layConnection = (RelativeLayout) view.findViewById(R.id.lay_connection);
        RelativeLayout layProgressbarRefresh = (RelativeLayout) view.findViewById(R.id.lay_activate_refresh);
        ProgressBar progressBarRefresh = (ProgressBar) view.findViewById(R.id.progressbar_refresh);
        TextView textErrorTitle = (TextView) view.findViewById(R.id.nointernet_title);
        TextView textErrorDesc = (TextView) view.findViewById(R.id.nointernet_desc);
        ImageView imgActivateWifi = (ImageView) view.findViewById(R.id.img_activate_wifi);
        ImageView imgActivateRefresh = (ImageView) view.findViewById(R.id.img_activate_refresh);
        ImageView imgActivateMobiledata = (ImageView) view.findViewById(R.id.img_activate_mobiledata);

        progressBarRefresh.setVisibility(View.INVISIBLE);

        imgActivateWifi.setOnClickListener(arg0 -> context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)));

        layProgressbarRefresh.setOnClickListener(arg0 -> {
            if (isConnectingToInternet()) {
                hideError();
            } else {
                showError();
                onResumeLoadData(act);
            }
        });

        imgActivateMobiledata.setOnClickListener(arg0 -> context.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)));
    }

    private void onResumeLoadData(Activity act) {
        if (act instanceof ActivitySplash) {
            ((ActivitySplash) act).resumeApp();
        }
//        else if (act instanceof ActivityMain) {
//            ((ActivityMain) act).resumeApp();
//        } else if (act instanceof ActivityNotification) {
//            ((ActivityNotification) act).resumeApp();
//        } else if (act instanceof ActivityLogin) {
//            ((ActivityLogin) act).resumeApp();
//        }
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                }
            } else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        if (GlobalVariables.ISTESTING) {
//                            Log.i("CheckInternet_status", "Network is available"+GlobalVariables.TAGPOSTTEXT);
                        }
                        return true;
                    }
                } catch (Exception e) {
                    if (GlobalVariables.ISTESTING) {
                        Log.i("CheckInternet_error", "" + e.getMessage() + GlobalVariables.TAGPOSTTEXT);
                    }
                }
            }
        }
        if (GlobalVariables.ISTESTING) {
            Log.i("CheckInternet_status", "Network is not available" + GlobalVariables.TAGPOSTTEXT);
        }
        return false;
    }

    public static void showInternetError() {
        layConnection.setVisibility(View.VISIBLE);
    }

    public void showError() {
        layConnection.setVisibility(View.VISIBLE);
    }

    public static void hideError() {
        layConnection.setVisibility(View.GONE);
    }
}
