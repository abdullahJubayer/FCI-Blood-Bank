package com.fci.androCroder.BD;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class About_BDG extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__bdg);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("About FCI Blood Donation");

    }
}
