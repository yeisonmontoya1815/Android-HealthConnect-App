package com.example.healthconnect.patientProfile;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthconnect.R;
import com.example.healthconnect.utils.FastToast;
import com.example.healthconnect.utils.database.Database;
import com.example.healthconnect.utils.database.Patient;

import java.util.Calendar;

public class EditablePatientProfileActivity extends AppCompatActivity {
    EditText patientName;
    EditText patientEmail;
    EditText patientPhoneNumber;
    EditText patientHeight;
    EditText patientWeight;

    AppCompatButton patientDateOfBirth;
    String dateOfBirth;

    @SuppressLint({"SourceLockedOrientationActivity", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editable_patient_profile);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView statusBackIcon = findViewById(R.id.status_bar_back_arrow_icon);
        ImageView statusBarIcon = findViewById(R.id.status_bar_icon);
        TextView statusBarTitle = findViewById(R.id.status_bar_title);
        statusBackIcon.setOnClickListener(v -> finish());
        statusBarIcon.setImageResource(R.drawable.patient_white);
        statusBarTitle.setText(getString(R.string.patient_profile));

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        patientName = findViewById(R.id.editable_patient_profile_activity_patient_name);
        patientEmail = findViewById(R.id.editable_patient_profile_activity_patient_email);
        patientDateOfBirth = findViewById(R.id.editable_patient_profile_activity_patient_date_of_birth);
        patientPhoneNumber = findViewById(R.id.editable_patient_profile_activity_patient_phone_number);
        patientHeight = findViewById(R.id.editable_patient_profile_activity_patient_height);
        patientWeight = findViewById(R.id.editable_patient_profile_activity_patient_weight);

        ImageView patientPicture = findViewById(R.id.editable_patient_profile_activity_patient_picture);
        Spinner patientGender = findViewById(R.id.editable_patient_profile_activity_patient_gender);
        AppCompatButton saveButton = findViewById(R.id.editable_patient_profile_activity_save_button);

        Patient patient = (Patient) getIntent().getSerializableExtra("patient");
        patientName.setText(patient != null ? patient.getName() : "");
        patientEmail.setText(patient != null ? patient.getEmail() : "");
        patientPhoneNumber.setText(patient != null ? patient.getPhoneNumber() : "");
        patientHeight.setText(patient != null ? String.format("%.2f", patient.getHeight()) : "");
        patientWeight.setText(patient != null ? String.format("%.2f", patient.getWeight()) : "");

        patientPicture.setImageResource(R.drawable.default_profile_picture);

        int tempDateOfBirth = patient != null ? patient.getDateOfBirth() : 0;

        if (tempDateOfBirth == 0) {
            dateOfBirth = String.format("%04d-%02d-%02d",
                    year,
                    month,
                    day);
        } else {
            dateOfBirth = String.format("%04d-%02d-%02d",
                    tempDateOfBirth / 10000,
                    (tempDateOfBirth % 10000) / 100,
                    tempDateOfBirth % 100);
        }

        patientDateOfBirth.setText(dateOfBirth);

        String[] genderOptions = {"Male", "Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genderOptions);
        patientGender.setAdapter(adapter);

        int genderIndex = (patient != null) ? (patient.getGender().equalsIgnoreCase(genderOptions[0]) ? 0 : 1) : 0;
        patientGender.setSelection(genderIndex);

        patientGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = genderOptions[position];
                if (patient != null) {
                    patient.setGender(selectedGender);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (patient != null) {
                    patient.setGender(genderOptions[0]);
                }
            }
        });

        patientDateOfBirth.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditablePatientProfileActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        dateOfBirth = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        patientDateOfBirth.setText(dateOfBirth);
                    },
                    patient != null ? patient.getDateOfBirth() / 10000 : year,
                    patient != null ? (patient.getDateOfBirth() % 10000) / 100 - 1 : month - 1,
                    patient != null ? patient.getDateOfBirth() % 100 : day
            );

            datePickerDialog.show();
        });

        saveButton.setOnClickListener(v -> {
            if (isPatientProfileValid()) {
                if (patient != null) {
                    Log.d("EditablePatientProfile", "Updating existing patient");
                    patient.setName(patientName.getText().toString());
                    patient.setEmail(patientEmail.getText().toString());
                    patient.setDateOfBirth(Integer.parseInt(dateOfBirth.replace("-", "")));
                    patient.setPhoneNumber(patientPhoneNumber.getText().toString());
                    patient.setHeight(Double.parseDouble(patientHeight.getText().toString()));
                    patient.setWeight(Double.parseDouble(patientWeight.getText().toString()));

                    Database.updatePatient(patient);
                } else {
                    Log.d("EditablePatientProfile", "Creating new patient");
                    Patient newPatient = new Patient(
                            0,
                            patientName.getText().toString(),
                            Integer.parseInt(dateOfBirth.replace("-", "")),
                            genderOptions[patientGender.getSelectedItemPosition()],
                            patientPhoneNumber.getText().toString(),
                            patientEmail.getText().toString(),
                            Double.parseDouble(patientHeight.getText().toString()),
                            Double.parseDouble(patientWeight.getText().toString())
                    );

                    Database.addPatient(newPatient);
                }

                Log.d("EditablePatientProfile", Database.getPatientByName(patientName.getText().toString()).toString());

                finish();
            } else {
                FastToast.show(this, "Please fill in all the fields.");
            }
        });
    }

    private boolean isPatientProfileValid() {
        return !patientName.getText().toString().isEmpty() &&
                !patientEmail.getText().toString().isEmpty() &&
                !patientDateOfBirth.getText().toString().isEmpty() &&
                !patientPhoneNumber.getText().toString().isEmpty() &&
                !patientHeight.getText().toString().isEmpty() &&
                !patientWeight.getText().toString().isEmpty();
    }
}