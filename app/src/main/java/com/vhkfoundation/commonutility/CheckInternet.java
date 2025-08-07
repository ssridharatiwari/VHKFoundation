package com.vhkfoundation.commonutility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

public class CheckInternet {
    private final Context context;

    public CheckInternet(Context context) {
        this.context = context;
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
                            Log.i("CheckInternet_status", "Network is available" + GlobalVariables.TAGPOSTTEXT);
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

    public String getStringRes(int strId) {
        String str = context.getResources().getString(strId);
        return str;
    }

}
