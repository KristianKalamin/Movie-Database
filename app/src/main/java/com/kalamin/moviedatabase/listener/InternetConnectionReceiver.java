package com.kalamin.moviedatabase.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.jetbrains.annotations.NotNull;

public class InternetConnectionReceiver extends BroadcastReceiver {
    private InternetConnectionListener internetConnectionListener;

    private boolean checkConnection(@NotNull Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (checkConnection(context)) {
            internetConnectionListener.onNetworkConnectionChanged(true);
        } else {
            internetConnectionListener.onNetworkConnectionChanged(false);
        }
    }

    public interface InternetConnectionListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

    public void setInternetConnectionListener(InternetConnectionListener internetConnectionListener) {
        this.internetConnectionListener = internetConnectionListener;
    }
}
