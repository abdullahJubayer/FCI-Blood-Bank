package com.fci.androCroder.BD;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fci.androCroder.BD.Service.NetworkStateRecever;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorDetails extends AppCompatActivity {
    CircleImageView details_image;
    TextView de_name,de_email,de_phone,de_blood_grp,de_lastdonet,de_times,de_village,de_upazilla;
    private NetworkStateRecever networkStateRecever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_details);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Donor Details");

        networkStateRecever=new NetworkStateRecever();
        registerReceiver(networkStateRecever,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        details_image=findViewById(R.id.details_image);
        de_name=findViewById(R.id.details_name);
        de_email=findViewById(R.id.details_email);
        de_phone=findViewById(R.id.details_Phone);
        de_blood_grp=findViewById(R.id.details_blood_group);
        de_lastdonet=findViewById(R.id.details_last_donate_date);
        de_times=findViewById(R.id.details_times_donate);
        de_village=findViewById(R.id.details_village);
        de_upazilla=findViewById(R.id.details_upazilla);


        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        String image=intent.getStringExtra("Image");
        String email=intent.getStringExtra("Email");
        String phone=intent.getStringExtra("Phone");
        String blood_gp=intent.getStringExtra("Blood_group");
        String last_do=intent.getStringExtra("Last_Donate_Date");
        String times=intent.getStringExtra("Give_Blood");
        String village=intent.getStringExtra("Village");
        String upazill=intent.getStringExtra("Upazilla");

        de_name.setText("Name: "+name);
        de_email.setText("Email: "+email);
        de_phone.setText("Phone: "+phone);
        de_blood_grp.setText("Blood Group:  "+blood_gp);
        de_lastdonet.setText("Last Donate: "+last_do);
        de_times.setText("Donate Times: "+times);
        de_village.setText("Village: "+village);
        de_upazilla.setText("Upazilla: "+upazill);

        Glide.with(this)
                .load(image)
                .apply(RequestOptions.circleCropTransform())
                .into(details_image);



    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkStateRecever);
        super.onStop();
    }
}
