package com.example.eduway;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class User extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView txtLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        // Handle edge-to-edge layout padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind views
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        // Redirect to login
        txtLogin.setOnClickListener(v -> {
            startActivity(new Intent(User.this, Login.class));
        });

        // Register button click
        btnRegister.setOnClickListener(v -> validateAndRegister());
    }

    private void validateAndRegister() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // 🔹 Validation checks with return (stops execution if invalid)
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Please enter your full name");
            etFullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Please enter your email");
            etEmail.requestFocus();
            return;
        }

        // ✅ Email format validation (main part)
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format! Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter a password");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        // ✅ If everything valid → Register in Firebase
        registerUser(fullName, email, password);
    }

    private void registerUser(String fullName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String userId = mAuth.getCurrentUser().getUid();

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("fullName", fullName);
                    userData.put("email", email);
                    userData.put("role", "user");

                    db.collection("Users").document(userId)
                            .set(userData)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(User.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(User.this, MapsActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(User.this, "Database Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(User.this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
