package com.example.healthconnect.patientHistory;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthconnect.R;
import com.example.healthconnect.utils.database.Appointment;

import java.util.List;

public class PatientHistoryActivityAppointmentsAdapter extends RecyclerView.Adapter<PatientHistoryActivityAppointmentsAdapter.AppointmentViewHolder> {
    private final List<Appointment> appointmentList;
    private final OnItemClickListener listener;

    private final boolean[] isExpanded;

    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }

    public PatientHistoryActivityAppointmentsAdapter(List<Appointment> appointmentList, OnItemClickListener listener) {
        this.appointmentList = appointmentList;
        this.listener = listener;
        this.isExpanded = new boolean[appointmentList.size()];
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_patient_history_item, parent, false);
        return new AppointmentViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        String date = String.format("%04d-%02d-%02d",
                appointment.getAppointmentDate() / 10000,
                (appointment.getAppointmentDate() % 10000) / 100,
                appointment.getAppointmentDate() % 100);
        int hour = appointment.getAppointmentTime() / 100;
        int minute = appointment.getAppointmentTime() % 100;
        holder.appointmentDateTime.setText(date + " - " + String.format("%02d:%02d", hour, minute));
        holder.appointmentType.setText("Regular Consultation");
        holder.notes.setText(appointment.getNotes());
        holder.medicines.setText(appointment.getMedicines());
        holder.exams.setText(appointment.getExams());

        if (isExpanded[position]) {
            holder.arrow.setImageResource(R.drawable.arrow_up_icon);
            holder.extraInfo.setVisibility(VISIBLE);
        } else {
            holder.arrow.setImageResource(R.drawable.arrow_down_icon);
            holder.extraInfo.setVisibility(GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            isExpanded[position] = !isExpanded[position];

            notifyItemChanged(position);

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
        TextView appointmentDateTime;
        TextView appointmentType;
        ImageView arrow;
        LinearLayout extraInfo;
        TextView notes;
        TextView medicines;
        TextView exams;

        public AppointmentViewHolder(View itemView) {
            super(itemView);

            appointmentDateTime = itemView.findViewById(R.id.activity_patient_history_item_appointment_date_time);
            appointmentType = itemView.findViewById(R.id.activity_patient_history_item_appointment_type);
            arrow = itemView.findViewById(R.id.activity_patient_history_item_arrow);
            extraInfo = itemView.findViewById(R.id.activity_patient_history_item_appointment_extra_info_layout);
            notes = itemView.findViewById(R.id.activity_patient_history_item_appointment_notes);
            medicines = itemView.findViewById(R.id.activity_patient_history_item_appointment_medicines);
            exams = itemView.findViewById(R.id.activity_patient_history_item_appointment_exams);
        }
    }
}
