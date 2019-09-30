package com.fci.androCroder.BD;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fci.androCroder.BD.Service.NetworkStateRecever;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Forgot_activity extends AppCompatActivity {


    EditText phone;
    Button send_btn;
    FirebaseAuth mAuth;
    private NetworkStateRecever networkStateRecever;
    private String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_activity);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Forgot Password");

        phone=findViewById(R.id.forgot_email);
        send_btn=findViewById(R.id.forgot_send_btn);
        mAuth = FirebaseAuth.getInstance();


        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_btn.setClickable(false);
                if (phone.getText().toString().isEmpty() || Integer.parseInt(phone.getText().toString())<11 || Integer.parseInt(phone.getText().toString())>11){
                    Toast.makeText(Forgot_activity.this,"Phone Number Not Correct",Toast.LENGTH_LONG).show();
                    send_btn.setClickable(true);
                }else {
                    sendVerification(phone.getText().toString());

                }
            }
        });

    }

    private void sendVerification(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+88"+number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {


                        final String code = credential.getSmsCode();
                        LayoutInflater inflater = LayoutInflater.from(Forgot_activity.this);
                        final View view = inflater.inflate(R.layout.submitcode_layout, null);
                        final EditText eee = view.findViewById(R.id.submit_code);
                        if (code !=null){
                            eee.setText(code);
                        }
                        new AlertDialog.Builder(Forgot_activity.this)
                                .setView(view)
                                .setTitle("Enter Verification Code")
                                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String code = eee.getText().toString();
                                        verifVerification(mVerificationId, code);
                                        dialog.dismiss();


                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create()
                                .show()
                        ;
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Log.e("EEEEE",e.getMessage());
                        send_btn.setClickable(true);
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(Forgot_activity.this, "Code Not Match", Toast.LENGTH_SHORT).show();
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(Forgot_activity.this, " Many Request", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {

                        mVerificationId = verificationId;
                    }
                });
    }


    private void verifVerification(String mVerificationId, String code) {
        if (mVerificationId != null && code!=null){
            PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mVerificationId,code);
            signInWithPhoneAuthCredential(credential);
        }
        else {
            Toast.makeText(Forgot_activity.this, "Code Not Match", Toast.LENGTH_SHORT).show();
            send_btn.setClickable(true);
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Forgot_activity.this, "go Password reset page", Toast.LENGTH_SHORT).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.e("Error","Credential Failed");
                            }
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
