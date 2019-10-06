package com.fci.androCroder.BD;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fci.androCroder.BD.Service.NetworkStateRecever;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login_activity extends AppCompatActivity {
    Spinner spinner_login2;
    ArrayAdapter<CharSequence> login_type2;
    EditText log_phone,log_pass;
    Button button;
    TextView  newAccount,forgot_acc;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ProgressBar progressBar;
    SharedPreferences preferences;
    boolean doubleBackToExitPressedOnce = false;
    NetworkStateRecever networkStateRecever;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        spinner_login2=findViewById(R.id.login_spinner2_id);
        log_phone=findViewById(R.id.login_phone_id);
        log_pass=findViewById(R.id.login_password_id);
        button=findViewById(R.id.login_log_id);
        newAccount=findViewById(R.id.login_to_new_id);
        forgot_acc=findViewById(R.id.forgot_id);

        progressBar = (ProgressBar)findViewById(R.id.spin_kit_login);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        preferences = getSharedPreferences("com.example.abdullahjubayer.bb2",MODE_PRIVATE);


        String[] log_type2 = {"Select Blood group","A+", "A-","B+","B-","O+","O-","AB+","AB-"};
        login_type2 =    new ArrayAdapter<CharSequence>(this,R.layout.login_spinner_item,log_type2);
        login_type2.setDropDownViewResource(R.layout.login_spinner_item);
        spinner_login2.setAdapter(login_type2);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()){
                    String blood_gp = spinner_login2.getSelectedItem().toString();
                    getBloodGroup(blood_gp);
                    progressBar.setVisibility(View.VISIBLE);
                    button.setClickable(false);

                }
            }
        });

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Add_Donor.class);
                startActivity(intent);
            }
        });

        forgot_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Forgot_activity.class);
                startActivity(intent);
            }
        });


    }


<<<<<<< HEAD
    private void login(final String phone, final String pass, final String blood) {
        if (!(phone.isEmpty() || pass.isEmpty()|| blood.isEmpty())){
            preferences.edit().putString("Phone",phone).putString("BloodGroup",blood).apply();
            Log.e("Preferences","Save");
            newIntent(phone,blood);
            Toast.makeText(getApplicationContext(), "Authentication Success.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "User Not Found..!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            button.setClickable(true);
        }
=======
    private void login(final String email, final String pass, final String blood) {

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user=mAuth.getCurrentUser();
                                if (user !=null){
                                    if (user.isEmailVerified()){
                                        newIntent(email,blood);
                                        preferences.edit().putString("Email",email).apply();
                                        preferences.edit().putString("Password",pass).apply();
                                        preferences.edit().putString("BloodGroup",blood).apply();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Email not Verified..!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        button.setClickable(true);
                                    }
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    button.setClickable(true);
                                }
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            button.setClickable(true);
                        }

                    }
                });
>>>>>>> Branch2

    }

    private void newIntent(String phone, String blood) {
        Intent intent=new Intent(Login_activity.this,MainActivity.class);
        intent.putExtra("B_group",blood);
        intent.putExtra("phone",phone);
        startActivity(intent);
        progressBar.setVisibility(View.GONE);
        button.setClickable(true);

    }

    public boolean valid(){

        String email= log_phone.getText().toString();
        String pass=log_pass.getText().toString();
        String type = spinner_login2.getSelectedItem().toString();

        boolean valid=true;
        if (email.isEmpty()){
            log_phone.setError("Wrong Email Formate");
            valid=false;
        } else {
            log_phone.setError(null);
        }
        if (pass.isEmpty()|| pass.length()<6){
            log_pass.setError("at last 6 character");
            valid=false;
        } else {
            log_pass.setError(null);
        }
        if (type.isEmpty()|| type.equals("Select Blood group")){
            Toast.makeText(getApplicationContext(),"Error in Blood Group",Toast.LENGTH_SHORT).show();
            valid=false;
        }
        return valid;
    }


    public void getBloodGroup(final String blood_gp){


        final String phone = log_phone.getText().toString();
        final String pass = log_pass.getText().toString();

            db.collection("All_Blood_Group").document(blood_gp).collection("Male").document(phone)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot doc=task.getResult();
                        assert doc != null;
                        if (doc.exists()){
                            login_valid(doc,blood_gp,phone,pass);
                        }
                        else {

                            db.collection("All_Blood_Group").document(blood_gp).collection("Female").document(phone)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if (task.isSuccessful()){
                                        DocumentSnapshot doc=task.getResult();
                                        assert doc != null;
                                        if (doc.exists()){
                                            login_valid(doc,blood_gp,phone,pass);
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),"User Not Found..!",Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                            button.setClickable(true);
                                        }
                                    }else {
                                        Toast.makeText(getApplicationContext(),"User Not Found..!",Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        button.setClickable(true);
                                    }
                                }
                            });
                        }

                    }else {
                        Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        button.setClickable(true);

                    }
                }
            });

            }

            public void login_valid(DocumentSnapshot doc,String blood_gp,String  phone,String pass){
                    String database_bg= (String) doc.get(ConstName.bloodGroup);
                    String database_phone= (String) doc.get(ConstName.phone1);
                    String database_pass= (String) doc.get(ConstName.password);
                    Log.i("Dataaaa",database_bg+"Phone"+database_phone+"pass"+database_pass);
                assert database_bg != null;
                if (database_bg.equals(blood_gp) && phone.equals(database_phone) && pass.equals(database_pass)){
                        login(phone,pass,blood_gp);
                    }else {
                        Toast.makeText(getApplicationContext(),"Blood Group Not Match",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        button.setClickable(true);
                    }
            }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
<<<<<<< HEAD
        if (preferences.contains("Phone") && preferences.contains("BloodGroup")){
            String phone=preferences.getString("Phone",null);
            String blood=preferences.getString("BloodGroup",null);
            if (phone != null && blood != null){
                newIntent(phone,blood);
=======
        if (preferences !=null){
            String email=preferences.getString("Email",null);
            String bloodgroup=preferences.getString("BloodGroup",null);
            if (email != null && bloodgroup != null){
                newIntent(email,bloodgroup);
>>>>>>> Branch2
            }
        }
        networkStateRecever=new NetworkStateRecever();
        registerReceiver(networkStateRecever,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkStateRecever);
    }
}
