package com.fci.androCroder.BD;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fci.androCroder.BD.Service.NetworkStateRecever;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class EditProfile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    EditText userPhone, userLastDonateDate, userHowMuchDonate, userName;
    CircleImageView userImage;
    private File picture_file;
    String down_Url;
    private StorageReference mStorageRef;
    FirebaseFirestore db;
<<<<<<< HEAD
    String bll,phn,last_date,how_m,gender_m;
=======
    String bloodGroup, email, phone, lastDonateDate, howMDonate,gender_m,usrName,uImage;
>>>>>>> Branch2
    Button up_button;
    ProgressBar progressBar;
    private NetworkStateRecever networkStateRecever;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Edit Profile");

        mStorageRef = FirebaseStorage.getInstance().getReference("editable_photo");
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
<<<<<<< HEAD
        bll = intent.getStringExtra("Blood_group");
        phn=intent.getStringExtra("Phone");
        last_date=intent.getStringExtra("Last_Donate");
        how_m=intent.getStringExtra("How_Much");
=======
        usrName = intent.getStringExtra("Name");
        bloodGroup = intent.getStringExtra("Blood_group");
        email = intent.getStringExtra("Email");
        phone =intent.getStringExtra("Phone");
        phone =intent.getStringExtra("Image");
        lastDonateDate =intent.getStringExtra("Last_Donate");
        howMDonate =intent.getStringExtra("How_Much");
>>>>>>> Branch2
        gender_m=intent.getStringExtra("Gender");

        progressBar=findViewById(R.id.spin_kit_edit);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);


        userName =findViewById(R.id.edit_pro_name);
        userPhone = findViewById(R.id.edit_pro_phon);
        userLastDonateDate = findViewById(R.id.edit_pro_lastdonate);
        userHowMuchDonate = findViewById(R.id.edit_pro_howmuch);
        userImage = findViewById(R.id.edit_pro_image);
        up_button=findViewById(R.id.upload_button);

        userName.setText(usrName);
        userPhone.setText(phone);
        userLastDonateDate.setText(lastDonateDate);
        userHowMuchDonate.setText(howMDonate);
        Glide.with(EditProfile.this).load(uImage).into(userImage);


        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                up_button.setClickable(false);
                uploadImage();
            }
        });



    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null){
            try {
                picture_file=FileUtil.from(EditProfile.this,data.getData());
                Bitmap img = BitmapFactory.decodeFile(picture_file.getAbsolutePath());
                Glide.with(EditProfile.this).load(img).into(userImage);
            } catch (IOException e) {
                Toast.makeText(EditProfile.this,"Error in Select Image",Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(EditProfile.this,"Error in Select Image",Toast.LENGTH_LONG).show();
        }
    }


    private void uploadImage() {

        final StorageReference storageReference = mStorageRef.child(System.currentTimeMillis()
                + "." + "jpg");

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
                        if (task.isSuccessful()) {
                            down_Url = task.getResult().toString();
                            uploadWithImage();
                            Log.i("downloadUrllllll", "onComplete: Url: " + down_Url);
                        }else {
                            Toast.makeText(EditProfile.this, "Picture Upload failed.",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            up_button.setClickable(true);
                        }
                    }
                });

            } catch (IOException e) {
                Toast.makeText(EditProfile.this, "Picture Upload failed.",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                up_button.setClickable(true);
            }

        }else {
            uploadWithoutImage();
        }

    }


    public void uploadWithoutImage() {

        String name=userName.getText().toString().trim();
        String ph = userPhone.getText().toString();
        String lst = userLastDonateDate.getText().toString();
        String how = userHowMuchDonate.getText().toString();


        if (name.isEmpty()){
            name= usrName;
        }if (ph.isEmpty()){
            ph= phone;
        }if (ph.isEmpty()){
            ph= phone;
        }
        if (lst.isEmpty()){
            lst= lastDonateDate;
        }
        if (how.isEmpty()){
            how= howMDonate;
        }


<<<<<<< HEAD
        DocumentReference documentReference=db.collection("All_Blood_Group").document(bll).collection(gender_m).document(phn);
        documentReference.update(ConstName.phone1,ph);
        documentReference.update(ConstName.lastDonateDate,lst);
        documentReference.update(ConstName.give_blood,how)
=======
        DocumentReference documentReference=db.collection("All_Blood_Group").document(bloodGroup).collection(gender_m).document(email);
        documentReference.update("Name",name);
        documentReference.update("Phone1",ph);
        documentReference.update("Last_Donate_Date",lst);
        documentReference.update("Give_Blood",how)
>>>>>>> Branch2
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        update2_no_image();
                        progressBar.setVisibility(View.GONE);
                        up_button.setClickable(true);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "Updated Failed", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        up_button.setClickable(true);
                    }
                });

    }


    public void uploadWithImage() {

        String name=userName.getText().toString().trim();
        String ph = userPhone.getText().toString();
        String lst = userLastDonateDate.getText().toString();
        String how = userHowMuchDonate.getText().toString();

        if (name.isEmpty()){
            name= usrName;
        }
        if (ph.isEmpty()){
            ph= phone;
        }
        if (lst.isEmpty()){
            lst= lastDonateDate;
        }
        if (how.isEmpty()){
            how= howMDonate;
        }
<<<<<<< HEAD
        DocumentReference documentReference=db.collection("All_Blood_Group").document(bll).collection(gender_m).document(phn);
        documentReference.update(ConstName.phone1,ph);
        documentReference.update(ConstName.imagePath,down_Url);
        documentReference.update(ConstName.lastDonateDate,lst);
        documentReference.update(ConstName.give_blood,how)
=======
        DocumentReference documentReference=db.collection("All_Blood_Group").document(bloodGroup).collection(gender_m).document(email);
        documentReference.update("Name",name);
        documentReference.update("Phone1",ph);
        documentReference.update("Image",down_Url);
        documentReference.update("Last_Donate_Date",lst);
        documentReference.update("Give_Blood",how)
>>>>>>> Branch2
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        update2_with_image();
                        progressBar.setVisibility(View.INVISIBLE);
                        up_button.setClickable(true);

                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Updated Failed..!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                up_button.setClickable(true);
            }
        });


    }


    private void update2_with_image() {

        String how = userHowMuchDonate.getText().toString();
        if (how.isEmpty()){
            how= howMDonate;
        }

<<<<<<< HEAD
        DocumentReference user=db.collection("All_donor_Info").document(bll).collection("Top_Donor").document(phn);
        user.update(ConstName.give_blood,how);
        user.update(ConstName.imagePath,down_Url)
=======
        DocumentReference user=db.collection("All_donor_Info").document(bloodGroup).collection("Top_Donor").document(email);
        user.update("give_Blood",how);
        user.update("image",down_Url)
>>>>>>> Branch2
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),Login_activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        progressBar.setVisibility(View.INVISIBLE);
                        up_button.setClickable(true);
                    }
                });
    }

    private void update2_no_image() {

        String how = userHowMuchDonate.getText().toString();
        if (how.isEmpty()){
            how= howMDonate;
        }

<<<<<<< HEAD
        DocumentReference user=db.collection("All_donor_Info").document(bll).collection("Top_Donor").document(phn);
        user.update(ConstName.give_blood,how)
=======
        DocumentReference user=db.collection("All_donor_Info").document(bloodGroup).collection("Top_Donor").document(email);
        user.update("give_Blood",how)
>>>>>>> Branch2
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),Login_activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        progressBar.setVisibility(View.INVISIBLE);
                        up_button.setClickable(true);
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
