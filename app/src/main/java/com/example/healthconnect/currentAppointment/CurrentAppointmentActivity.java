package com.example.healthconnect.currentAppointment;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthconnect.R;
import com.example.healthconnect.patientHistory.PatientHistoryActivity;
import com.example.healthconnect.patientProfile.PatientProfileActivity;
import com.example.healthconnect.utils.database.Appointment;
import com.example.healthconnect.utils.database.Database;

public class CurrentAppointmentActivity extends AppCompatActivity {
    @SuppressLint({"SourceLockedOrientationActivity", "SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_current_appointment);
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
        statusBarIcon.setImageResource(R.drawable.stethoscope_white);
        statusBarTitle.setText(getString(R.string.current_appointment));

        ConstraintLayout patientInfo = findViewById(R.id.current_appointment_activity_patient_info_layout);
        ImageView patientPicture = findViewById(R.id.current_appointment_activity_appointment_item_image);
        TextView patientName = findViewById(R.id.current_appointment_activity_appointment_item_patient_name);
        TextView patientAge = findViewById(R.id.current_appointment_activity_appointment_item_patient_age);
        TextView appointmentType = findViewById(R.id.current_appointment_activity_appointment_item_appointment_type);
        TextView appointmentTime = findViewById(R.id.current_appointment_activity_appointment_item_appointment_time);
        EditText notes = findViewById(R.id.current_appointment_activity_notes);
        EditText medicines = findViewById(R.id.current_appointment_activity_medicines);
        EditText exams = findViewById(R.id.current_appointment_activity_exams);
        AppCompatButton save = findViewById(R.id.current_appointment_activity_save_button);
        TextView previousAppointmentTitle = findViewById(R.id.current_appointment_activity_previous_appointment_title);
        ConstraintLayout previousAppointmentLayout = findViewById(R.id.current_appointment_activity_previous_appointment_info_layout);
        TextView previousAppointmentDateTime = findViewById(R.id.current_appointment_activity_previous_appointment_date_time);
        TextView previousAppointmentType = findViewById(R.id.current_appointment_activity_previous_appointment_type);

        Appointment appointment = (Appointment) getIntent().getSerializableExtra("appointment");
        patientPicture.setImageResource(R.drawable.default_profile_picture);
        patientName.setText(appointment != null ? appointment.getPatient().getName() : "");
        patientAge.setText(appointment != null ? appointment.getPatient().getAge() + " years-old" : "");
        appointmentType.setText(appointment != null ? "Regular Consultation" : "");
        appointmentTime.setText(appointment != null ? String.format("%02d:%02d", appointment.getAppointmentTime() / 100, appointment.getAppointmentTime() % 100) : "");
        notes.setText(appointment != null ? appointment.getNotes() : "");
        medicines.setText(appointment != null ? appointment.getMedicines() : "");
        exams.setText(appointment != null ? appointment.getExams() : "");

        Appointment previousAppointment = null;

        if (appointment != null) {
            previousAppointment = Database.getPreviousDoneAppointmentByPatientId(appointment.getPatient());
        }

        if (previousAppointment != null) {
            previousAppointmentTitle.setVisibility(VISIBLE);
            previousAppointmentLayout.setVisibility(VISIBLE);

            previousAppointmentDateTime.setText(
                    String.format("%04d-%02d-%02d",
                            previousAppointment.getAppointmentDate() / 10000,
                            (previousAppointment.getAppointmentDate() % 10000) / 100,
                            previousAppointment.getAppointmentDate() % 100) +
                            " - " +
                            String.format(
                                    "%02d:%02d", previousAppointment.getAppointmentTime() / 100,
                                    previousAppointment.getAppointmentTime() % 100)
            );

            previousAppointmentType.setText(previousAppointment.getAppointmentType());
        } else {
            previousAppointmentTitle.setVisibility(GONE);
            previousAppointmentLayout.setVisibility(GONE);
        }

        patientInfo.setOnClickListener(v -> {
            Intent intent = new Intent(CurrentAppointmentActivity.this, PatientProfileActivity.class);
            assert appointment != null;
            intent.putExtra("patient", appointment.getPatient());
            startActivity(intent);
        });

        save.setOnClickListener(v -> {
            if (appointment != null) {
                appointment.setNotes(notes.getText().toString());
                appointment.setMedicines(medicines.getText().toString());
                appointment.setExams(exams.getText().toString());
                appointment.setDone(true);

                Database.updateAppointment(appointment);
            }

            finish();
        });

        previousAppointmentLayout.setOnClickListener(v -> {
            Intent intent = new Intent(CurrentAppointmentActivity.this, PatientHistoryActivity.class);
            assert appointment != null;
            intent.putExtra("patient", appointment.getPatient());
            startActivity(intent);
        });
    }
}