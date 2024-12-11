package com.example.healthconnect.appointmentHistory;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthconnect.R;
import com.example.healthconnect.utils.database.Appointment;

import java.util.ArrayList;
import java.util.List;

public class AppointmentHistoryActivityAppointmentsAdapter extends RecyclerView.Adapter<AppointmentHistoryActivityAppointmentsAdapter.AppointmentViewHolder> {

    private final List<Appointment> appointmentList;
    private final List<Appointment> appointmentListFull;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }

    public AppointmentHistoryActivityAppointmentsAdapter(List<Appointment> appointmentList, OnItemClickListener listener) {
        this.appointmentList = appointmentList;
        this.appointmentListFull = new ArrayList<>(appointmentList);
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String query) {
        appointmentList.clear();

        if (query.isEmpty()) {
            appointmentList.addAll(appointmentListFull);
        } else {
            for (Appointment appointment : appointmentListFull) {
                if (appointment.getPatient().getName().toLowerCase().contains(query.toLowerCase())) {
                    appointmentList.add(appointment);
                }
            }
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_appointment_history_item, parent, false);
        return new AppointmentViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);

        holder.patientName.setText(appointment.getPatient().getName());
        holder.patientGenderAge.setText(appointment.getPatient().getGender() + ", " + appointment.getPatient().getAge() + " years-old");
        String date = String.format("%04d-%02d-%02d",
                appointment.getAppointmentDate() / 10000,
                (appointment.getAppointmentDate() % 10000) / 100,
                appointment.getAppointmentDate() % 100);
        int hour = appointment.getAppointmentTime() / 100;
        int minute = appointment.getAppointmentTime() % 100;
        holder.appointmentTypeDateTime.setText("Regular Consultation" + " | " + date + " - " + String.format("%02d:%02d", hour, minute));

        holder.patientImage.setImageResource(R.drawable.default_profile_picture);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(appointment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        ImageView patientImage;
        TextView patientName;
        TextView patientGenderAge;
        TextView appointmentTypeDateTime;

        public AppointmentViewHolder(View itemView) {
            super(itemView);

            patientImage = itemView.findViewById(R.id.activity_appointment_history_item_patient_picture);
            patientName = itemView.findViewById(R.id.activity_appointment_history_item_patient_name);
            patientGenderAge = itemView.findViewById(R.id.activity_appointment_history_item_patient_gender_age);
            appointmentTypeDateTime = itemView.findViewById(R.id.activity_appointment_history_item_appointment_type_date_time);
        }
    }
}

