package com.example.healthconnect.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "health_connect.db";
    private static final int DATABASE_VERSION = 1;

    private static Database instance;

    private Database(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized void initializeInstance(Context context) {
        if (instance == null) {
            instance = new Database(context);
        }
    }

    private static synchronized Database getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Database is not initialized. Call initializeInstance(Context) first.");
        }
        return instance;
    }

    private static final String CREATE_TABLE_PATIENTS =
            "CREATE TABLE patients (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "date_of_birth INTEGER NOT NULL, " + // Format: YYYYMMDD
                    "gender TEXT NOT NULL, " +
                    "phone_number TEXT NOT NULL, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "height REAL NOT NULL, " +
                    "weight REAL NOT NULL" +
                    ");";

    private static final String CREATE_TABLE_APPOINTMENTS =
            "CREATE TABLE appointments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "patient_id INTEGER NOT NULL, " +
                    "appointment_type TEXT NOT NULL, " +
                    "appointment_date INTEGER NOT NULL, " + // Format: YYYYMMDD
                    "appointment_time INTEGER NOT NULL, " + // Format: HHMM
                    "notes TEXT, " +
                    "medicines TEXT NOT NULL, " +
                    "exams TEXT NOT NULL, " +
                    "is_done INTEGER NOT NULL DEFAULT 0, " +
                    "FOREIGN KEY(patient_id) REFERENCES patients(id)" +
                    ");";

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_PATIENTS);
        database.execSQL(CREATE_TABLE_APPOINTMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS patients;");
        database.execSQL("DROP TABLE IF EXISTS appointments;");
        onCreate(database);
    }

    public static List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        SQLiteDatabase database = getInstance().getReadableDatabase();

        Cursor cursor = database.query("patients",
                null,
                null,
                null,
                null,
                null,
                "name ASC");

        if (cursor.moveToFirst()) {
            do {
                patients.add(new Patient(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("date_of_birth")),
                        cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone_number")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("height")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("weight"))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return patients;
    }

    public static Patient getPatientById(int patientId) {
        SQLiteDatabase database = getInstance().getReadableDatabase();

        Cursor cursor = database.query(
                "patients",
                null,
                "id = ?",
                new String[]{String.valueOf(patientId)},
                null,
                null,
                null
        );

        Patient patient = null;

        if (cursor.moveToFirst()) {
            patient = new Patient(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("date_of_birth")),
                    cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phone_number")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("height")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("weight"))
            );
        }

        cursor.close();

        return patient;
    }

    public static Patient getPatientByName(String patientName) {
        SQLiteDatabase database = getInstance().getReadableDatabase();

        Cursor cursor = database.query(
                "patients",
                null,
                "name = ?",
                new String[]{patientName},
                null,
                null,
                null
        );

        Patient patient = null;

        if (cursor.moveToFirst()) {
            patient = new Patient(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("date_of_birth")),
                    cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phone_number")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("height")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("weight"))
            );
        }

        cursor.close();

        return patient;
    }

    public static long addPatient(Patient patient) {
        SQLiteDatabase database = getInstance().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", patient.getName());
        values.put("date_of_birth", patient.getDateOfBirth());
        values.put("gender", patient.getGender());
        values.put("phone_number", patient.getPhoneNumber());
        values.put("email", patient.getEmail());
        values.put("height", patient.getHeight());
        values.put("weight", patient.getWeight());

        long patientId = database.insert("patients", null, values);
        database.close();

        return patientId;
    }

    public static void updatePatient(Patient patient) {
        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", patient.getName());
        values.put("date_of_birth", patient.getDateOfBirth());
        values.put("gender", patient.getGender());
        values.put("phone_number", patient.getPhoneNumber());
        values.put("email", patient.getEmail());
        values.put("height", patient.getHeight());
        values.put("weight", patient.getWeight());

        database.update(
                "patients",
                values,
                "id = ?",
                new String[]{String.valueOf(patient.getId())}
        );

        database.close();
    }

    public static List<Appointment> getAllNotDoneAppointmentsByDate(int date) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase database = getInstance().getReadableDatabase();

        Cursor cursor = database.query(
                "appointments",
                null,
                "appointment_date = ? AND is_done = ?",
                new String[]{String.valueOf(date), "0"},
                null,
                null,
                "appointment_time ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                Appointment appointment = new Appointment(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        getPatientById(cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"))),
                        cursor.getString(cursor.getColumnIndexOrThrow("appointment_type")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("appointment_date")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("appointment_time")),
                        cursor.getString(cursor.getColumnIndexOrThrow("notes")),
                        cursor.getString(cursor.getColumnIndexOrThrow("medicines")),
                        cursor.getString(cursor.getColumnIndexOrThrow("exams")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("is_done")) == 1
                );

                appointments.add(appointment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return appointments;
    }

    public static List<Appointment> getTodayAppointments() {
        List<Appointment> todayAppointments = new ArrayList<>();
        SQLiteDatabase database = instance.getReadableDatabase();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Cursor cursor = database.query(
                "appointments",
                null,
                "appointment_date = ? AND is_done = ?",
                new String[]{String.valueOf(year * 10000 + month * 100 + day), "0"},
                null,
                null,
                "appointment_time ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                Appointment appointment = new Appointment(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        getPatientById(cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"))),
                        cursor.getString(cursor.getColumnIndexOrThrow("appointment_type")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("appointment_date")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("appointment_time")),
                        cursor.getString(cursor.getColumnIndexOrThrow("notes")),
                        cursor.getString(cursor.getColumnIndexOrThrow("medicines")),
                        cursor.getString(cursor.getColumnIndexOrThrow("exams")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("is_done")) == 1
                );
                todayAppointments.add(appointment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return todayAppointments;
    }

    public static List<Appointment> getDoneAppointments() {
        List<Appointment> doneAppointments = new ArrayList<>();
        SQLiteDatabase database = instance.getReadableDatabase();

        Cursor cursor = database.query(
                "appointments",
                null,
                "is_done = ?",
                new String[]{"1"},
                null,
                null,
                "appointment_date DESC, appointment_time DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                Appointment appointment = new Appointment(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        getPatientById(cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"))),
                        cursor.getString(cursor.getColumnIndexOrThrow("appointment_type")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("appointment_date")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("appointment_time")),
                        cursor.getString(cursor.getColumnIndexOrThrow("notes")),
                        cursor.getString(cursor.getColumnIndexOrThrow("medicines")),
                        cursor.getString(cursor.getColumnIndexOrThrow("exams")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("is_done")) == 1
                );
                doneAppointments.add(appointment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return doneAppointments;
    }

    public static List<Appointment> getDoneAppointmentsByPatientId(Patient patient) {
        List<Appointment> patientAppointments = new ArrayList<>();
        SQLiteDatabase database = instance.getReadableDatabase();

        Cursor cursor = database.query(
                "appointments",
                null,
                "patient_id = ? AND is_done = ?",
                new String[]{String.valueOf(patient.getId()), "1"},
                null,
                null,
                "appointment_date DESC, appointment_time DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                Appointment appointment = new Appointment(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        getPatientById(cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"))),
                        cursor.getString(cursor.getColumnIndexOrThrow("appointment_type")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("appointment_date")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("appointment_time")),
                        cursor.getString(cursor.getColumnIndexOrThrow("notes")),
                        cursor.getString(cursor.getColumnIndexOrThrow("medicines")),
                        cursor.getString(cursor.getColumnIndexOrThrow("exams")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("is_done")) == 1
                );
                patientAppointments.add(appointment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return patientAppointments;
    }

    public static Appointment getPreviousDoneAppointmentByPatientId(Patient patient) {
        Appointment previousAppointment = null;
        SQLiteDatabase database = instance.getReadableDatabase();

        Cursor cursor = database.query(
                "appointments",
                null,
                "patient_id = ? AND is_done = ?",
                new String[]{String.valueOf(patient.getId()), "1"},
                null,
                null,
                "appointment_date DESC, appointment_time DESC",
                "1"
        );

        if (cursor.moveToFirst()) {
            previousAppointment = new Appointment(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    getPatientById(cursor.getInt(cursor.getColumnIndexOrThrow("patient_id"))), // Assuming you have a method to get Patient by ID
                    cursor.getString(cursor.getColumnIndexOrThrow("appointment_type")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("appointment_date")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("appointment_time")),
                    cursor.getString(cursor.getColumnIndexOrThrow("notes")),
                    cursor.getString(cursor.getColumnIndexOrThrow("medicines")),
                    cursor.getString(cursor.getColumnIndexOrThrow("exams")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("is_done")) == 1
            );
        }

        cursor.close();
        database.close();

        return previousAppointment;
    }

    public static long addAppointment(Appointment appointment) {
        SQLiteDatabase database = getInstance().getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("patient_id", appointment.getPatient().getId());
        values.put("appointment_type", appointment.getAppointmentType());
        values.put("appointment_date", appointment.getAppointmentDate());
        values.put("appointment_time", appointment.getAppointmentTime());
        values.put("notes", appointment.getNotes());
        values.put("medicines", appointment.getMedicines());
        values.put("exams", appointment.getExams());
        values.put("is_done", appointment.isDone() ? 1 : 0);

        long id = database.insert("appointments", null, values);

        database.close();

        return id;
    }

    public static void updateAppointment(Appointment appointment) {
        SQLiteDatabase database = instance.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("patient_id", appointment.getPatient().getId());
        values.put("appointment_type", appointment.getAppointmentType());
        values.put("appointment_date", appointment.getAppointmentDate());
        values.put("appointment_time", appointment.getAppointmentTime());
        values.put("notes", appointment.getNotes());
        values.put("medicines", appointment.getMedicines());
        values.put("exams", appointment.getExams());
        values.put("is_done", appointment.isDone() ? 1 : 0);

        database.update(
                "appointments",
                values,
                "id = ?",
                new String[]{String.valueOf(appointment.getId())}
        );

        database.close();
    }

    public static void deleteAppointment(Appointment appointment) {
        SQLiteDatabase database = instance.getWritableDatabase();

        database.delete(
                "appointments",
                "id = ?",
                new String[]{String.valueOf(appointment.getId())}
        );

        database.close();
    }
}
