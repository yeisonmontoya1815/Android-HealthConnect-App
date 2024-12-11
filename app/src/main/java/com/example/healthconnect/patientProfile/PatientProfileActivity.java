package com.example.healthconnect.patientProfile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthconnect.R;
import com.example.healthconnect.patientHistory.PatientHistoryActivity;
import com.example.healthconnect.utils.database.Database;
import com.example.healthconnect.utils.database.Patient;

public class PatientProfileActivity extends AppCompatActivity {
    ImageView patientPicture;
    TextView patientName;
    TextView patientEmail;
    TextView patientPhoneNumber;
    TextView patientDateOfBirthAge;
    TextView patientHeight;
    TextView patientWeight;
    TextView patientGender;

    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_profile);
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

        ImageView editButton = findViewById(R.id.patient_profile_activity_edit_patient_info);

        patientPicture = findViewById(R.id.patient_profile_activity_patient_picture);
        patientName = findViewById(R.id.patient_profile_activity_patient_name);
        patientEmail = findViewById(R.id.patient_profile_activity_patient_email);
        patientPhoneNumber = findViewById(R.id.patient_profile_activity_patient_phone_number);
        patientDateOfBirthAge = findViewById(R.id.patient_profile_activity_patient_birth_date_age);
        patientHeight = findViewById(R.id.patient_profile_activity_patient_height);
        patientWeight = findViewById(R.id.patient_profile_activity_patient_weight);
        patientGender = findViewById(R.id.patient_profile_activity_patient_gender);

        LinearLayout appointmentHistoryButton = findViewById(R.id.patient_profile_activity_patient_appointment_history_layout);

        patient = (Patient) getIntent().getSerializableExtra("patient");

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(PatientProfileActivity.this, EditablePatientProfileActivity.class);
            intent.putExtra("patient", patient);
            startActivity(intent);
        });

        appointmentHistoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(PatientProfileActivity.this, PatientHistoryActivity.class);
            intent.putExtra("patient", patient);
            startActivity(intent);
        });
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onResume() {
        super.onResume();

        patient = Database.getPatientById(patient.getId());

        patientPicture.setImageResource(R.drawable.default_profile_picture);
        patientName.setText(patient.getName());
        patientEmail.setText(patient.getEmail());
        patientPhoneNumber.setText(patient.getPhoneNumber());
        patientHeight.setText("Height\n" + formatMeasurement(patient.getHeight()) + " cm");
        patientWeight.setText("Weight\n" + formatMeasurement(patient.getWeight()) + " Kg");
        patientGender.setText("Gender\n" + patient.getGender());

        int tempDateOfBirth = patient != null ? patient.getDateOfBirth() : 0;

        String dateOfBirth = String.format("%04d-%02d-%02d",
                tempDateOfBirth / 10000,
                (tempDateOfBirth % 10000) / 100,
                tempDateOfBirth % 100);

        patientDateOfBirthAge.setText(dateOfBirth + " (" + patient.getAge() + " years-old )");
    }

    @SuppressLint("DefaultLocale")
    private String formatMeasurement(double value) {
        if (value == (int) value) {
            return String.format("%d", (int) value);
        } else {
            return String.format("%.2f", value);
        }
    }
}