package com.fci.androCroder.BD.Service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
    public static boolean getConnectivityStatus(Context context){
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}
