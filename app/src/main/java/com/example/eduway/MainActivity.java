package com.example.eduway;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnDriver = findViewById(R.id.btnDriver);
        btnDriver.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Driver Button Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Driver.class);
            startActivity(intent);
        });

        Button btnUser = findViewById(R.id.btnUser);
        btnUser.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "User Button Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, User.class);
            startActivity(intent);
        });

        findViewById(R.id.txtLogin).setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Login Text Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        });
    }
}
