package com.fci.androCroder.BD;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fci.androCroder.BD.Service.NetworkStateRecever;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewPassword extends AppCompatActivity {

    EditText new_pass,new_re_pass;
    Button ok;
    private ProgressBar progressBar;
    private NetworkStateRecever networkStateRecever;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("New Password");

        final Intent intent=getIntent();

        new_pass=findViewById(R.id.new_pass_id);
        new_re_pass=findViewById(R.id.new_re_pass_id);
        ok=findViewById(R.id.new_okBtn_id);
        progressBar=findViewById(R.id.spin_kit_newPass);
        db = FirebaseFirestore.getInstance();


        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()){
                    if (intent !=null){
                        String number=intent.getStringExtra("Number");
                        if (number != null){
                            updatePass();
                            progressBar.setVisibility(View.VISIBLE);
                            ok.setClickable(false);
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Pass Not Valid",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    ok.setClickable(true);
                }
            }
        });
    }

    private void updatePass() {
    }

    public boolean valid(){
        boolean valid=true;
        String pass=new_pass.getText().toString().trim();
        String re_pass=new_re_pass.getText().toString().trim();
        if (pass.isEmpty()|| pass.length()<6){
            new_pass.setError("at last 6 character");
            valid=false;
        } else {
            new_pass.setError(null);
        }

        if (re_pass.isEmpty() || !re_pass.equals(pass)){
            new_re_pass.setError("Pass  Not Match");
            valid=false;
        } else {
            new_re_pass.setError(null);
        }

        return valid;
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkStateRecever);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        networkStateRecever=new NetworkStateRecever();
        registerReceiver(networkStateRecever,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
}
