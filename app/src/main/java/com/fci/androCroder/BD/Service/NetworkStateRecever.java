package com.fci.androCroder.BD.Service;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
public class NetworkStateRecever extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        AlertDialog dialog=new AlertDialog.Builder(context)
                .setTitle("Connect to a Network")
                .setMessage("To Use FCI Blood Bank You need to Connected a Network")
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        
                    }
                })
                .setCancelable(false)
                .create();
        boolean isConnected=NetworkUtils.getConnectivityStatus(context);
        if (isConnected){
            if (dialog.isShowing()){
                dialog.cancel();
            }
        }else {
            dialog.show();
        }
    }
}
