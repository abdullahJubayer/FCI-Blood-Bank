package com.fci.androCroder.BD;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fci.androCroder.BD.Service.NetworkStateRecever;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class Add_Donor extends AppCompatActivity  {
    private static final int RQ_CODE = 1;
    EditText d_name,d_depertmant,d_semester,d_phone1,d_address,d_email,d_pass,d_re_pass;
    Button save_button;
    CircleImageView donor_image;
    File picture_file;
    String ImagedownloadUrl;
    FirebaseFirestore db;
    StorageReference mStorageRef;
    FirebaseAuth mAuth;
    Spinner spinner,spinner2;
    ArrayAdapter<CharSequence> adapterGender;
    ArrayAdapter<CharSequence> adapterBlood;
    ProgressBar progressBar;
    private NetworkStateRecever networkStateRecever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__donor);

        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("SignUp");

        donor_image=findViewById(R.id.donor_picture);
        d_name=findViewById(R.id.donor_name);
        d_depertmant=findViewById(R.id.donor_department);
        d_semester=findViewById(R.id.donor_semester);
        d_phone1=findViewById(R.id.donor_mobile_number);
        d_address=findViewById(R.id.donor_address);
        save_button=findViewById(R.id.donor_save_button);
        spinner=findViewById(R.id.gender_select_spinner);
        spinner2=findViewById(R.id.blood_select_spinner);
        d_email=findViewById(R.id.donor_email);
        d_pass=findViewById(R.id.donor_password);
        d_re_pass=findViewById(R.id.donor_re_pass);
        progressBar=findViewById(R.id.spin_kit_new_donor);

        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        mStorageRef = FirebaseStorage.getInstance().getReference("All_donor_photo/"+System.currentTimeMillis()+".jpg");
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String[] gend = {"Select Gender","Male", "Female"};
        String[] b_grop = {"Select Blood group","A+", "A-","B+","B-","O+","O-","AB+","AB-"};


        adapterGender =    new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,gend);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterGender);


        adapterBlood =    new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,b_grop);
        adapterBlood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapterBlood);


        donor_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){

                        createuser();
                        progressBar.setVisibility(View.VISIBLE);
                        save_button.setClickable(false);

                    }
                else {
                    Toast.makeText(getApplicationContext(),"Please Fill All Data Correctly",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RQ_CODE && resultCode==RESULT_OK && data!=null){
            try {
                picture_file=FileUtil.from(Add_Donor.this,data.getData());
                Bitmap img =BitmapFactory.decodeFile(picture_file.getAbsolutePath());
                Glide.with(Add_Donor.this).load(img).into(donor_image);
            } catch (IOException e) {
                Toast.makeText(Add_Donor.this,"Error in Select Image",Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(Add_Donor.this,"Error in Select Image",Toast.LENGTH_LONG).show();
        }
    }

    public void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, RQ_CODE);
    }


    private void uploadImage() {

        final StorageReference storageReference=mStorageRef;

        if (picture_file !=null){
            try {
                File compressedImage=new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToFile(picture_file.getAbsoluteFile());

                UploadTask uploadTask=storageReference.putFile(Uri.fromFile(compressedImage));
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return storageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful() && task.getResult() !=null) {
                            ImagedownloadUrl =task.getResult().toString().trim();
                            UploadUserData();
                            Log.i("downloadUrllllll", "onComplete: Url: " + ImagedownloadUrl);
                        }else {
                            Toast.makeText(Add_Donor.this, "Picture Upload failed.",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            save_button.setClickable(true);
                        }
                    }
                });
            } catch (IOException e) {
                progressBar.setVisibility(View.INVISIBLE);
                save_button.setClickable(true);
            }

        }else {
            ImagedownloadUrl="https://firebasestorage.googleapis.com/v0/b/fci-bloodbank.appspot.com/o/editable_photo%2F1555583524434.jpg?alt=media&token=caa5e1b2-5423-4496-8811-ec517da8286c";
            UploadUserData();
        }

    }

    private void UploadUserData() {

        final String email= d_email.getText().toString();
        final String name= d_name.getText().toString();
        final String department=d_depertmant.getText().toString();
        final String semester = d_semester.getText().toString();
        final String phone1 = d_phone1.getText().toString();
        String address = d_address.getText().toString();
        final String gender=spinner.getSelectedItem().toString();
        final String blood_group=spinner2.getSelectedItem().toString();
        String pass= d_pass.getText().toString();
        String last_donate= "0/0/0";
        final String give_blood="0";


             final Map<String, Object> newContact = new HashMap<>();
            newContact.clear();

            newContact.put("Name", name);
            newContact.put("Image", ImagedownloadUrl);
            newContact.put("Department", department);
            newContact.put("Batch", semester);
            newContact.put("Phone1", phone1);
            newContact.put("Address", address);
            newContact.put("Gender", gender);
            newContact.put("Blood_Group", blood_group);
            newContact.put("Email", email);
            newContact.put("Last_Donate_Date",last_donate);
            newContact.put("Give_Blood", give_blood);
            newContact.put("Password", pass);


        DocumentReference user=db.collection("All_Blood_Group").document(blood_group).collection(gender).document(email);
               user.set(newContact).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       Top_Donor_Note data=new Top_Donor_Note(name,ImagedownloadUrl,department,semester,gender,blood_group,give_blood);
                       DocumentReference user=db.collection("All_donor_Info").document(blood_group).collection("Top_Donor").document(email);
                       user.set(data);
                       showDialog();
                       progressBar.setVisibility(View.INVISIBLE);
                       save_button.setClickable(true);

                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(Add_Donor.this, "Donor Registered Failed...!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                       progressBar.setVisibility(View.INVISIBLE);
                       save_button.setClickable(true);
                   }
               });


    }

    private void createuser() {
        String email= d_email.getText().toString();
        String pass= d_pass.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user=mAuth.getCurrentUser();
                            if (user != null){
                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            uploadImage();
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Donor Registered failed", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            save_button.setClickable(true);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Donor Registered failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                save_button.setClickable(true);
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String name= d_name.getText().toString();
        String department=d_depertmant.getText().toString();
        String semester = d_semester.getText().toString();
        String phone1 = d_phone1.getText().toString();;
        String address = d_address.getText().toString();
        String gender=spinner.getSelectedItem().toString();
        String blood_group=spinner2.getSelectedItem().toString();
        String email= d_email.getText().toString();
        String pass= d_pass.getText().toString();
        String re_pass= d_re_pass.getText().toString();


        if (name.isEmpty() || name.length() < 3) {
            d_name.setError("at least 3 characters");
            valid = false;
        } else {
            d_name.setError(null);
        }

        if (department.isEmpty()) {
            d_depertmant.setError("Department Null");
            valid = false;
        } else {
            d_depertmant.setError(null);
        }

        if (semester.isEmpty()) {
            d_semester.setError("Batch Null");
            valid = false;
        } else {
            d_semester.setError(null);
        }

        if (phone1.isEmpty()){
            d_phone1.setError("Phone Null");
            valid=false;
        } else {
            d_phone1.setError(null);
        }
        if (address.isEmpty()){
            d_address.setError("Village Null");
            valid=false;
        } else {
            d_address.setError(null);
        }
        if (gender.isEmpty()|| gender.equals("Select Gender")){
            Toast.makeText(getApplicationContext(),"Error in Gender",Toast.LENGTH_SHORT).show();
            valid=false;
        }
        if (blood_group.isEmpty()|| blood_group.equals("Select Blood group")){
            Toast.makeText(getApplicationContext(),"Error in Gender",Toast.LENGTH_SHORT).show();
            valid=false;
        }
        if (email.isEmpty()){
            d_email.setError("Wrong Email Formate");
            valid=false;
        } else {
            d_email.setError(null);
        }
        if (pass.isEmpty()|| pass.length()<6){
            d_pass.setError("at last 6 character");
            valid=false;
        } else {
            d_pass.setError(null);
        }
        if (re_pass.isEmpty()|| !(re_pass.equals(pass))){
            d_re_pass.setError("Password Not Match");
            valid=false;
        } else {
            d_re_pass.setError(null);
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

    private void showDialog(){
        new AlertDialog.Builder(Add_Donor.this)
                .setTitle("Registration Successful")
                .setMessage("Please Check Your Email To Verify Your Account")
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

}
