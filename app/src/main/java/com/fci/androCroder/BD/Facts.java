package com.fci.androCroder.BD;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.fci.androCroder.BD.Service.NetworkStateRecever;

public class Facts extends AppCompatActivity {

    private NetworkStateRecever networkStateRecever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts);

        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Facts");
    }

    @Override
    protected void onStart() {
        super.onStart();
        networkStateRecever=new NetworkStateRecever();
        registerReceiver(networkStateRecever,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkStateRecever);
        super.onStop();
    }
}
