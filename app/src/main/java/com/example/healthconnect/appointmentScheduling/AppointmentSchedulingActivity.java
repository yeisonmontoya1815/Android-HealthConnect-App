package com.example.healthconnect.appointmentScheduling;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthconnect.R;
import com.example.healthconnect.appointment.AppointmentActivity;
import com.example.healthconnect.utils.database.Appointment;
import com.example.healthconnect.utils.database.Database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AppointmentSchedulingActivity extends AppCompatActivity implements AppointmentSchedulingActivityAppoitmentsAdapter.OnItemClickListener {
    AppCompatButton sunday;
    AppCompatButton monday;
    AppCompatButton tuesday;
    AppCompatButton wednesday;
    AppCompatButton thursday;
    AppCompatButton friday;
    AppCompatButton saturday;

    List<Appointment> appointments;
    AppointmentSchedulingActivityAppoitmentsAdapter adapter;

    int selectedDate;

    @SuppressLint({"SourceLockedOrientationActivity", "SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment_scheduling);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        ImageView statusBackIcon = findViewById(R.id.status_bar_back_arrow_icon);
        ImageView statusBarIcon = findViewById(R.id.status_bar_icon);
        TextView statusBarTitle = findViewById(R.id.status_bar_title);
        statusBackIcon.setOnClickListener(v -> finish());
        statusBarIcon.setImageResource(R.drawable.scheduling_white);
        statusBarTitle.setText(getString(R.string.appointments_scheduling));

        sunday = findViewById(R.id.vertical_calendar_sunday);
        monday = findViewById(R.id.vertical_calendar_monday);
        tuesday = findViewById(R.id.vertical_calendar_tuesday);
        wednesday = findViewById(R.id.vertical_calendar_wednesday);
        thursday = findViewById(R.id.vertical_calendar_thursday);
        friday = findViewById(R.id.vertical_calendar_friday);
        saturday = findViewById(R.id.vertical_calendar_saturday);
        TextView monthYear = findViewById(R.id.appointment_scheduling_activity_vertical_calendar_month_year);

        selectedDate = year * 10000 + month * 100 + dayOfMonth;
        monthYear.setText(months[month] + ", " + year);

        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                sunday.setBackgroundTintList(getResources().getColorStateList(R.color.black));
                sunday.setText(String.valueOf(dayOfMonth));
                monday.setText(String.valueOf(dayOfMonth + 1));
                tuesday.setText(String.valueOf(dayOfMonth + 2));
                wednesday.setText(String.valueOf(dayOfMonth + 3));
                thursday.setText(String.valueOf(dayOfMonth + 4));
                friday.setText(String.valueOf(dayOfMonth + 5));
                saturday.setText(String.valueOf(dayOfMonth + 6));
                break;
            case Calendar.MONDAY:
                monday.setBackgroundTintList(getResources().getColorStateList(R.color.black));
                sunday.setText(String.valueOf(dayOfMonth - 1));
                monday.setText(String.valueOf(dayOfMonth));
                tuesday.setText(String.valueOf(dayOfMonth + 1));
                wednesday.setText(String.valueOf(dayOfMonth + 2));
                thursday.setText(String.valueOf(dayOfMonth + 3));
                friday.setText(String.valueOf(dayOfMonth + 4));
                saturday.setText(String.valueOf(dayOfMonth + 5));
                break;
            case Calendar.TUESDAY:
                tuesday.setBackgroundTintList(getResources().getColorStateList(R.color.black));
                sunday.setText(String.valueOf(dayOfMonth - 2));
                monday.setText(String.valueOf(dayOfMonth - 1));
                tuesday.setText(String.valueOf(dayOfMonth));
                wednesday.setText(String.valueOf(dayOfMonth + 1));
                thursday.setText(String.valueOf(dayOfMonth + 2));
                friday.setText(String.valueOf(dayOfMonth + 3));
                saturday.setText(String.valueOf(dayOfMonth + 4));
                break;
            case Calendar.WEDNESDAY:
                wednesday.setBackgroundTintList(getResources().getColorStateList(R.color.black));
                sunday.setText(String.valueOf(dayOfMonth - 3));
                monday.setText(String.valueOf(dayOfMonth - 2));
                tuesday.setText(String.valueOf(dayOfMonth - 1));
                wednesday.setText(String.valueOf(dayOfMonth));
                thursday.setText(String.valueOf(dayOfMonth + 1));
                friday.setText(String.valueOf(dayOfMonth + 2));
                saturday.setText(String.valueOf(dayOfMonth + 3));
                break;
            case Calendar.THURSDAY:
                thursday.setBackgroundTintList(getResources().getColorStateList(R.color.black));
                sunday.setText(String.valueOf(dayOfMonth - 4));
                monday.setText(String.valueOf(dayOfMonth - 3));
                tuesday.setText(String.valueOf(dayOfMonth - 2));
                wednesday.setText(String.valueOf(dayOfMonth - 1));
                thursday.setText(String.valueOf(dayOfMonth));
                friday.setText(String.valueOf(dayOfMonth + 1));
                saturday.setText(String.valueOf(dayOfMonth + 2));
                break;
            case Calendar.FRIDAY:
                friday.setBackgroundTintList(getResources().getColorStateList(R.color.black));
                sunday.setText(String.valueOf(dayOfMonth - 5));
                monday.setText(String.valueOf(dayOfMonth - 4));
                tuesday.setText(String.valueOf(dayOfMonth - 3));
                wednesday.setText(String.valueOf(dayOfMonth - 2));
                thursday.setText(String.valueOf(dayOfMonth - 1));
                friday.setText(String.valueOf(dayOfMonth));
                saturday.setText(String.valueOf(dayOfMonth + 1));
                break;
            case Calendar.SATURDAY:
                saturday.setBackgroundTintList(getResources().getColorStateList(R.color.black));
                sunday.setText(String.valueOf(dayOfMonth - 6));
                monday.setText(String.valueOf(dayOfMonth - 5));
                tuesday.setText(String.valueOf(dayOfMonth - 4));
                wednesday.setText(String.valueOf(dayOfMonth - 3));
                thursday.setText(String.valueOf(dayOfMonth - 2));
                friday.setText(String.valueOf(dayOfMonth - 1));
                saturday.setText(String.valueOf(dayOfMonth));
                break;
        }

        sunday.setOnClickListener(v -> {
            resetBackgroundColor();
            sunday.setBackgroundTintList(getResources().getColorStateList(R.color.black));
            selectedDate = year * 10000 + month * 100 + Integer.parseInt(sunday.getText().toString());
            onResume();
        });

        monday.setOnClickListener(v -> {
            resetBackgroundColor();
            monday.setBackgroundTintList(getResources().getColorStateList(R.color.black));
            selectedDate = year * 10000 + month * 100 + Integer.parseInt(monday.getText().toString());
            onResume();
        });

        tuesday.setOnClickListener(v -> {
            resetBackgroundColor();
            tuesday.setBackgroundTintList(getResources().getColorStateList(R.color.black));
            selectedDate = year * 10000 + month * 100 + Integer.parseInt(tuesday.getText().toString());
            onResume();
        });

        wednesday.setOnClickListener(v -> {
            resetBackgroundColor();
            wednesday.setBackgroundTintList(getResources().getColorStateList(R.color.black));
            selectedDate = year * 10000 + month * 100 + Integer.parseInt(wednesday.getText().toString());
            onResume();
        });

        thursday.setOnClickListener(v -> {
            resetBackgroundColor();
            thursday.setBackgroundTintList(getResources().getColorStateList(R.color.black));
            selectedDate = year * 10000 + month * 100 + Integer.parseInt(thursday.getText().toString());
            onResume();
        });

        friday.setOnClickListener(v -> {
            resetBackgroundColor();
            friday.setBackgroundTintList(getResources().getColorStateList(R.color.black));
            selectedDate = year * 10000 + month * 100 + Integer.parseInt(friday.getText().toString());
            onResume();
        });

        saturday.setOnClickListener(v -> {
            resetBackgroundColor();
            saturday.setBackgroundTintList(getResources().getColorStateList(R.color.black));
            selectedDate = year * 10000 + month * 100 + Integer.parseInt(saturday.getText().toString());
            onResume();
        });

        RecyclerView upcomingAppointments = findViewById(R.id.appointment_scheduling_activity_appointments_list);
        ImageView addAppointment = findViewById(R.id.appointment_scheduling_activity_add_appointment);

        appointments = new ArrayList<>();

        adapter = new AppointmentSchedulingActivityAppoitmentsAdapter(appointments, this);
        upcomingAppointments.setLayoutManager(new LinearLayoutManager(this));
        upcomingAppointments.setAdapter(adapter);

        addAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(AppointmentSchedulingActivity.this, AppointmentActivity.class);
            intent.putExtra("isNewAppointment", true);
            startActivity(intent);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();

        try {
            appointments.clear();
        } catch (Exception e) {
            Log.e("AppointmentSchedulingActivity", "Error clearing appointments: ", e);
        }

        appointments.addAll(Database.getAllNotDoneAppointmentsByDate(selectedDate + 100));

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(Appointment appointment) {
        Intent intent = new Intent(AppointmentSchedulingActivity.this, AppointmentActivity.class);
        intent.putExtra("appointment", appointment);
        startActivity(intent);
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private void resetBackgroundColor() {
        sunday.setBackgroundTintList(getResources().getColorStateList(R.color.bondi_blue));
        monday.setBackgroundTintList(getResources().getColorStateList(R.color.bondi_blue));
        tuesday.setBackgroundTintList(getResources().getColorStateList(R.color.bondi_blue));
        wednesday.setBackgroundTintList(getResources().getColorStateList(R.color.bondi_blue));
        thursday.setBackgroundTintList(getResources().getColorStateList(R.color.bondi_blue));
        friday.setBackgroundTintList(getResources().getColorStateList(R.color.bondi_blue));
        saturday.setBackgroundTintList(getResources().getColorStateList(R.color.bondi_blue));
    }
}