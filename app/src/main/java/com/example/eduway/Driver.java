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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Driver extends AppCompatActivity {

    EditText etName, etBusNumber, etEmail, etPassword;
    Button btnRegister;
    TextView txtLogin;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering driver...");

        etName = findViewById(R.id.etName);
        etBusNumber = findViewById(R.id.etBusNumber);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        txtLogin.setOnClickListener(v -> {
            Intent i = new Intent(Driver.this, Login.class);
            startActivity(i);
        });

        btnRegister.setOnClickListener(v -> registerDriver());
    }

    private void registerDriver() {
        String name = etName.getText().toString().trim();
        String busNumber = etBusNumber.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // ✅ Empty fields check
        if (name.isEmpty() || busNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Strict email format check (only valid emails allowed)
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format! Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Password length check (minimum 6 characters)
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = auth.getCurrentUser().getUid();

                    Map<String, Object> driver = new HashMap<>();
                    driver.put("name", name);
                    driver.put("busNumber", busNumber);
                    driver.put("email", email);

                    db.collection("Driver").document(uid)
                            .set(driver)
                            .addOnSuccessListener(unused -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Driver Registered Successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Driver.this, MapsActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Firestore Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Auth Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
