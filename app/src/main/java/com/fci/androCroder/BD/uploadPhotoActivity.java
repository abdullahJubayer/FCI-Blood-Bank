package com.fci.androCroder.BD;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fci.androCroder.BD.Service.NetworkStateRecever;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class uploadPhotoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    Button mButtonChooseImage;
    private Button mButtonUpload;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private File picture_file;
    String down_Url;
    private StorageReference mStorageRef;
    FirebaseFirestore db;
    String nameof_writer;
    String imageof_writer;
    String message;
    ProgressBar progressBar;
    private NetworkStateRecever networkStateRecever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Upload Photo");
        }


        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        db = FirebaseFirestore.getInstance();

        Intent intent=getIntent();
        nameof_writer=intent.getStringExtra("nameOfSender");
        imageof_writer=intent.getStringExtra("imageOfSender");



        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.image_view);

        progressBar = (ProgressBar)findViewById(R.id.spin_kit_upload);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);



        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                mButtonUpload.setClickable(false);

                message=mEditTextFileName.getText().toString();
                if (picture_file!=null){
                    if ( !message.isEmpty()){
                        uploadImage(message);
                    }else {
                        Toast.makeText(uploadPhotoActivity.this,"Text is Empty",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        mButtonUpload.setClickable(true);
                    }
                }
                else {
                    Toast.makeText(uploadPhotoActivity.this,"Image Not Select",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    mButtonUpload.setClickable(true);
                }

            }
        });
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadImage(final String message) {

        final StorageReference storageReference = mStorageRef.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));

        if (mImageUri != null) {
            storageReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 500);

                        if (task.isSuccessful()){

                             down_Url=task.getResult().toString();

                            if (message.isEmpty()){
                                Toast.makeText(uploadPhotoActivity.this,"Please Write Something",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                mButtonUpload.setClickable(true);
                            }else {
                                if (nameof_writer != null && imageof_writer != null){
                                    uploadData(message,down_Url,nameof_writer,imageof_writer);
                                }
                                else {
                                    Toast.makeText(uploadPhotoActivity.this,"Something Missing",Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    mButtonUpload.setClickable(true);
                                }
                            }

                        }

                    } else {
                        Toast.makeText(uploadPhotoActivity.this, "Picture Upload failed.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        mButtonUpload.setClickable(true);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(uploadPhotoActivity.this, "Picture Upload failed.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    mButtonUpload.setClickable(true);
                }
            });


        }

    }

    private void uploadData(String message, String down_Url, String nameof_writer, String imageof_writer) {

        Calendar calendar=Calendar.getInstance();
        String date=calendar.getTime().toString();

        Upload upload = new Upload(message, down_Url,nameof_writer,imageof_writer,date);

        DocumentReference user = db.collection("Photogallery").document();
        user.set(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(uploadPhotoActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
                mButtonUpload.setClickable(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(uploadPhotoActivity.this, "Upload Failed", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
                mButtonUpload.setClickable(true);
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
                picture_file=FileUtil.from(uploadPhotoActivity.this,data.getData());
                Bitmap img = BitmapFactory.decodeFile(picture_file.getAbsolutePath());
                Glide.with(uploadPhotoActivity.this).load(img).into(mImageView);
            } catch (IOException e) {
                Toast.makeText(uploadPhotoActivity.this,"Error in Select Image",Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(uploadPhotoActivity.this,"Error in Select Image",Toast.LENGTH_LONG).show();
        }
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
