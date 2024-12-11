package com.example.healthconnect;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthconnect.doctorProfile.DoctorProfileActivity;
import com.example.healthconnect.login.LoginActivity;
import com.example.healthconnect.utils.FastSharedPreferences;
import com.example.healthconnect.utils.database.Database;
import com.example.healthconnect.utils.database.MockDataGenerator;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class WelcomeActivity extends AppCompatActivity {
    CircularProgressIndicator progressIndicator;
    TextView progressIndicatorText;

    final int COUNTDOWN_TIMER_IN_SECONDS = 3;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressIndicator = findViewById(R.id.welcome_activity_progress);
        progressIndicatorText = findViewById(R.id.welcome_activity_progress_text);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Database.initializeInstance(this);

        if (Database.getAllPatients().isEmpty()) {
            progressIndicator.setIndeterminate(true);

            progressIndicatorText.setVisibility(VISIBLE);

            new MockDataTask().execute();
        } else {
            progressIndicator.setIndeterminate(false);
            progressIndicator.setMax(100);
            progressIndicator.setProgress(0);

            progressIndicatorText.setVisibility(GONE);

            new CountDownTimer(COUNTDOWN_TIMER_IN_SECONDS * 1000, 1) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long elapsedMillis = (COUNTDOWN_TIMER_IN_SECONDS * 1000) - millisUntilFinished;
                    int percentage = (int) ((elapsedMillis / (float) (COUNTDOWN_TIMER_IN_SECONDS * 1000)) * 100);
                    progressIndicator.setProgress(percentage);
                }

                @Override
                public void onFinish() {
                    progressIndicator.setProgress(100);

                    onFinishLoading();

                    finish();
                }
            }.start();
        }
    }

    private class MockDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            MockDataGenerator.generateMockData();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            onFinishLoading();
            finish();
        }
    }

    private void onFinishLoading() {
        if (!FastSharedPreferences.get(WelcomeActivity.this, "doctor_name", "").equals("")) {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        } else {
            Intent intent = new Intent(WelcomeActivity.this, DoctorProfileActivity.class);
            intent.putExtra("isFirstTime", true);
            startActivity(intent);
        }
    }
}
