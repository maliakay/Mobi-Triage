package com.example.mobiltriyaj;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiltriyaj.Class.Profile;
import com.example.mobiltriyaj.Class.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
//Mali değiştirdi
public class QuestEntranceActivity extends AppCompatActivity {
    EditText birthday;
    RadioButton radioButtonMale, radioButtonFemale;
    RadioGroup radioGroupGender;
    private Spinner spinnerkanGrubu;
    private ArrayAdapter<CharSequence> veriAdapterKanGrubu;
    private EditText creat_name, creat_surname, creat_weight, creat_height, creat_bDay;
    private String txtName, txtSurname, txtWeight, txtHeight, txtbDay, selectedGender, kanGrubuu;
    private DatabaseReference mReference;
    private HashMap<String, Object> mData;
    private String deviceUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_entrance);
        Toast.makeText(this, getResources().getString(R.string.toast_start_alert), Toast.LENGTH_SHORT).show();

        spinnerkanGrubu = findViewById(R.id.spinnerBloodGroup);
        birthday = findViewById(R.id.creat_bDay);
        creat_name = findViewById(R.id.creat_name);
        creat_surname = findViewById(R.id.creat_surname);
        creat_weight = findViewById(R.id.creat_weight);
        creat_height = findViewById(R.id.creat_height);
        creat_bDay = findViewById(R.id.creat_bDay);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);

        deviceUID = Utils.getDeviceUID(this);
        mReference = FirebaseDatabase.getInstance().getReference();

        veriAdapterKanGrubu = ArrayAdapter.createFromResource(this, R.array.blood_group_array, android.R.layout.simple_spinner_item);
        veriAdapterKanGrubu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerkanGrubu.setAdapter(veriAdapterKanGrubu);
    }

    public void bDay(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(QuestEntranceActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        birthday.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    public void save(View v) {
        txtName = creat_name.getText().toString();
        txtSurname = creat_surname.getText().toString();
        txtWeight = creat_weight.getText().toString();
        txtHeight = creat_height.getText().toString();
        txtbDay = creat_bDay.getText().toString();

        int selectedRadioButtonId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedRadioButtonId == R.id.radioButtonMale) {
            selectedGender = "Erkek";
        } else if (selectedRadioButtonId == R.id.radioButtonFemale) {
            selectedGender = "Kadın";
        }

        kanGrubuu = spinnerkanGrubu.getSelectedItem().toString();

        if(!TextUtils.isEmpty(selectedGender)) {
            Profile profile = new Profile(deviceUID, txtName, txtSurname, txtHeight, txtWeight, txtbDay, selectedGender, kanGrubuu);

            mData = new HashMap<>();
            mData.put("KullanıcıAdı", txtName);
            mData.put("KullanıcıSoyadı", txtSurname);
            mData.put("KullanıcıKilo", txtWeight);
            mData.put("KullanıcıBoy", txtHeight);
            mData.put("KullanıcıDoğumTarihi", txtbDay);
            mData.put("KullanıcıCinsiyet", selectedGender);
            mData.put("KullanıcıKanGrubu", kanGrubuu);

            mReference.child(deviceUID).child("Quest")
                    .setValue(mData)
                    .addOnCompleteListener(QuestEntranceActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(QuestEntranceActivity.this, getResources().getString(R.string.toast_save_alert), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(QuestEntranceActivity.this, TriageActivity.class);
                                intent.putExtra("profile", profile);
                                startActivity(intent);
                            } else {
                                Toast.makeText(QuestEntranceActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            Toast.makeText(this, getResources().getString(R.string.toast_gender_alert), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(QuestEntranceActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
