package com.example.mobiltriyaj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mobiltriyaj.Class.Profile;
import com.example.mobiltriyaj.Class.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private List<Profile> profileList;
    private String deviceUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        profileList = new ArrayList<>();
        deviceUID = Utils.getDeviceUID(this);
        reference = FirebaseDatabase.getInstance().getReference(deviceUID).child("Profiller");

        profileAdapter = new ProfileAdapter(this, profileList, reference);
        recyclerView.setAdapter(profileAdapter);

        loadProfiles();
    }

    private void loadProfiles() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profileList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Profile profile = new Profile(
                            dataSnapshot.getKey(),
                            dataSnapshot.child("KullanıcıAdı").getValue(String.class),
                            dataSnapshot.child("KullanıcıSoyadı").getValue(String.class),
                            dataSnapshot.child("KullanıcıBoy").getValue(String.class),
                            dataSnapshot.child("KullanıcıKilo").getValue(String.class),
                            dataSnapshot.child("KullanıcıDoğumTarihi").getValue(String.class),
                            dataSnapshot.child("KullanıcıCinsiyet").getValue(String.class),
                            dataSnapshot.child("KullanıcıKanGrubu").getValue(String.class)
                    );
                    profileList.add(profile);
                }
                profileAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_update_alert), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void quest_entrance(View v) {
        startActivity(new Intent(MainActivity.this, QuestEntranceActivity.class));
        finish();
    }

    public void keepGoing(View v) {
        startActivity(new Intent(MainActivity.this, TriageActivity.class));
        finish();
    }

    public void create_profile(View v) {
        startActivity(new Intent(MainActivity.this, CreateProfileActivity.class));
        finish();
    }
}
