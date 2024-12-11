package com.example.healthconnect.appointmentHistory;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthconnect.R;
import com.example.healthconnect.patientHistory.PatientHistoryActivity;
import com.example.healthconnect.utils.database.Appointment;
import com.example.healthconnect.utils.database.Database;

import java.util.List;

public class AppointmentHistoryActivity extends AppCompatActivity implements AppointmentHistoryActivityAppointmentsAdapter.OnItemClickListener {
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment_history);
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
        statusBarTitle.setText(getString(R.string.appointment_history));

        EditText searchBar = findViewById(R.id.search_bar_input);

        RecyclerView patientList = findViewById(R.id.appointment_history_activity_patient_list);

        List<Appointment> appointments = Database.getDoneAppointments();

        AppointmentHistoryActivityAppointmentsAdapter adapter = new AppointmentHistoryActivityAppointmentsAdapter(appointments, this);
        patientList.setLayoutManager(new LinearLayoutManager(this));
        patientList.setAdapter(adapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.filter(editable.toString());
            }
        });
    }

    @Override
    public void onItemClick(Appointment appointment) {
        Intent intent = new Intent(AppointmentHistoryActivity.this, PatientHistoryActivity.class);
        intent.putExtra("patient", appointment.getPatient());
        startActivity(intent);
    }
}