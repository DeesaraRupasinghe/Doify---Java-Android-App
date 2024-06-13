package com.example.doify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class first_screen extends AppCompatActivity {


    private static final long DELAY_TIME = 5000; // 5 seconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Delay for 5 seconds and then open LoginActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openLoginScreen();
            }
        }, DELAY_TIME);
    }

    private void openLoginScreen() {
        Intent intent = new Intent(first_screen.this, signUp.class);
        startActivity(intent);
        finish(); // Optional, if you don't want the user to come back to this screen
    }
}
