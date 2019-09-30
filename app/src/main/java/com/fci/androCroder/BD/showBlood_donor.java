package com.fci.androCroder.BD;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fci.androCroder.BD.Service.NetworkStateRecever;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class showBlood_donor extends AppCompatActivity {

    String Blood_group,Gender_intent;
    ListView listView;
    FirebaseFirestore db;
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> image = new ArrayList<String>();
    ArrayList<String> last_donet_date = new ArrayList<String>();
    ArrayList<String> phone = new ArrayList<String>();
    ArrayList<String> department = new ArrayList<String>();
    ArrayList<String> Batch = new ArrayList<String>();
    ArrayList<String> Address = new ArrayList<String>();
    ArrayList<String> Gender = new ArrayList<String>();
    ArrayList<String> blood_group = new ArrayList<String>();
    ArrayList<String> Give_Blood = new ArrayList<String>();
    private NetworkStateRecever networkStateRecever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_blood_donor);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("All Donor");

        db = FirebaseFirestore.getInstance();
        listView = findViewById(R.id.show_blood_listView);

        Intent intent = getIntent();
        Blood_group = intent.getStringExtra("blood_group");
        Gender_intent=intent.getStringExtra("gender");

        loadData();
    }

    private void loadData() {

        db.collection("All_Blood_Group").document(Blood_group)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot doc=task.getResult();
                        db.collection("All_Blood_Group").document(Blood_group).collection(Gender_intent).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                                name.clear();
                                image.clear();
                                last_donet_date.clear();
                                phone.clear();
                                department.clear();
                                Batch.clear();
                                Address.clear();
                                Gender.clear();
                                blood_group.clear();
                                Give_Blood.clear();
                                for (DocumentSnapshot snapshot : documentSnapshots) {

                                    name.add(snapshot.get(ConstName.name).toString());
                                    image.add(snapshot.get(ConstName.imagePath).toString());
                                    last_donet_date.add(snapshot.get(ConstName.lastDonateDate).toString());
                                    phone.add(snapshot.get(ConstName.phone1).toString());
                                    department.add(snapshot.get(ConstName.department).toString());
                                    Batch.add(snapshot.get(ConstName.batch).toString());
                                    Address.add(snapshot.get(ConstName.address).toString());
                                    Gender.add(snapshot.get(ConstName.gender).toString());
                                    blood_group.add(snapshot.get(ConstName.bloodGroup).toString());
                                    Give_Blood.add(snapshot.get(ConstName.give_blood).toString());

                                }

                                //adapter
                                show_blood_donor_adapter adapter =
                                        new show_blood_donor_adapter(showBlood_donor.this, name, image,
                                                last_donet_date, phone, department, Batch, Address, Gender, blood_group, Give_Blood);

                                adapter.notifyDataSetChanged();
                                listView.setAdapter(adapter);

                            }
                        });

                } else {
                    Toast.makeText(getApplicationContext(), "Path Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(showBlood_donor.this, DonorDetails.class);
                intent.putExtra("name", name.get(position));
                intent.putExtra("Image", image.get(position));
                intent.putExtra("Phone", phone.get(position));
                intent.putExtra("Department", department.get(position));
                intent.putExtra("Batch", Batch.get(position));
                intent.putExtra("Blood_group", blood_group.get(position));
                intent.putExtra("Last_Donate_Date", last_donet_date.get(position));
                intent.putExtra("Give_Blood", Give_Blood.get(position));
                intent.putExtra("Address", Address.get(position));
                startActivity(intent);
                finishActivity(0);
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
