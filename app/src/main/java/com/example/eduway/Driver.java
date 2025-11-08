package com.example.eduway;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Driver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver); // 👈 note this name matches your XML

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.txtLogin).setOnClickListener(v -> {
            Toast.makeText(Driver.this, "Login Text Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Driver.this, Login.class);
            startActivity(intent);
        });
        findViewById(R.id.btnRegister).setOnClickListener(v -> {
            Toast.makeText(Driver.this, "Login Text Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Driver.this, MapsActivity.class);
            startActivity(intent);
        });
    }
}
