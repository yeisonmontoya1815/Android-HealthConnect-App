package com.example.healthconnect.patientHistory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthconnect.R;
import com.example.healthconnect.patientProfile.PatientProfileActivity;
import com.example.healthconnect.utils.database.Appointment;
import com.example.healthconnect.utils.database.Database;
import com.example.healthconnect.utils.database.Patient;

import java.util.List;

public class PatientHistoryActivity extends AppCompatActivity implements PatientHistoryActivityAppointmentsAdapter.OnItemClickListener {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView statusBackIcon = findViewById(R.id.status_bar_back_arrow_icon);
        ImageView statusBarIcon = findViewById(R.id.status_bar_icon);
        TextView statusBarTitle = findViewById(R.id.status_bar_title);
        statusBackIcon.setOnClickListener(v -> finish());
        statusBarIcon.setImageResource(R.drawable.stethoscope_white);
        statusBarTitle.setText(getString(R.string.patients_history));

        ConstraintLayout patientInfoLayout = findViewById(R.id.patient_history_activity_patient_info_layout);
        ImageView patientPicture = findViewById(R.id.patient_history_activity_item_image);
        TextView patientName = findViewById(R.id.patient_history_activity_patient_name);
        TextView patientAge = findViewById(R.id.patient_history_activity_patient_age);

        RecyclerView appointmentList = findViewById(R.id.patient_history_activity_appointment_list);

        Patient patient = (Patient) getIntent().getSerializableExtra("patient");

        patientPicture.setImageResource(R.drawable.default_profile_picture);
        patientName.setText(patient != null ? patient.getName() : "");
        patientAge.setText(patient.getAge() + " years-old");

        List<Appointment> appointments = Database.getDoneAppointmentsByPatientId(patient);

        PatientHistoryActivityAppointmentsAdapter adapter = new PatientHistoryActivityAppointmentsAdapter(appointments, this);
        appointmentList.setLayoutManager(new LinearLayoutManager(this));
        appointmentList.setAdapter(adapter);

        patientInfoLayout.setOnClickListener(v -> {
            Intent intent = new Intent(PatientHistoryActivity.this, PatientProfileActivity.class);
            intent.putExtra("patient", patient);
            startActivity(intent);
        });
    }

    @Override
    public void onItemClick(Appointment appointment) {
        // Do nothing
    }
}