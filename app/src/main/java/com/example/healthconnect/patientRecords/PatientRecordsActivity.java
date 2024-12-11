package com.example.healthconnect.patientRecords;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.healthconnect.patientProfile.EditablePatientProfileActivity;
import com.example.healthconnect.patientProfile.PatientProfileActivity;
import com.example.healthconnect.utils.database.Database;
import com.example.healthconnect.utils.database.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientRecordsActivity extends AppCompatActivity implements PatientRecordsActivityPatientAdapter.OnItemClickListener {
    List<Patient> patients;

    RecyclerView patientsList;
    PatientRecordsActivityPatientAdapter adapter;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_records);
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
        statusBarTitle.setText(getString(R.string.patients_records));

        ImageView addPatientButton = findViewById(R.id.patient_records_activity_add_appointment);
        addPatientButton.setOnClickListener(view -> startActivity(new Intent(PatientRecordsActivity.this, EditablePatientProfileActivity.class)));

        patients = new ArrayList<>();
        adapter = new PatientRecordsActivityPatientAdapter(patients, this);

        EditText searchBar = findViewById(R.id.search_bar_input);

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

        patientsList = findViewById(R.id.patient_records_activity_patient_list);
        patientsList.setLayoutManager(new LinearLayoutManager(this));
        patientsList.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();

        try {
            patients.clear();
        } catch (Exception e) {
            Log.e("PatientRecordsActivity", "Error clearing appointments: ", e);
        }

        patients.addAll(Database.getAllPatients());

        if (adapter != null) {
            adapter.updatePatientList(patients);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(Patient patient) {
        Intent intent = new Intent(PatientRecordsActivity.this, PatientProfileActivity.class);
        intent.putExtra("patient", patient);
        startActivity(intent);
    }
}