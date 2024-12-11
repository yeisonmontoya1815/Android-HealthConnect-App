package com.example.healthconnect.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthconnect.R;
import com.example.healthconnect.home.HomeActivity;
import com.example.healthconnect.utils.FastSharedPreferences;
import com.example.healthconnect.utils.FastToast;

public class LoginActivity extends AppCompatActivity {
    EditText loginEmailEDT;
    EditText loginPasswordEDT;

    String doctorEmail;
    String doctorPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AppCompatButton loginButton = findViewById(R.id.login_activity_button);

        loginEmailEDT = findViewById(R.id.login_activity_email);
        loginPasswordEDT = findViewById(R.id.login_activity_password);

        doctorEmail = (String) FastSharedPreferences.get(this, "doctor_email", "");
        doctorPassword = (String) FastSharedPreferences.get(this, "doctor_password", "");

        loginButton.setOnClickListener(view -> {
            if (areCredentialsValid()) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            } else {
                FastToast.show(getApplicationContext(), "Invalid credentials");
            }
        });
    }

    boolean areCredentialsValid() {
        return (doctorEmail.equals(loginEmailEDT.getText().toString()) && doctorPassword.equals(loginPasswordEDT.getText().toString()));
    }
}