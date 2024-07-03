package com.example.mobiltriyaj;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiltriyaj.Class.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    private EditText edit_name, edit_surname, edit_weight, edit_height, edit_bDay;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale, radioButtonFemale;
    private Spinner spinnerkanGrubu;
    private DatabaseReference reference;
    private String deviceUID;
    private ArrayAdapter<CharSequence> adapter;
    private String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        edit_name = findViewById(R.id.edit_name);
        edit_surname = findViewById(R.id.edit_surname);
        edit_weight = findViewById(R.id.edit_weight);
        edit_height = findViewById(R.id.edit_height);
        edit_bDay = findViewById(R.id.edit_bDay);
        radioGroupGender = findViewById(R.id.edit_radioGroupGender);
        radioButtonMale = findViewById(R.id.edit_radioButtonMale);
        radioButtonFemale = findViewById(R.id.edit_radioButtonFemale);
        spinnerkanGrubu = findViewById(R.id.edit_spinnerBloodGroup);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_group_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerkanGrubu.setAdapter(adapter);

        deviceUID = Utils.getDeviceUID(this);
        reference = FirebaseDatabase.getInstance().getReference(deviceUID).child("Profiller");

        // Intent ile gönderilen profil bilgilerini al
        profileId = getIntent().getStringExtra("profileId");
        String name = getIntent().getStringExtra("name");
        String surname = getIntent().getStringExtra("surname");
        String weight = getIntent().getStringExtra("weight");
        String height = getIntent().getStringExtra("height");
        String birthDate = getIntent().getStringExtra("birthDate");
        String gender = getIntent().getStringExtra("gender");
        String bloodGroup = getIntent().getStringExtra("bloodGroup");

        // Profil bilgilerini ilgili alanlara yerleştir
        edit_name.setText(name);
        edit_surname.setText(surname);
        edit_weight.setText(weight);
        edit_height.setText(height);
        edit_bDay.setText(birthDate);

        if (gender != null) {
            if (gender.equals("Erkek") || gender.equals("Male")) {
                radioButtonMale.setChecked(true);
            } else if (gender.equals("Kadın") || gender.equals("Female")) {
                radioButtonFemale.setChecked(true);
            }
        }

        if (bloodGroup != null) {
            int spinnerPosition = adapter.getPosition(bloodGroup);
            spinnerkanGrubu.setSelection(spinnerPosition);
        }
    }

    public void save(View v) {
        String name = edit_name.getText().toString();
        String surname = edit_surname.getText().toString();
        String weight = edit_weight.getText().toString();
        String height = edit_height.getText().toString();
        String bDay = edit_bDay.getText().toString();
        String gender = ((RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();
        String bloodGroup = spinnerkanGrubu.getSelectedItem().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname) && !TextUtils.isEmpty(weight) &&
                !TextUtils.isEmpty(height) && !TextUtils.isEmpty(bDay)) {

            HashMap<String, Object> profileData = new HashMap<>();
            profileData.put("KullanıcıAdı", name);
            profileData.put("KullanıcıSoyadı", surname);
            profileData.put("KullanıcıKilo", weight);
            profileData.put("KullanıcıBoy", height);
            profileData.put("KullanıcıDoğumTarihi", bDay);
            profileData.put("KullanıcıCinsiyet", gender);
            profileData.put("KullanıcıKanGrubu", bloodGroup);

            reference.child(profileId).setValue(profileData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.toast_save_alert), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.toast_update_alert), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.toast_empty_alert), Toast.LENGTH_SHORT).show();
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
