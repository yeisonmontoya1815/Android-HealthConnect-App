package com.example.healthconnect.patientRecords;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthconnect.R;
import com.example.healthconnect.utils.database.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientRecordsActivityPatientAdapter extends RecyclerView.Adapter<PatientRecordsActivityPatientAdapter.PatientViewHolder> {
    private final List<Patient> patientList;
    private final List<Patient> patientListFull;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Patient patient);
    }

    public PatientRecordsActivityPatientAdapter(List<Patient> patientList, OnItemClickListener listener) {
        this.patientList = patientList;
        this.patientListFull = new ArrayList<>();
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String query) {
        patientList.clear();

        if (query.isEmpty()) {
            patientList.addAll(patientListFull);
        } else {
            for (Patient patient : patientListFull) {
                if (patient.getName().toLowerCase().contains(query.toLowerCase())) {
                    patientList.add(patient);
                }
            }
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_patient_records_item, parent, false);
        return new PatientViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patientList.get(position);

        holder.patientName.setText(patient.getName());
        holder.patientGenderAge.setText(patient.getGender() + ", " + patient.getAge() + " years-old");
        holder.patientPhoneNumberEmail.setText(patient.getPhoneNumber() + " | " + patient.getEmail());

        holder.patientImage.setImageResource(R.drawable.default_profile_picture);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(patient);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {
        ImageView patientImage;
        TextView patientName;
        TextView patientGenderAge;
        TextView patientPhoneNumberEmail;

        public PatientViewHolder(View itemView) {
            super(itemView);

            patientImage = itemView.findViewById(R.id.activity_patient_records_item_patient_picture);
            patientName = itemView.findViewById(R.id.activity_patient_records_item_patient_name);
            patientGenderAge = itemView.findViewById(R.id.activity_patient_records_item_patient_gender_age);
            patientPhoneNumberEmail = itemView.findViewById(R.id.activity_patient_records_item_patient_phone_number_email);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updatePatientList(List<Patient> newPatientList) {
        patientListFull.clear();
        patientListFull.addAll(newPatientList);

        patientList.clear();
        patientList.addAll(patientListFull);
    }
}
