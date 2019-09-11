package com.fci.androCroder.BD;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.fci.androCroder.BD.Service.NetworkStateRecever;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    CardView
            a_positive,
            a_negative,
            b_positive,
            b_negative,
            o_positive,
            o_negative,
            ab_positive,
            ab_negative;
    private NetworkStateRecever networkStateRecever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Search Donor");

        networkStateRecever=new NetworkStateRecever();
        registerReceiver(networkStateRecever,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        a_positive=findViewById(R.id.blood_group_a_positibe);
        a_negative=findViewById(R.id.blood_group_a_negative);
        b_positive=findViewById(R.id.blood_group_b_positive);
        b_negative=findViewById(R.id.blood_group_b_negative);
        o_positive=findViewById(R.id.blood_group_o_positive);
        o_negative=findViewById(R.id.blood_group_o_negative);
        ab_positive=findViewById(R.id.blood_group_ab_positive);
        ab_negative=findViewById(R.id.blood_group_ab_negative);


        setListner();
    }

    private void setListner() {

        a_positive.setOnClickListener(this);
        a_negative.setOnClickListener(this);
        b_positive.setOnClickListener(this);
        b_negative.setOnClickListener(this);
        o_positive.setOnClickListener(this);
        o_negative.setOnClickListener(this);
        ab_positive.setOnClickListener(this);
        ab_negative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

            if(v.getId()== R.id.blood_group_a_positibe) {
                newIntent("A+");
            }
            if (v.getId()==R.id.blood_group_a_negative) {
                newIntent("A-");
            }
            if (v.getId()==R.id.blood_group_b_positive) {
                newIntent("B+");
            }
            if (v.getId()==R.id.blood_group_b_negative) {
                newIntent("B-");
            }
            if (v.getId()==R.id.blood_group_o_positive){
                newIntent("O+");
            }
            if (v.getId()==R.id.blood_group_o_negative) {
                newIntent("O-");
            }
            if (v.getId()==R.id.blood_group_ab_positive){
                newIntent("AB+");
            }
            if (v.getId()==R.id.blood_group_ab_negative) {
                newIntent("AB-");
            }


    }

    private void newIntent(String s) {
        Intent intent=new Intent(getApplicationContext(),Male_Female_Choose.class);
        intent.putExtra("Blood_group",s);
        startActivity(intent);
        finishActivity(0);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkStateRecever);
        super.onStop();
    }
}
