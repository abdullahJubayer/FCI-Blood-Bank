package com.fci.androCroder.BD;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fci.androCroder.BD.Service.NetworkStateRecever;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_activity extends AppCompatActivity {


    EditText email;
    Button send_btn;
    FirebaseAuth auth;
    private NetworkStateRecever networkStateRecever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_activity);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Forgot Password");

        email=findViewById(R.id.forgot_email);
        send_btn=findViewById(R.id.forgot_send_btn);
        auth = FirebaseAuth.getInstance();


        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_btn.setClickable(false);
                if (email.getText().toString().isEmpty()){
                    Toast.makeText(Forgot_activity.this,"Email is Empty",Toast.LENGTH_LONG).show();
                    send_btn.setClickable(true);
                }else {


                    auth.sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        AlertDialog dialog=new AlertDialog.Builder(Forgot_activity.this)
                                                .setTitle("Verification Email Sent")
                                                .setMessage("Please Check Your Email")
                                                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                })
                                                .setCancelable(false)
                                                .create();
                                        dialog.show();
                                        send_btn.setClickable(true);
                                    }else {
                                        Toast.makeText(Forgot_activity.this,"Email Not sent.",Toast.LENGTH_LONG).show();
                                        send_btn.setClickable(true);
                                    }
                                }
                            });

                }
            }
        });

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
