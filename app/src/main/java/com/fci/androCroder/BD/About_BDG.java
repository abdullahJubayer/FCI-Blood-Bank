package com.fci.androCroder.BD;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fci.androCroder.BD.Service.NetworkStateRecever;

public class About_BDG extends AppCompatActivity {

    private NetworkStateRecever networkStateRecever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__bdg);
        networkStateRecever=new NetworkStateRecever();
        registerReceiver(networkStateRecever,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("About FCI Blood Donation");

    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkStateRecever);
        super.onStop();
    }
}
