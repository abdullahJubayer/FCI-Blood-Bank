package com.fci.androCroder.BD;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fci.androCroder.BD.Service.NetworkStateRecever;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Nullable;

public class Photo_gallery extends AppCompatActivity {

    String User_name, User_img, date;
    Calendar calander;
    SimpleDateFormat simpledateFormat;


    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar progressBar;

    FirebaseFirestore db;
    private List<Upload> mUploads;
    private NetworkStateRecever networkStateRecever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Gallery");

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.spin_kit_gallery);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        progressBar.setVisibility(View.VISIBLE);

        mUploads = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        CollectionReference user = db.collection("Photogallery");
        user.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mUploads.clear();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        String des = snapshot.get("mName").toString();
                        String de_img = snapshot.get("mImageUrl").toString();
                        String name = snapshot.get("name").toString();
                        String image = snapshot.get("image").toString();

                        Upload upload = new Upload(des, de_img, name, image, date);
                        mUploads.add(upload);

                        mAdapter = new ImageAdapter(Photo_gallery.this, mUploads);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(mAdapter);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }else {
                    Toast.makeText(Photo_gallery.this,"Data Not Found",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }
            }
            })
            ;

        Intent intent = getIntent();
        User_name = intent.getStringExtra("send_name");
        User_img = intent.getStringExtra("send_img");


        calander = Calendar.getInstance();
        simpledateFormat = new SimpleDateFormat("HH:mm:ss");
        date = simpledateFormat.format(calander.getTime());


        FloatingActionButton floatingActionButton = findViewById(R.id.button_add_photo);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhotogalleryActivity();
            }
        });


    }

    private void uploadPhotogalleryActivity() {
        Intent intent=new Intent(getApplicationContext(),uploadPhotoActivity.class);
        intent.putExtra("nameOfSender",User_name);
        intent.putExtra("imageOfSender",User_img);
        startActivity(intent);
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
