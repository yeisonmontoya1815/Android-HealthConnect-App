package com.example.healthconnect.appointment;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.healthconnect.utils.ConfirmationDialog.showConfirmationDialog;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthconnect.R;
import com.example.healthconnect.currentAppointment.CurrentAppointmentActivity;
import com.example.healthconnect.utils.database.Appointment;
import com.example.healthconnect.utils.database.Database;
import com.example.healthconnect.utils.database.Patient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity {
    AutoCompleteTextView patientNameInput;
    AppCompatButton date;
    AppCompatButton time;

    Appointment appointment;

    String appointmentDate;
    String appointmentTime;

    int patientId;

    @SuppressLint({"SourceLockedOrientationActivity", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment);
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
        statusBarIcon.setImageResource(R.drawable.scheduling_white);
        statusBarTitle.setText(getString(R.string.appointment));

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        patientNameInput = findViewById(R.id.appointment_activity_patient_name);
        date = findViewById(R.id.appointment_activity_appointment_date);
        time = findViewById(R.id.appointment_activity_appointment_time);
        ImageView cancelAppointment = findViewById(R.id.appointment_activity_cancel_appointment);
        ImageView startAppointment = findViewById(R.id.appointment_activity_start_appointment);
        AppCompatButton cancelButton = findViewById(R.id.appointment_activity_cancel_button);
        AppCompatButton saveButton = findViewById(R.id.appointment_activity_save_button);

        appointment = (Appointment) getIntent().getSerializableExtra("appointment");
        patientNameInput.setText(appointment != null ? appointment.getPatient().getName() : "");

        patientId = appointment != null ? appointment.getPatient().getId() : 0;

        int tempAppointmentDate = appointment != null ? appointment.getAppointmentDate() : 0;

        if (tempAppointmentDate == 0) {
            appointmentDate = String.format("%04d-%02d-%02d",
                    year,
                    month + 1,
                    day
            );
        } else {
            appointmentDate = String.format("%04d-%02d-%02d",
                    tempAppointmentDate / 10000,
                    (tempAppointmentDate % 10000) / 100,
                    tempAppointmentDate % 100);
        }

        date.setText(appointmentDate);

        int tempAppointmentTime = appointment != null ? appointment.getAppointmentTime() : 0;

        if (tempAppointmentTime == 0) {
            appointmentTime = "16:30";
        } else {
            appointmentTime = String.format("%02d:%02d",
                    tempAppointmentTime / 100,
                    tempAppointmentTime % 100);
        }

        time.setText(appointmentTime);

        boolean isNewAppointment = getIntent().getBooleanExtra("isNewAppointment", false);

        if (isNewAppointment) {
            patientNameInput.setEnabled(true);
            cancelAppointment.setVisibility(GONE);
            cancelButton.setVisibility(VISIBLE);
        } else {
            patientNameInput.setEnabled(false);
            cancelAppointment.setVisibility(VISIBLE);
            cancelButton.setVisibility(GONE);
        }

        List<String> patients = new ArrayList<>();
        for (Patient patient : Database.getAllPatients()) {
            patients.add(patient.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                patients
        );

        patientNameInput.setAdapter(adapter);

        patientNameInput.setOnItemClickListener((parent, view, position, id) -> {
            Patient selectedPatient = Database.getPatientByName(parent.getItemAtPosition(position).toString());

            if (selectedPatient != null) {
                patientNameInput.setText(selectedPatient.getName());
                patientId = selectedPatient.getId();
            }
        });

        date.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AppointmentActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        appointmentDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        date.setText(appointmentDate);
                    },
                    appointment != null ? appointment.getAppointmentDate() / 10000 : year,
                    appointment != null ? (appointment.getAppointmentDate() % 10000) / 100 - 1 : month,
                    appointment != null ? appointment.getAppointmentDate() % 100 : day
            );

            datePickerDialog.show();
        });

        time.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    v.getContext(),
                    (view, selectedHour, selectedMinute) -> {
                        appointmentTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        time.setText(appointmentTime);
                    },
                    appointment != null ? appointment.getAppointmentTime() / 100 : hour,
                    appointment != null ? appointment.getAppointmentTime() % 100 : minute,
                    true // True for 24-hour format, false for AM/PM format
            );

            timePickerDialog.show();
        });


        cancelAppointment.setOnClickListener(v ->
                showConfirmationDialog(
                        this,
                        "Cancel Appointment",
                        "Are you sure you want to cancel this appointment?",
                        "Yes",
                        "No",
                        () -> {
                            Database.deleteAppointment(appointment);
                            finish();
                        },
                        () -> {
                            // Do nothing
                        }
                )
        );


        startAppointment.setOnClickListener(v -> {
            if (!isAppointmentValid() || Database.getPatientById(patientId) == null) {
                return;
            }

            saveUpdateAppointment();

            Intent intent = new Intent(AppointmentActivity.this, CurrentAppointmentActivity.class);
            intent.putExtra("appointment", appointment);
            startActivity(intent);

            finish();
        });

        saveButton.setOnClickListener(v -> {
            if (!isAppointmentValid() || Database.getPatientById(patientId) == null) {
                return;
            }

            saveUpdateAppointment();
            finish();
        });

        cancelButton.setOnClickListener(v -> showConfirmationDialog(
                this,
                "Cancel Appointment",
                "Are you sure you want to cancel this action?\nUnsaved changes will be lost.",
                "Yes",
                "No",
                this::finish,
                () -> {
                    // Do nothing
                }
        ));
    }

    private void saveUpdateAppointment() {
        if (appointment == null) {
            appointment = new Appointment(
                    0,
                    Database.getPatientByName(patientNameInput.getText().toString()),
                    "",
                    Integer.parseInt(appointmentDate.replace("-", "")),
                    Integer.parseInt(appointmentTime.replace(":", "")),
                    "",
                    "",
                    "",
                    false
            );

            long appointmentId = Database.addAppointment(appointment);
            appointment.setId(Integer.parseInt(String.valueOf(appointmentId)));
        } else {
            appointment.setPatient(Database.getPatientByName(patientNameInput.getText().toString()));
            appointment.setAppointmentDate(Integer.parseInt(appointmentDate.replace("-", "")));
            appointment.setAppointmentTime(Integer.parseInt(appointmentTime.replace(":", "")));

            Database.updateAppointment(appointment);
        }
    }

    private boolean isAppointmentValid() {
        return !patientNameInput.getText().toString().isEmpty() &&
                !appointmentDate.isEmpty() &&
                !appointmentTime.isEmpty();
    }
}