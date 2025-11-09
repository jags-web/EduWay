package com.example.eduway;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvForgotPassword, tvSignup;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        // Initialize UI Elements
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignup = findViewById(R.id.tvSignup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");

        // Login Button Click
        btnLogin.setOnClickListener(v -> loginUser());

        // Forgot Password Click
        tvForgotPassword.setOnClickListener(v -> forgotPassword());

        // Signup Text Click
        tvSignup.setOnClickListener(v -> {
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // ✅ Empty fields check
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Strict email format check
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    // Redirect to next activity (MapsActivity or Home)
                    Intent i = new Intent(Login.this, MapsActivity.class);
                    startActivity(i);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void forgotPassword() {
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Enter your registered email to reset password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Sending password reset email...");
        progressDialog.show();

        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Password reset link sent to your email", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
